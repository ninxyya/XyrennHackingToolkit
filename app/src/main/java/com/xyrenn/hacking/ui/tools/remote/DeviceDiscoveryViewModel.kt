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
class DeviceDiscoveryViewModel @Inject constructor(
    private val remoteHelper: RemoteHelper
) : ViewModel() {

    private val _devices = MutableLiveData<List<RemoteDevice>>()
    val devices: LiveData<List<RemoteDevice>> = _devices

    private val _isDiscovering = MutableLiveData(false)
    val isDiscovering: LiveData<Boolean> = _isDiscovering

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    fun startDiscovery() {
        viewModelScope.launch {
            _isDiscovering.value = true
            _status.value = "Discovering devices..."
            _devices.value = emptyList()

            // Simulate discovering different types of devices
            val deviceTypes = listOf("TV", "AC", "Lamp", "Speaker", "Camera", "Thermostat", "Fridge", "Doorbell")
            val brands = listOf("Samsung", "LG", "Sony", "Panasonic", "Philips", "Xiaomi", "Google", "Amazon")

            val deviceList = mutableListOf<RemoteDevice>()

            for (i in 1..12) {
                if (!_isDiscovering.value!!) break

                val type = deviceTypes.random()
                val brand = brands.random()
                val device = RemoteDevice(
                    id = "dev-$i",
                    ip = "192.168.1.${100 + i}",
                    name = "$brand $type",
                    type = type,
                    isAvailable = true
                )
                deviceList.add(device)
                _devices.value = deviceList.toList()

                _status.value = "Found ${deviceList.size} devices..."
                delay(500)
            }

            _isDiscovering.value = false
            _status.value = "Discovery complete. Found ${deviceList.size} devices"
        }
    }

    fun stopDiscovery() {
        _isDiscovering.value = false
        _status.value = "Discovery stopped"
    }

    fun selectDevice(device: RemoteDevice) {
        // Handle device selection
    }
}