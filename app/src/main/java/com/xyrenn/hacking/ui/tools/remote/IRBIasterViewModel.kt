package com.xyrenn.hacking.ui.tools.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.utils.helpers.IrHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IRBlasterViewModel @Inject constructor(
    private val irHelper: IrHelper
) : ViewModel() {

    private val _lastCommand = MutableLiveData("None")
    val lastCommand: LiveData<String> = _lastCommand

    private val _hasIrBlaster = MutableLiveData(false)
    val hasIrBlaster: LiveData<Boolean> = _hasIrBlaster

    init {
        _hasIrBlaster.value = irHelper.hasIrBlaster()
    }

    fun sendIrCommand(deviceType: String, command: String) {
        viewModelScope.launch {
            val success = irHelper.sendIrCommand(deviceType, command)
            if (success) {
                _lastCommand.value = "$deviceType: $command"
            } else {
                _lastCommand.value = "Failed: $deviceType: $command"
            }
        }
    }

    fun sendCustomIr(hexCode: String) {
        viewModelScope.launch {
            val success = irHelper.sendCustomIr(hexCode)
            if (success) {
                _lastCommand.value = "Custom IR sent"
            } else {
                _lastCommand.value = "Custom IR failed"
            }
        }
    }
}