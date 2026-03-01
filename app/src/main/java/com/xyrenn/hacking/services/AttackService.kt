package com.xyrenn.hacking.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.xyrenn.hacking.R
import com.xyrenn.hacking.utils.helpers.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AttackService : Service() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isRunning = false
    private var currentJob: Job? = null

    companion object {
        const val ACTION_START_ATTACK = "com.xyrenn.hacking.START_ATTACK"
        const val ACTION_STOP_ATTACK = "com.xyrenn.hacking.STOP_ATTACK"
        const val EXTRA_ATTACK_TYPE = "attack_type"
        const val EXTRA_TARGET = "target"
        const val EXTRA_PARAMS = "params"
    }

    override fun onCreate() {
        super.onCreate()
        startForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_ATTACK -> {
                val attackType = intent.getStringExtra(EXTRA_ATTACK_TYPE) ?: "unknown"
                val target = intent.getStringExtra(EXTRA_TARGET) ?: ""
                val params = intent.getStringExtra(EXTRA_PARAMS) ?: ""
                startAttack(attackType, target, params)
            }
            ACTION_STOP_ATTACK -> {
                stopAttack()
            }
        }
        return START_STICKY
    }

    private fun startForeground() {
        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ATTACK)
            .setContentTitle("Attack Service")
            .setContentText("Attack service is running")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(NotificationHelper.NOTIFICATION_ID_ATTACK, notification)
    }

    private fun startAttack(attackType: String, target: String, params: String) {
        if (isRunning) return

        isRunning = true
        currentJob = serviceScope.launch {
            notificationHelper.showAttackNotification(attackType, target)

            when (attackType) {
                "deauth" -> executeDeauthAttack(target, params)
                "spam_sms" -> executeSmsSpam(target, params)
                "ddos" -> executeDdosAttack(target, params)
                "remote" -> executeRemoteControl(target, params)
                else -> executeGenericAttack(attackType, target, params)
            }

            stopSelf()
        }
    }

    private suspend fun executeDeauthAttack(target: String, params: String) {
        // Simulate deauth attack
        for (i in 1..100) {
            if (!isRunning) break
            delay(100)
            // Send deauth packets
        }
    }

    private suspend fun executeSmsSpam(target: String, params: String) {
        // Simulate SMS spam
        val count = params.toIntOrNull() ?: 10
        for (i in 1..count) {
            if (!isRunning) break
            delay(1000)
            // Send SMS
        }
    }

    private suspend fun executeDdosAttack(target: String, params: String) {
        // Simulate DDoS attack
        val duration = params.toIntOrNull() ?: 30
        val endTime = System.currentTimeMillis() + (duration * 1000)
        
        while (System.currentTimeMillis() < endTime && isRunning) {
            // Send packets
            delay(10)
        }
    }

    private suspend fun executeRemoteControl(target: String, params: String) {
        // Simulate remote control
        delay(5000)
    }

    private suspend fun executeGenericAttack(attackType: String, target: String, params: String) {
        // Generic attack simulation
        delay(10000)
    }

    private fun stopAttack() {
        isRunning = false
        currentJob?.cancel()
        notificationHelper.cancelAttackNotification()
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        currentJob?.cancel()
        serviceScope.cancel()
    }
}