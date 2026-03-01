package com.xyrenn.hacking.data.local.dao

import androidx.room.*
import com.xyrenn.hacking.data.local.entities.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Query("SELECT * FROM logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE type = :type ORDER BY timestamp DESC")
    fun getLogsByType(type: String): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getLogsByDateRange(startTime: Long, endTime: Long): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE status = :status ORDER BY timestamp DESC")
    suspend fun getLogsByStatus(status: String): List<LogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<LogEntity>)

    @Update
    suspend fun updateLog(log: LogEntity)

    @Delete
    suspend fun deleteLog(log: LogEntity)

    @Query("DELETE FROM logs")
    suspend fun deleteAllLogs()

    @Query("SELECT COUNT(*) FROM logs")
    fun getLogsCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM logs")
    suspend fun getLogsCount(): Int

    @Query("SELECT COUNT(*) FROM logs WHERE status = 'success'")
    suspend fun getSuccessCount(): Int

    @Query("SELECT CAST((SELECT COUNT(*) FROM logs WHERE status = 'success') AS REAL) / COUNT(*) * 100 FROM logs")
    fun getSuccessRateFlow(): Flow<Int>

    @Query("SELECT SUM(packets) FROM logs")
    fun getTotalPacketsFlow(): Flow<Int>

    @Query("SELECT type, COUNT(*) as count FROM logs GROUP BY type")
    suspend fun getAttackStatsByType(): Map<String, Int>

    @Query("SELECT date(timestamp/1000, 'unixepoch') as date, COUNT(*) as count FROM logs WHERE timestamp > :days * 86400000 GROUP BY date ORDER BY date DESC")
    suspend fun getDailyStats(days: Int): List<DailyStat>

    data class DailyStat(
        val date: String,
        val count: Int
    )
}