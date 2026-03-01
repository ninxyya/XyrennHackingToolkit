package com.xyrenn.hacking.ui.tools.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.remote.models.RemoteDevice
import com.xyrenn.hacking.utils.helpers.RemoteHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteControlViewModel @Inject constructor(
    private val remoteHelper: RemoteHelper
) : ViewModel() {

    private val _devices = MutableLiveData<List<RemoteDevice>>()
    val devices: LiveData<List<RemoteDevice>> = _devices

    private val _isDiscovering = MutableLiveData(false)
    val isDiscovering: LiveData<Boolean> = _isDiscovering

    private val _connectionStatus = MutableLiveData("Disconnected")
    val connectionStatus: LiveData<String> = _connectionStatus

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected

    private val _selectedDevice = MutableLiveData<RemoteDevice?>()
    val selectedDevice: LiveData<RemoteDevice?> = _selectedDevice

    private val _commandOutput = MutableLiveData("")
    val commandOutput: LiveData<String> = _commandOutput

    fun discoverDevices() {
        viewModelScope.launch {
            _isDiscovering.value = true
            _connectionStatus.value = "Discovering devices..."
            _devices.value = emptyList()

            // Simulate device discovery
            delay(2000)

            val deviceList = listOf(
                RemoteDevice("TV-LG-01", "192.168.1.100", "LG TV", "TV", true),
                RemoteDevice("AC-Panasonic", "192.168.1.101", "Panasonic AC", "AC", true),
                RemoteDevice("Laptop-XPS", "192.168.1.102", "Dell XPS", "PC", true),
                RemoteDevice("Android-Phone", "192.168.1.103", "Samsung S23", "Phone", true)
            )

            _devices.value = deviceList
            _isDiscovering.value = false
            _connectionStatus.value = "Found ${deviceList.size} devices"
        }
    }

    fun selectDevice(device: RemoteDevice) {
        _selectedDevice.value = device
        _connectionStatus.value = "Selected: ${device.name}"
    }

    fun connectToSelectedDevice() {
        val device = _selectedDevice.value ?: return

        viewModelScope.launch {
            _connectionStatus.value = "Connecting to ${device.name}..."
            delay(1000)

            val success = remoteHelper.connectToDevice(device)
            if (success) {
                _isConnected.value = true
                _connectionStatus.value = "Connected to ${device.name}"
                _commandOutput.value = "Connected to ${device.name}\n"
            } else {
                _connectionStatus.value = "Connection failed"
            }
        }
    }

    fun disconnectDevice() {
        viewModelScope.launch {
            remoteHelper.disconnect()
            _isConnected.value = false
            _selectedDevice.value = null
            _connectionStatus.value = "Disconnected"
            _commandOutput.value = ""
        }
    }

    fun sendCommand(command: String) {
        val currentOutput = _commandOutput.value ?: ""
        _commandOutput.value = "$currentOutput> $command\n"

        viewModelScope.launch {
            val result = remoteHelper.sendCommand(command)
            _commandOutput.value = "${_commandOutput.value}$result\n"
        }
    }
}