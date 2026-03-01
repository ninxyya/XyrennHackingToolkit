package com.xyrenn.hacking.ui.tools.wifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.wifi.models.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeautherViewModel @Inject constructor() : ViewModel() {

    private val _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>> = _clients

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    private val _attackStatus = MutableLiveData("Idle")
    val attackStatus: LiveData<String> = _attackStatus

    private val selectedClients = mutableListOf<Client>()
    private var isAttacking = false

    fun scanClients(targetSsid: String) {
        viewModelScope.launch {
            _isScanning.value = true
            _attackStatus.value = "Scanning clients for $targetSsid..."

            delay(2000)

            val scannedClients = listOf(
                Client("AA:BB:CC:DD:EE:01", "192.168.1.2", "iPhone", -45),
                Client("AA:BB:CC:DD:EE:02", "192.168.1.3", "Android", -50),
                Client("AA:BB:CC:DD:EE:03", "192.168.1.4", "Laptop", -55)
            )

            _clients.value = scannedClients
            _isScanning.value = false
            _attackStatus.value = "Found ${scannedClients.size} clients"
        }
    }

    fun toggleClientSelection(client: Client) {
        if (selectedClients.contains(client)) {
            selectedClients.remove(client)
        } else {
            selectedClients.add(client)
        }
        // Update UI to show selection
        _clients.value = _clients.value?.map {
            if (it.mac == client.mac) it.copy(isSelected = !it.isSelected) else it
        }
    }

    fun startDeauthSelected() {
        if (selectedClients.isEmpty()) {
            _attackStatus.value = "No clients selected"
            return
        }

        viewModelScope.launch {
            isAttacking = true
            _attackStatus.value = "Deauthing ${selectedClients.size} clients..."

            while (isAttacking) {
                delay(100)
            }
        }
    }

    fun startDeauthAll() {
        viewModelScope.launch {
            isAttacking = true
            _attackStatus.value = "Deauthing ALL clients..."

            while (isAttacking) {
                delay(100)
            }
        }
    }

    fun stopDeauth() {
        isAttacking = false
        _attackStatus.value = "Attack stopped"
    }
}