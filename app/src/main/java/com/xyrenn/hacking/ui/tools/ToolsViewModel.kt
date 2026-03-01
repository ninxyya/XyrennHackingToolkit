package com.xyrenn.hacking.ui.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.data.models.Tool
import com.xyrenn.hacking.data.repository.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolsViewModel @Inject constructor(
    private val repository: ToolRepository
) : ViewModel() {
    
    private val _tools = MutableLiveData<List<Tool>>()
    val tools: LiveData<List<Tool>> = _tools
    
    init {
        loadTools()
    }
    
    private fun loadTools() {
        viewModelScope.launch {
            val toolList = listOf(
                Tool(1, "WiFi Killer", "Kick devices from WiFi", "wifi", true),
                Tool(2, "Deauther", "Deauth attack", "wifi", true),
                Tool(3, "SMS Spammer", "Spam SMS", "spam", true),
                Tool(4, "WA Spammer", "Spam WhatsApp", "spam", false),
                Tool(5, "DDoS Attack", "DDoS tool", "network", true),
                Tool(6, "Port Scanner", "Scan open ports", "network", true),
                Tool(7, "Remote Control", "Control devices", "remote", true),
                Tool(8, "TV Control", "Control TV", "remote", true),
                Tool(9, "Termux", "Terminal emulator", "advanced", true),
                Tool(10, "Encryptor", "Encrypt files", "crypto", true)
            )
            _tools.postValue(toolList)
        }
    }
}