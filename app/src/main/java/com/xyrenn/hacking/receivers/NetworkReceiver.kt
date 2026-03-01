package com.xyrenn.hacking.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.xyrenn.hacking.utils.helpers.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NetworkReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ConnectivityManager.CONNECTIVITY_ACTION -> {
                handleConnectivityChange(context)
            }
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                handleWifiStateChange(context, intent)
            }
            WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                handleNetworkStateChange(context, intent)
            }
        }
    }

    private fun handleConnectivityChange(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            val type = when (activeNetwork?.type) {
                ConnectivityManager.TYPE_WIFI -> "WiFi"
                ConnectivityManager.TYPE_MOBILE -> "Mobile Data"
                else -> "Network"
            }
            notificationHelper.showNotification(
                context,
                "Network Connected",
                "Connected to $type"
            )
        } else {
            notificationHelper.showNotification(
                context,
                "Network Disconnected",
                "No network connection"
            )
        }
    }

    private fun handleWifiStateChange(context: Context, intent: Intent) {
        val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
        when (state) {
            WifiManager.WIFI_STATE_ENABLED -> {
                notificationHelper.showNotification(context, "WiFi Enabled", "WiFi is now on")
            }
            WifiManager.WIFI_STATE_DISABLED -> {
                notificationHelper.showNotification(context, "WiFi Disabled", "WiFi is now off")
            }
        }
    }

    private fun handleNetworkStateChange(context: Context, intent: Intent) {
        val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        if (networkInfo?.isConnected == true) {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val connectionInfo = wifiManager.connectionInfo
            notificationHelper.showNotification(
                context,
                "Connected to WiFi",
                "SSID: ${connectionInfo.ssid}"
            )
        }
    }
}