package com.xyrenn.hacking.utils.helpers

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import com.xyrenn.hacking.ui.tools.remote.models.RemoteDevice
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var currentSocket: Socket? = null
    private var currentConnection: HttpURLConnection? = null

    fun getLocalIpAddress(): String {
        return try {
            val wifiInfo = wifiManager.connectionInfo
            val ipInt = wifiInfo.ipAddress
            "${ipInt and 0xFF}.${ipInt shr 8 and 0xFF}.${ipInt shr 16 and 0xFF}.${ipInt shr 24 and 0xFF}"
        } catch (e: Exception) {
            "192.168.1.1"
        }
    }

    fun getBroadcastAddress(): String {
        val ip = getLocalIpAddress().split(".")
        return "${ip[0]}.${ip[1]}.${ip[2]}.255"
    }

    fun isReachable(ip: String, timeout: Int = 500): Boolean {
        return try {
            val address = InetAddress.getByName(ip)
            address.isReachable(timeout)
        } catch (e: Exception) {
            false
        }
    }

    fun connectToDevice(device: RemoteDevice): Boolean {
        return try {
            // Simulate connection
            Thread.sleep(500)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun sendTvCommand(ip: String, command: String): Boolean {
        return try {
            // Simulate sending command to TV
            val url = URL("http://$ip:8080/remote")
            currentConnection = url.openConnection() as HttpURLConnection
            currentConnection?.requestMethod = "POST"
            currentConnection?.doOutput = true
            currentConnection?.setRequestProperty("Content-Type", "application/json")

            val outputStream = DataOutputStream(currentConnection?.outputStream)
            outputStream.writeBytes("{\"command\":\"$command\"}")
            outputStream.flush()
            outputStream.close()

            val responseCode = currentConnection?.responseCode
            currentConnection?.disconnect()

            responseCode == 200
        } catch (e: Exception) {
            false
        }
    }

    fun sendCommand(command: String): String {
        return try {
            // Simulate command execution
            when (command.lowercase()) {
                "help" -> "Available commands: help, ls, pwd, whoami"
                "ls" -> "Documents\nDownloads\nPictures\nVideos"
                "pwd" -> "/home/user"
                "whoami" -> "user@device"
                else -> "Command not recognized: $command"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun disconnect() {
        try {
            currentSocket?.close()
            currentConnection?.disconnect()
        } catch (e: Exception) {
            // Ignore
        }
        currentSocket = null
        currentConnection = null
    }
}