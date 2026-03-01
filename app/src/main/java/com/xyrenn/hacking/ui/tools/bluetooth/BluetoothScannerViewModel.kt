package com.xyrenn.hacking.ui.tools.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.bluetooth.models.BluetoothDeviceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothScannerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _devices = MutableLiveData<List<BluetoothDeviceInfo>>()
    val devices: LiveData<List<BluetoothDeviceInfo>> = _devices

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    private val _isBluetoothEnabled = MutableLiveData(false)
    val isBluetoothEnabled: LiveData<Boolean> = _isBluetoothEnabled

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val deviceList = mutableListOf<BluetoothDeviceInfo>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()

                    device?.let {
                        if (!deviceList.any { d -> d.address == it.address }) {
                            val deviceInfo = BluetoothDeviceInfo(
                                name = it.name ?: "Unknown",
                                address = it.address,
                                rssi = rssi,
                                bondState = it.bondState
                            )
                            deviceList.add(deviceInfo)
                            _devices.value = deviceList.toList()
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    _isScanning.value = false
                    _status.value = "Scan complete. Found ${deviceList.size} devices"
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    _isBluetoothEnabled.value = state == BluetoothAdapter.STATE_ON
                }
            }
        }
    }

    init {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        }
        context.registerReceiver(receiver, filter)

        checkBluetoothState()
    }

    fun checkBluetoothState() {
        _isBluetoothEnabled.value = bluetoothAdapter?.isEnabled == true
    }

    fun startScan() {
        if (bluetoothAdapter?.isEnabled != true) {
            _status.value = "Bluetooth is disabled"
            return
        }

        viewModelScope.launch {
            deviceList.clear()
            _devices.value = emptyList()
            _isScanning.value = true
            _status.value = "Scanning for devices..."

            bluetoothAdapter?.startDiscovery()
        }
    }

    fun stopScan() {
        bluetoothAdapter?.cancelDiscovery()
        _isScanning.value = false
        _status.value = "Scan stopped"
    }

    fun selectDevice(device: BluetoothDeviceInfo) {
        _status.value = "Selected: ${device.name}"
    }

    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(receiver)
        bluetoothAdapter?.cancelDiscovery()
    }
}