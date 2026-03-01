package com.xyrenn.hacking.utils.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val smsManager = SmsManager.getDefault()

    fun hasSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun sendSms(phoneNumber: String, message: String): Boolean {
        return try {
            if (hasSmsPermission()) {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun sendMultipartSms(phoneNumber: String, message: String): Boolean {
        return try {
            if (hasSmsPermission()) {
                val parts = smsManager.divideMessage(message)
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}