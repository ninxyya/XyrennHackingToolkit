package com.xyrenn.hacking.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xyrenn.hacking.data.local.converters.Converters
import com.xyrenn.hacking.data.local.dao.LogDao
import com.xyrenn.hacking.data.local.dao.ToolDao
import com.xyrenn.hacking.data.local.dao.UserDao
import com.xyrenn.hacking.data.local.entities.LogEntity
import com.xyrenn.hacking.data.local.entities.ToolEntity
import com.xyrenn.hacking.data.local.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        UserEntity::class,
        ToolEntity::class,
        LogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun toolDao(): ToolDao
    abstract fun logDao(): LogDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        private val coroutineScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            coroutineScope.launch {
                val dao = database.get().toolDao()
                // Insert default tools
                val defaultTools = listOf(
                    ToolEntity(
                        name = "WiFi Killer",
                        description = "Kick devices from WiFi networks",
                        category = "wifi",
                        icon = "⚡",
                        order = 1
                    ),
                    ToolEntity(
                        name = "Deauther",
                        description = "Deauth attack on clients",
                        category = "wifi",
                        icon = "🔌",
                        order = 2
                    ),
                    ToolEntity(
                        name = "SMS Spammer",
                        description = "Send multiple SMS messages",
                        category = "spam",
                        icon = "💬",
                        order = 3
                    ),
                    ToolEntity(
                        name = "DDoS Attack",
                        description = "Distributed denial of service",
                        category = "network",
                        icon = "🌐",
                        order = 4
                    ),
                    ToolEntity(
                        name = "Remote Control",
                        description = "Control devices remotely",
                        category = "remote",
                        icon = "🎮",
                        order = 5
                    )
                )
                dao.insertAll(defaultTools)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return INSTANCE ?: throw IllegalStateException("Database not initialized")
        }

        fun init(context: android.content.Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "xyrenn_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch {
                            // Insert default data
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}