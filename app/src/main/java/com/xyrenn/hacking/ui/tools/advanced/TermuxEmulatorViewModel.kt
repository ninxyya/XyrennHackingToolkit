package com.xyrenn.hacking.ui.tools.advanced

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.utils.helpers.TermuxHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermuxEmulatorViewModel @Inject constructor(
    private val termuxHelper: TermuxHelper
) : ViewModel() {

    private val _output = MutableLiveData("Termux Emulator Ready\n$ ")
    val output: LiveData<String> = _output

    private val _isExecuting = MutableLiveData(false)
    val isExecuting: LiveData<Boolean> = _isExecuting

    private var commandHistory = mutableListOf<String>()
    private var historyIndex = -1

    init {
        // Add welcome message
        appendToOutput("Welcome to Termux Emulator")
        appendToOutput("Type 'help' for available commands")
    }

    fun executeCommand(command: String) {
        if (command.isBlank()) return

        viewModelScope.launch {
            _isExecuting.value = true
            appendToOutput("$ $command")

            commandHistory.add(command)
            historyIndex = commandHistory.size

            val result = termuxHelper.executeCommand(command)
            appendToOutput(result)

            _isExecuting.value = false
        }
    }

    fun installPackage(packageName: String) {
        executeCommand("pkg install $packageName")
    }

    fun clearOutput() {
        _output.value = "Termux Emulator Ready\n$ "
    }

    private fun appendToOutput(text: String) {
        val current = _output.value ?: ""
        _output.value = "$current\n$text\n$ "
    }

    fun getPreviousCommand(): String {
        if (commandHistory.isNotEmpty() && historyIndex > 0) {
            historyIndex--
            return commandHistory[historyIndex]
        }
        return ""
    }

    fun getNextCommand(): String {
        if (commandHistory.isNotEmpty() && historyIndex < commandHistory.size - 1) {
            historyIndex++
            return commandHistory[historyIndex]
        }
        return ""
    }
}