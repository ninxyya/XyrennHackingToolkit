package com.xyrenn.hacking.ui.tools.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.utils.helpers.IrHelper
import com.xyrenn.hacking.utils.helpers.RemoteHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvControlViewModel @Inject constructor(
    private val remoteHelper: RemoteHelper,
    private val irHelper: IrHelper
) : ViewModel() {

    private val _tvsFound = MutableLiveData<List<String>>()
    val tvsFound: LiveData<List<String>> = _tvsFound

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    private val _connectionStatus = MutableLiveData("Not connected")
    val connectionStatus: LiveData<String> = _connectionStatus

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected

    private val _lastCommand = MutableLiveData("None")
    val lastCommand: LiveData<String> = _lastCommand

    private var currentTvIp: String? = null

    fun scanForTvs() {
        viewModelScope.launch {
            _isScanning.value = true
            _connectionStatus.value = "Scanning for TVs..."
            _tvsFound.value = emptyList()

            // Simulate scanning
            delay(3000)

            val tvs = listOf(
                "192.168.1.100 (LG Smart TV)",
                "192.168.1.101 (Samsung TV)",
                "192.168.1.102 (Sony Bravia)"
            )

            _tvsFound.value = tvs
            _isScanning.value = false
            _connectionStatus.value = "Found ${tvs.size} TVs"
        }
    }

    fun connectToTv(ip: String) {
        viewModelScope.launch {
            _connectionStatus.value = "Connecting to $ip..."
            currentTvIp = ip
            delay(1500)

            // Simulate connection
            val success = true

            if (success) {
                _isConnected.value = true
                _connectionStatus.value = "Connected to TV at $ip"
            } else {
                _connectionStatus.value = "Connection failed"
            }
        }
    }

    fun sendTvCommand(command: String) {
        if (_isConnected.value != true) {
            _connectionStatus.value = "Not connected"
            return
        }

        _lastCommand.value = command

        viewModelScope.launch {
            when (command) {
                "power", "volume_up", "volume_down", "mute", "channel_up", "channel_down" -> {
                    // Try IR first, then network
                    val irSuccess = irHelper.sendIrCommand("tv", command)
                    if (!irSuccess) {
                        remoteHelper.sendTvCommand(currentTvIp ?: "", command)
                    }
                }
                "netflix", "youtube", "prime" -> {
                    // Send via network
                    remoteHelper.sendTvCommand(currentTvIp ?: "", command)
                }
                else -> {
                    // Regular commands
                    remoteHelper.sendTvCommand(currentTvIp ?: "", command)
                }
            }
        }
    }

    fun disconnectTv() {
        viewModelScope.launch {
            remoteHelper.disconnect()
            _isConnected.value = false
            currentTvIp = null
            _connectionStatus.value = "Disconnected"
            _lastCommand.value = "None"
        }
    }
}