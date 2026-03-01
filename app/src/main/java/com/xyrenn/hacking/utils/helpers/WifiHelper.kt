package com.xyrenn.hacking.utils.helpers

import android.content.Context
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun isWifiEnabled(): Boolean {
        return wifiManager.isWifiEnabled
    }

    fun enableWifi() {
        wifiManager.isWifiEnabled = true
    }

    fun disableWifi() {
        wifiManager.isWifiEnabled = false
    }

    fun sendDeauthPacket() {
        // This is a simulation - actual deauth requires root or specific hardware
        // In real implementation, this would use native code or root access
    }

    fun getCurrentNetworkInfo(): String {
        val connectionInfo = wifiManager.connectionInfo
        return "Connected to: ${connectionInfo.ssid} (${connectionInfo.rssi} dBm)"
    }
}