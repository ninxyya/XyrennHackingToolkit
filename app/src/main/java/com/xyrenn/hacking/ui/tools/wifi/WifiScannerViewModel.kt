package com.xyrenn.hacking.ui.tools.wifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.wifi.models.AccessPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiScannerViewModel @Inject constructor() : ViewModel() {

    private val _networks = MutableLiveData<List<AccessPoint>>()
    val networks: LiveData<List<AccessPoint>> = _networks

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    fun startScan() {
        viewModelScope.launch {
            _isScanning.value = true
            _networks.value = emptyList()

            // Simulate scanning
            delay(3000)

            val scannedNetworks = listOf(
                AccessPoint("WiFi Rumah", "AA:BB:CC:DD:EE:01", 1, -45, true),
                AccessPoint("WiFi Kantor", "AA:BB:CC:DD:EE:02", 6, -60, true),
                AccessPoint("WiFi Tetangga", "AA:BB:CC:DD:EE:03", 11, -55, true),
                AccessPoint("Public WiFi", "AA:BB:CC:DD:EE:04", 3, -70, false),
                AccessPoint("IndiHome", "AA:BB:CC:DD:EE:05", 8, -50, true),
                AccessPoint("First Media", "AA:BB:CC:DD:EE:06", 13, -65, true),
                AccessPoint("Biznet", "AA:BB:CC:DD:EE:07", 2, -48, true),
                AccessPoint("MyRepublic", "AA:BB:CC:DD:EE:08", 4, -72, true)
            )

            _networks.value = scannedNetworks
            _isScanning.value = false
        }
    }

    fun stopScan() {
        _isScanning.value = false
    }
}