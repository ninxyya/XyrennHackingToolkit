package com.xyrenn.hacking.data.repository

import com.xyrenn.hacking.data.local.dao.LogDao
import com.xyrenn.hacking.data.local.entities.LogEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository @Inject constructor(
    private val logDao: LogDao
) {

    fun getAllLogs(): Flow<List<LogEntity>> {
        return logDao.getAllLogs()
    }

    fun getLogsByType(type: String): Flow<List<LogEntity>> {
        return logDao.getLogsByType(type)
    }

    fun getLogsByDateRange(startTime: Long, endTime: Long): Flow<List<LogEntity>> {
        return logDao.getLogsByDateRange(startTime, endTime)
    }

    suspend fun insertLog(log: LogEntity) {
        logDao.insertLog(log)
    }

    suspend fun deleteLog(log: LogEntity) {
        logDao.deleteLog(log)
    }

    suspend fun deleteAllLogs() {
        logDao.deleteAllLogs()
    }

    suspend fun getLogsCount(): Int {
        return logDao.getLogsCount()
    }

    suspend fun getLogsByStatus(status: String): List<LogEntity> {
        return logDao.getLogsByStatus(status)
    }
}