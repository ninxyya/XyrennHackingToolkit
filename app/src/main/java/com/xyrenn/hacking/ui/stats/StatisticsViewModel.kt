package com.xyrenn.hacking.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.stats.models.StatData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor() : ViewModel() {

    private val _stats = MutableLiveData<StatData>()
    val stats: LiveData<StatData> = _stats

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000)

            val statData = StatData(
                totalAttacks = 157,
                successRate = 78,
                totalPackets = 15420,
                wifiAttacks = 45,
                spamAttacks = 32,
                networkAttacks = 28,
                remoteAttacks = 35,
                cryptoAttacks = 17,
                activeTime = "2h 45m"
            )

            _stats.value = statData
            _isLoading.value = false
        }
    }
}