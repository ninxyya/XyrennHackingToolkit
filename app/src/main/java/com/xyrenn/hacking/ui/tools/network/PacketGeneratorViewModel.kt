package com.xyrenn.hacking.ui.tools.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.utils.helpers.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PacketGeneratorViewModel @Inject constructor(
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _isGenerating = MutableLiveData(false)
    val isGenerating: LiveData<Boolean> = _isGenerating

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val _packetsGenerated = MutableLiveData(0)
    val packetsGenerated: LiveData<Int> = _packetsGenerated

    private var generateJob: kotlinx.coroutines.Job? = null

    fun generateUdpPackets(targetIp: String, port: Int, data: String, count: Int) {
        generateJob = viewModelScope.launch {
            _isGenerating.value = true
            _status.value = "Generating UDP packets to $targetIp:$port"
            var sentCount = 0

            for (i in 1..count) {
                if (!_isGenerating.value!!) break

                networkHelper.sendUdpPacket(targetIp, port, data)
                sentCount++
                _packetsGenerated.value = sentCount

                delay(100)
            }

            _isGenerating.value = false
            _status.value = "Generated $sentCount UDP packets"
        }
    }

    fun generateTcpPackets(targetIp: String, port: Int, data: String, count: Int) {
        generateJob = viewModelScope.launch {
            _isGenerating.value = true
            _status.value = "Generating TCP packets to $targetIp:$port"
            var sentCount = 0

            for (i in 1..count) {
                if (!_isGenerating.value!!) break

                networkHelper.sendTcpPacket(targetIp, port, data)
                sentCount++
                _packetsGenerated.value = sentCount

                delay(100)
            }

            _isGenerating.value = false
            _status.value = "Generated $sentCount TCP packets"
        }
    }

    fun stopGeneration() {
        generateJob?.cancel()
        _isGenerating.value = false
        _status.value = "Generation stopped"
    }
}