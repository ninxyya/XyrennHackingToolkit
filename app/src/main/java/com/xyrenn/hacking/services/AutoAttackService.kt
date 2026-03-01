package com.xyrenn.hacking.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.xyrenn.hacking.data.local.dao.LogDao
import com.xyrenn.hacking.data.local.preferences.PreferenceManager
import com.xyrenn.hacking.utils.helpers.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AutoAttackService : Service() {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var logDao: LogDao

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var scheduledAttacks = mutableListOf<ScheduledAttack>()

    data class ScheduledAttack(
        val id: String,
        val type: String,
        val target: String,
        val scheduleTime: Long,
        val params: String,
        var isExecuted: Boolean = false
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startScheduler()
        return START_STICKY
    }

    private fun startScheduler() {
        serviceScope.launch {
            while (true) {
                checkScheduledAttacks()
                delay(60000) // Check every minute
            }
        }
    }

    private suspend fun checkScheduledAttacks() {
        val currentTime = System.currentTimeMillis()
        val attacksToExecute = scheduledAttacks.filter {
            !it.isExecuted && it.scheduleTime <= currentTime
        }

        attacksToExecute.forEach { attack ->
            executeScheduledAttack(attack)
            attack.isExecuted = true
        }
    }

    private suspend fun executeScheduledAttack(attack: ScheduledAttack) {
        notificationHelper.showNotification(
            this,
            "Auto Attack",
            "Executing ${attack.type} on ${attack.target}"
        )

        // Start attack service
        val intent = Intent(this, AttackService::class.java).apply {
            action = AttackService.ACTION_START_ATTACK
            putExtra(AttackService.EXTRA_ATTACK_TYPE, attack.type)
            putExtra(AttackService.EXTRA_TARGET, attack.target)
            putExtra(AttackService.EXTRA_PARAMS, attack.params)
        }
        ContextCompat.startForegroundService(this, intent)
    }

    fun scheduleAttack(attack: ScheduledAttack) {
        scheduledAttacks.add(attack)
    }

    fun cancelScheduledAttack(attackId: String) {
        scheduledAttacks.removeAll { it.id == attackId }
    }

    fun cancelAllScheduled() {
        scheduledAttacks.clear()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
} 