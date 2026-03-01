package com.xyrenn.hacking.services

import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.xyrenn.hacking.utils.helpers.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NetworkMonitorService : Service() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private lateinit var connectivityManager: ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            notifyNetworkAvailable()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            notifyNetworkLost()
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            checkWifiCapabilities(networkCapabilities)
        }
    }

    override fun onCreate() {
        super.onCreate()
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        startMonitoring()
    }

    private fun startMonitoring() {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun notifyNetworkAvailable() {
        notificationHelper.showNotification(
            this,
            "Network Available",
            "Network connection established"
        )
    }

    private fun notifyNetworkLost() {
        notificationHelper.showNotification(
            this,
            "Network Lost",
            "Network connection lost"
        )
    }

    private fun checkWifiCapabilities(capabilities: NetworkCapabilities) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            // Check WiFi capabilities
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}