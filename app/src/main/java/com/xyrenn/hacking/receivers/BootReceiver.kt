package com.xyrenn.hacking.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.xyrenn.hacking.data.local.preferences.PreferenceManager
import com.xyrenn.hacking.services.AutoAttackService
import com.xyrenn.hacking.services.NetworkMonitorService
import com.xyrenn.hacking.services.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            startServicesOnBoot(context)
        }
    }

    private fun startServicesOnBoot(context: Context) {
        if (preferenceManager.isAutoStart()) {
            // Start Notification Service
            if (preferenceManager.areNotificationsEnabled()) {
                startService(context, NotificationService::class.java)
            }

            // Start Network Monitor
            startService(context, NetworkMonitorService::class.java)

            // Start Auto Attack Scheduler
            startService(context, AutoAttackService::class.java)
        }
    }

    private fun startService(context: Context, serviceClass: Class<*>) {
        val intent = Intent(context, serviceClass)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}