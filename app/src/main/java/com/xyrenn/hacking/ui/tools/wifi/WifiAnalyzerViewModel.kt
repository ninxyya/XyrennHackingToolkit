package com.xyrenn.hacking.ui.tools.wifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.wifi.models.ChannelAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiAnalyzerViewModel @Inject constructor() : ViewModel() {

    private val _channelAnalysis = MutableLiveData<ChannelAnalysis>()
    val channelAnalysis: LiveData<ChannelAnalysis> = _channelAnalysis

    private val _isAnalyzing = MutableLiveData(false)
    val isAnalyzing: LiveData<Boolean> = _isAnalyzing

    fun analyzeChannels() {
        viewModelScope.launch {
            _isAnalyzing.value = true

            // Simulate analysis
            delay(2000)

            val analysis = ChannelAnalysis(
                channel1 = 3,
                channel6 = 7,
                channel11 = 2,
                channelOthers = 5
            )

            _channelAnalysis.value = analysis
            _isAnalyzing.value = false
        }
    }
}