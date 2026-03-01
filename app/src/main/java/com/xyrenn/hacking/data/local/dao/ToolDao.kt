package com.xyrenn.hacking.data.local.dao

import androidx.room.*
import com.xyrenn.hacking.data.local.entities.ToolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Query("SELECT * FROM tools ORDER BY `order` ASC")
    fun getAllTools(): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE category = :category ORDER BY `order` ASC")
    fun getToolsByCategory(category: String): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE id = :id")
    fun getToolById(id: Int): Flow<ToolEntity?>

    @Query("SELECT * FROM tools WHERE name LIKE :query OR description LIKE :query")
    suspend fun searchTools(query: String): List<ToolEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTool(tool: ToolEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tools: List<ToolEntity>)

    @Update
    suspend fun updateTool(tool: ToolEntity)

    @Delete
    suspend fun deleteTool(tool: ToolEntity)

    @Query("DELETE FROM tools")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM tools")
    fun getToolsCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tools")
    suspend fun getToolsCount(): Int
}