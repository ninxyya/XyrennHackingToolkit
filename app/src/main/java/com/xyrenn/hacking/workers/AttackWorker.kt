package com.xyrenn.hacking.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xyrenn.hacking.data.local.dao.LogDao
import com.xyrenn.hacking.data.local.entities.LogEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

class AttackWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val logDao: LogDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val attackType = params.inputData.getString("attack_type") ?: return Result.failure()
        val target = params.inputData.getString("target") ?: return Result.failure()
        val duration = params.inputData.getInt("duration", 30)

        return try {
            // Execute attack
            val startTime = System.currentTimeMillis()
            executeAttack(attackType, target, duration)
            val endTime = System.currentTimeMillis()

            // Log the attack
            val log = LogEntity(
                type = attackType,
                target = target,
                status = "success",
                startTime = startTime,
                endTime = endTime,
                duration = ((endTime - startTime) / 1000).toInt(),
                packets = duration * 100,
                details = "Attack completed successfully"
            )
            logDao.insertLog(log)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun executeAttack(attackType: String, target: String, duration: Int) {
        val endTime = System.currentTimeMillis() + (duration * 1000)
        while (System.currentTimeMillis() < endTime) {
            delay(100)
        }
    }
}