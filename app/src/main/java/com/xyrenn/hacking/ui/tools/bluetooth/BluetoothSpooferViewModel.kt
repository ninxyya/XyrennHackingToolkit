package com.xyrenn.hacking.ui.tools.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.os.ParcelUuid
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.bluetooth.models.SpoofedDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BluetoothSpooferViewModel @Inject constructor() : ViewModel() {

    private val _spoofedDevice = MutableLiveData<SpoofedDevice>()
    val spoofedDevice: LiveData<SpoofedDevice> = _spoofedDevice

    private val _isAdvertising = MutableLiveData(false)
    val isAdvertising: LiveData<Boolean> = _isAdvertising

    private val _isBluetoothEnabled = MutableLiveData(false)
    val isBluetoothEnabled: LiveData<Boolean> = _isBluetoothEnabled

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    private var currentAdvertiseCallback: AdvertiseCallback? = null

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
            _isAdvertising.postValue(true)
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            _isAdvertising.postValue(false)
        }
    }

    init {
        checkBluetoothState()
    }

    fun checkBluetoothState() {
        _isBluetoothEnabled.value = bluetoothAdapter?.isEnabled == true
    }

    fun generateSpoofedDevice(name: String, type: String) {
        viewModelScope.launch {
            val spoofed = SpoofedDevice(
                name = name,
                type = type,
                address = generateRandomMac(),
                uuid = UUID.randomUUID().toString()
            )
            _spoofedDevice.value = spoofed
        }
    }

    fun startAdvertising() {
        val device = _spoofedDevice.value ?: return

        bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(true)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .setIncludeTxPowerLevel(true)
            .addServiceUuid(ParcelUuid.fromString(device.uuid))
            .build()

        bluetoothLeAdvertiser?.startAdvertising(settings, data, advertiseCallback)
        currentAdvertiseCallback = advertiseCallback
    }

    fun stopAdvertising() {
        bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
        currentAdvertiseCallback = null
        _isAdvertising.value = false
    }

    private fun generateRandomMac(): String {
        val mac = StringBuilder()
        for (i in 0..5) {
            mac.append(String.format("%02X", (Math.random() * 256).toInt()))
            if (i < 5) mac.append(":")
        }
        return mac.toString()
    }

    override fun onCleared() {
        super.onCleared()
        stopAdvertising()
    }
}