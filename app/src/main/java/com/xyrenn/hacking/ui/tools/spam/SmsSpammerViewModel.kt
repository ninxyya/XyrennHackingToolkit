package com.xyrenn.hacking.ui.tools.spam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.utils.helpers.SmsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsSpammerViewModel @Inject constructor(
    private val smsHelper: SmsHelper
) : ViewModel() {

    private val _isSending = MutableLiveData(false)
    val isSending: LiveData<Boolean> = _isSending

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val _sentCount = MutableLiveData(0)
    val sentCount: LiveData<Int> = _sentCount

    private var currentJob: kotlinx.coroutines.Job? = null

    fun sendSms(phoneNumber: String, message: String, count: Int) {
        currentJob = viewModelScope.launch {
            _isSending.value = true
            _status.value = "Sending SMS..."
            _sentCount.value = 0

            for (i in 1..count) {
                if (!_isSending.value!!) break

                val success = smsHelper.sendSms(phoneNumber, message)
                if (success) {
                    _sentCount.value = i
                    _status.value = "Sent $i of $count"
                } else {
                    _status.value = "Failed to send SMS"
                    break
                }
                delay(1000) // Delay between messages
            }

            _isSending.value = false
            _status.value = if (_sentCount.value == count) "Completed" else "Stopped"
        }
    }

    fun stopSending() {
        currentJob?.cancel()
        _isSending.value = false
        _status.value = "Stopped"
    }
}