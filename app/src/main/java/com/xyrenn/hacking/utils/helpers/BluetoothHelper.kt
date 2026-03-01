package com.xyrenn.hacking.utils.helpers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // SPP UUID

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    fun getPairedDevices(): List<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    fun connectToDevice(device: BluetoothDevice): BluetoothSocket? {
        return try {
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()
            socket
        } catch (e: IOException) {
            null
        }
    }

    fun sendData(socket: BluetoothSocket, data: String): Boolean {
        return try {
            socket.outputStream.write(data.toByteArray())
            true
        } catch (e: IOException) {
            false
        }
    }

    fun receiveData(socket: BluetoothSocket): String? {
        return try {
            val buffer = ByteArray(1024)
            val bytes = socket.inputStream.read(buffer)
            String(buffer, 0, bytes)
        } catch (e: IOException) {
            null
        }
    }

    fun getDeviceClass(device: BluetoothDevice): String {
        return when (device.bluetoothClass?.deviceClass) {
            android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES -> "Headphones"
            android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO -> "Speaker"
            android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> "Car Audio"
            android.bluetooth.BluetoothClass.Device.COMPUTER_LAPTOP -> "Laptop"
            android.bluetooth.BluetoothClass.Device.COMPUTER_DESKTOP -> "Desktop"
            android.bluetooth.BluetoothClass.Device.PHONE_SMART -> "Smartphone"
            android.bluetooth.BluetoothClass.Device.PHONE_CELLULAR -> "Phone"
            android.bluetooth.BluetoothClass.Device.WEARABLE_WRIST_WATCH -> "Smart Watch"
            android.bluetooth.BluetoothClass.Device.WEARABLE_HEADSET -> "Headset"
            else -> "Unknown"
        }
    }
}