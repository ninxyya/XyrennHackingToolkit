package com.xyrenn.hacking.utils.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun pingHost(host: String): Boolean {
        return try {
            val inet = InetAddress.getByName(host)
            inet.isReachable(1000)
        } catch (e: Exception) {
            false
        }
    }

    fun isPortOpen(host: String, port: Int, timeout: Int = 1000): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(host, port), timeout)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getHostname(ip: String): String? {
        return try {
            InetAddress.getByName(ip).hostName
        } catch (e: Exception) {
            null
        }
    }

    fun getMacAddress(ip: String): String {
        // This is a simplified version - actual MAC address retrieval requires ARP table
        return "00:11:22:33:44:55"
    }

    fun getVendorByMac(ip: String): String {
        // Simplified vendor detection
        return "Unknown"
    }

    fun sendPacket(targetIp: String, port: Int): Boolean {
        // Simulate sending packet
        return true
    }

    fun sendUdpPacket(targetIp: String, port: Int, data: String): Boolean {
        // Simulate sending UDP packet
        return true
    }

    fun sendTcpPacket(targetIp: String, port: Int, data: String): Boolean {
        // Simulate sending TCP packet
        return true
    }
}