package com.xyrenn.hacking.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.history.models.AttackLog
import com.xyrenn.hacking.ui.history.models.LogFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {

    private val _historyLogs = MutableLiveData<List<AttackLog>>()
    val historyLogs: LiveData<List<AttackLog>> = _historyLogs

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val _selectedLog = MutableLiveData<AttackLog?>()
    val selectedLog: LiveData<AttackLog?> = _selectedLog

    private val allLogs = mutableListOf<AttackLog>()
    private var currentFilter = "All"

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            _status.value = "Loading history..."

            delay(1000)

            // Sample data
            allLogs.clear()
            allLogs.addAll(
                listOf(
                    AttackLog(
                        id = 1,
                        type = "WiFi Killer",
                        target = "Test WiFi",
                        status = "Success",
                        startTime = System.currentTimeMillis() - 3600000,
                        endTime = System.currentTimeMillis() - 3540000,
                        duration = 60,
                        details = "Deauth attack completed. 150 packets sent."
                    ),
                    AttackLog(
                        id = 2,
                        type = "SMS Spam",
                        target = "+628123456789",
                        status = "Success",
                        startTime = System.currentTimeMillis() - 7200000,
                        endTime = System.currentTimeMillis() - 7140000,
                        duration = 60,
                        details = "50 SMS messages sent successfully."
                    ),
                    AttackLog(
                        id = 3,
                        type = "DDoS",
                        target = "192.168.1.100",
                        status = "Partial",
                        startTime = System.currentTimeMillis() - 10800000,
                        endTime = System.currentTimeMillis() - 10740000,
                        duration = 60,
                        details = "Attack interrupted. 5000 packets sent."
                    ),
                    AttackLog(
                        id = 4,
                        type = "Remote Control",
                        target = "LG TV",
                        status = "Success",
                        startTime = System.currentTimeMillis() - 14400000,
                        endTime = System.currentTimeMillis() - 14340000,
                        duration = 60,
                        details = "Connected and sent power off command."
                    ),
                    AttackLog(
                        id = 5,
                        type = "Encryptor",
                        target = "file.txt",
                        status = "Success",
                        startTime = System.currentTimeMillis() - 18000000,
                        endTime = System.currentTimeMillis() - 17940000,
                        duration = 60,
                        details = "File encrypted with AES-256."
                    )
                )
            )

            applyFilter()
            _isLoading.value = false
        }
    }

    fun filterByType(type: String) {
        currentFilter = type
        applyFilter()
        _status.value = "Filtered by: $type"
    }

    private fun applyFilter() {
        val filtered = when (currentFilter) {
            "All" -> allLogs
            "WiFi Attacks" -> allLogs.filter { it.type.contains("WiFi") || it.type.contains("Deauth") }
            "Spam" -> allLogs.filter { it.type.contains("Spam") || it.type.contains("SMS") }
            "DDoS" -> allLogs.filter { it.type.contains("DDoS") || it.type.contains("Network") }
            "Remote" -> allLogs.filter { it.type.contains("Remote") || it.type.contains("TV") }
            "Crypto" -> allLogs.filter { it.type.contains("Encrypt") || it.type.contains("Hash") }
            else -> allLogs
        }
        _historyLogs.value = filtered
    }

    fun selectLog(log: AttackLog) {
        _selectedLog.value = log
    }

    fun clearHistory() {
        viewModelScope.launch {
            allLogs.clear()
            _historyLogs.value = emptyList()
            _status.value = "History cleared"
        }
    }
}