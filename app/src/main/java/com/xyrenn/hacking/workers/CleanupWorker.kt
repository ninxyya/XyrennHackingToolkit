package com.xyrenn.hacking.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xyrenn.hacking.data.local.dao.LogDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class CleanupWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val logDao: LogDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Delete logs older than 30 days
            val cutoffTime = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)
            // Perform cleanup
            // logDao.deleteOldLogs(cutoffTime)

            // Clean up temporary files
            cleanupTempFiles()

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun cleanupTempFiles() {
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.forEach { file ->
            if (file.isFile && file.name.startsWith("temp_")) {
                file.delete()
            }
        }
    }
}