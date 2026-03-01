package com.xyrenn.hacking.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.xyrenn.hacking.utils.helpers.NotificationHelper
import com.xyrenn.hacking.utils.helpers.SmsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var smsHelper: SmsHelper

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val phoneNumber = intent.getStringExtra("phone_number") ?: return
        val message = intent.getStringExtra("message") ?: return
        val count = intent.getIntExtra("count", 1)

        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..count) {
                smsHelper.sendSms(phoneNumber, message)
                Thread.sleep(1000)
            }
        }

        notificationHelper.showNotification(
            context,
            "Spam Schedule Executed",
            "Sent $count messages to $phoneNumber"
        )

        Toast.makeText(context, "Spam schedule executed", Toast.LENGTH_SHORT).show()
    }
}