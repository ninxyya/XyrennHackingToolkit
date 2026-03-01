package com.xyrenn.hacking.ui.tools.wifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.wifi.models.AccessPoint
import com.xyrenn.hacking.utils.helpers.WifiHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiKillerViewModel @Inject constructor(
    private val wifiHelper: WifiHelper
) : ViewModel() {

    private val _networks = MutableLiveData<List<AccessPoint>>()
    val networks: LiveData<List<AccessPoint>> = _networks

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    private val _attackStatus = MutableLiveData("Idle")
    val attackStatus: LiveData<String> = _attackStatus

    private var isAttacking = false

    fun scanNetworks() {
        viewModelScope.launch {
            _isScanning.value = true
            _attackStatus.value = "Scanning..."

            // Simulate scanning
            delay(2000)

            val scannedNetworks = listOf(
                AccessPoint("Test WiFi 1", "AA:BB:CC:DD:EE:FF", 1, -45, true),
                AccessPoint("Test WiFi 2", "AA:BB:CC:DD:EE:11", 6, -60, true),
                AccessPoint("Test WiFi 3", "AA:BB:CC:DD:EE:22", 11, -55, false)
            )

            _networks.value = scannedNetworks
            _isScanning.value = false
            _attackStatus.value = "Scan complete"
        }
    }

    fun startDeauthAttack(targetSsid: String) {
        viewModelScope.launch {
            isAttacking = true
            _attackStatus.value = "Attacking $targetSsid..."

            while (isAttacking) {
                // Simulate sending deauth packets
                wifiHelper.sendDeauthPacket()
                delay(100)
            }
        }
    }

    fun stopAttack() {
        isAttacking = false
        _attackStatus.value = "Attack stopped"
    }
}