package com.xyrenn.hacking.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xyrenn.hacking.services.AttackService
import com.xyrenn.hacking.utils.helpers.NotificationHelper

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_ATTACK_STOP = "ACTION_ATTACK_STOP"
        const val ACTION_ATTACK_RESTART = "ACTION_ATTACK_RESTART"
        const val ACTION_SNOOZE = "ACTION_SNOOZE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_ATTACK_STOP -> {
                stopAttack(context)
            }
            ACTION_ATTACK_RESTART -> {
                restartAttack(context, intent)
            }
            ACTION_SNOOZE -> {
                snoozeNotification(context)
            }
        }
    }

    private fun stopAttack(context: Context) {
        val intent = Intent(context, AttackService::class.java).apply {
            action = AttackService.ACTION_STOP_ATTACK
        }
        context.startService(intent)
    }

    private fun restartAttack(context: Context, intent: Intent) {
        val attackType = intent.getStringExtra(AttackService.EXTRA_ATTACK_TYPE) ?: return
        val target = intent.getStringExtra(AttackService.EXTRA_TARGET) ?: return
        val params = intent.getStringExtra(AttackService.EXTRA_PARAMS) ?: return

        val serviceIntent = Intent(context, AttackService::class.java).apply {
            action = AttackService.ACTION_START_ATTACK
            putExtra(AttackService.EXTRA_ATTACK_TYPE, attackType)
            putExtra(AttackService.EXTRA_TARGET, target)
            putExtra(AttackService.EXTRA_PARAMS, params)
        }
        context.startService(serviceIntent)
    }

    private fun snoozeNotification(context: Context) {
        // Cancel current notification
        val notificationHelper = NotificationHelper.getInstance(context)
        notificationHelper.cancelNotification(NotificationHelper.NOTIFICATION_ID_ATTACK)
    }
}