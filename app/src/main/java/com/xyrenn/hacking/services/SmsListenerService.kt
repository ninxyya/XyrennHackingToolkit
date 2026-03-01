package com.xyrenn.hacking.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telephony.SmsMessage
import com.xyrenn.hacking.data.local.dao.LogDao
import com.xyrenn.hacking.data.local.entities.LogEntity
import com.xyrenn.hacking.receivers.SmsReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SmsListenerService : Service() {

    @Inject
    lateinit var logDao: LogDao

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.action == SmsReceiver.ACTION_SMS_RECEIVED) {
                val messages = it.getSerializableExtra(SmsReceiver.EXTRA_SMS_MESSAGES) as? Array<SmsMessage>
                messages?.let { msgs ->
                    processSmsMessages(msgs)
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun processSmsMessages(messages: Array<SmsMessage>) {
        serviceScope.launch {
            for (message in messages) {
                val sender = message.originatingAddress ?: "Unknown"
                val body = message.messageBody

                // Log received SMS
                val log = LogEntity(
                    type = "SMS_RECEIVED",
                    target = sender,
                    status = "received",
                    startTime = System.currentTimeMillis(),
                    endTime = System.currentTimeMillis(),
                    duration = 0,
                    packets = 0,
                    details = body
                )
                logDao.insertLog(log)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}