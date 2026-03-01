package com.xyrenn.hacking.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.xyrenn.hacking.receivers.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class SchedulerService : Service() {

    @Inject
    lateinit var alarmManager: AlarmManager

    companion object {
        const val EXTRA_JOB_ID = "job_id"
        const val EXTRA_JOB_TYPE = "job_type"
        const val EXTRA_JOB_DATA = "job_data"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "SCHEDULE_JOB" -> scheduleJob(it)
                "CANCEL_JOB" -> cancelJob(it)
            }
        }
        return START_STICKY
    }

    private fun scheduleJob(intent: Intent) {
        val jobId = intent.getIntExtra(EXTRA_JOB_ID, 0)
        val jobType = intent.getStringExtra(EXTRA_JOB_TYPE) ?: return
        val jobData = intent.getStringExtra(EXTRA_JOB_DATA) ?: return
        val scheduleTime = intent.getLongExtra("schedule_time", 0)

        val alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_JOB_ID, jobId)
            putExtra(EXTRA_JOB_TYPE, jobType)
            putExtra(EXTRA_JOB_DATA, jobData)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            jobId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (scheduleTime > 0) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                scheduleTime,
                pendingIntent
            )
        } else {
            // Schedule for next execution
            val calendar = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 5) // Schedule 5 minutes from now
            }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent
            )
        }
    }

    private fun cancelJob(intent: Intent) {
        val jobId = intent.getIntExtra(EXTRA_JOB_ID, 0)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            jobId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}