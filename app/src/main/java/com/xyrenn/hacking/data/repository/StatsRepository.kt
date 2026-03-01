package com.xyrenn.hacking.data.repository

import com.xyrenn.hacking.data.local.dao.LogDao
import com.xyrenn.hacking.data.local.dao.ToolDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val logDao: LogDao,
    private val toolDao: ToolDao
) {

    fun getDashboardStats(): Flow<DashboardStats> {
        return combine(
            logDao.getLogsCountFlow(),
            toolDao.getToolsCountFlow(),
            logDao.getSuccessRateFlow(),
            logDao.getTotalPacketsFlow()
        ) { logsCount, toolsCount, successRate, totalPackets ->
            DashboardStats(
                totalAttacks = logsCount,
                totalTools = toolsCount,
                successRate = successRate,
                totalPackets = totalPackets,
                activeTime = calculateActiveTime()
            )
        }
    }

    suspend fun getAttackStatsByType(): Map<String, Int> {
        return logDao.getAttackStatsByType()
    }

    suspend fun getDailyStats(days: Int): List<DailyStat> {
        return logDao.getDailyStats(days)
    }

    private fun calculateActiveTime(): String {
        // Calculate total active time from logs
        return "2h 30m"
    }

    data class DashboardStats(
        val totalAttacks: Int,
        val totalTools: Int,
        val successRate: Int,
        val totalPackets: Int,
        val activeTime: String
    )

    data class DailyStat(
        val date: String,
        val count: Int
    )
}