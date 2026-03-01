package com.xyrenn.hacking.ui.tools.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.network.models.NetworkDevice
import com.xyrenn.hacking.utils.helpers.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkScannerViewModel @Inject constructor(
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _devices = MutableLiveData<List<NetworkDevice>>()
    val devices: LiveData<List<NetworkDevice>> = _devices

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    fun scanNetwork(subnet: String) {
        viewModelScope.launch {
            _isScanning.value = true
            _status.value = "Scanning $subnet..."
            _devices.value = emptyList()

            val deviceList = mutableListOf<NetworkDevice>()

            // Simulate scanning IP range
            for (i in 1..254) {
                if (!_isScanning.value!!) break

                val ip = "192.168.1.$i"
                val isAlive = networkHelper.pingHost(ip)

                if (isAlive) {
                    val device = NetworkDevice(
                        ip = ip,
                        mac = networkHelper.getMacAddress(ip),
                        hostname = networkHelper.getHostname(ip) ?: "Unknown",
                        vendor = networkHelper.getVendorByMac(ip),
                        openPorts = listOf(80, 443, 22).filter { networkHelper.isPortOpen(ip, it) }
                    )
                    deviceList.add(device)
                    _devices.value = deviceList.toList()
                }

                _status.value = "Scanning... ${(i/2.54).toInt()}%"
                delay(50)
            }

            _isScanning.value = false
            _status.value = "Scan complete. Found ${deviceList.size} devices"
        }
    }

    fun stopScan() {
        _isScanning.value = false
        _status.value = "Scan stopped"
    }
}