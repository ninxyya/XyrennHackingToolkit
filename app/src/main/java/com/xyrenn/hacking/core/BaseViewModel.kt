package com.xyrenn.hacking.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected fun launchSafe(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    protected open fun handleError(e: Exception) {
        // Override in child ViewModels
    }
}