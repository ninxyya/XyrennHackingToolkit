package com.xyrenn.hacking.ui.tools.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.network.models.PortInfo
import com.xyrenn.hacking.utils.helpers.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortScannerViewModel @Inject constructor(
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _ports = MutableLiveData<List<PortInfo>>()
    val ports: LiveData<List<PortInfo>> = _ports

    private val _isScanning = MutableLiveData(false)
    val isScanning: LiveData<Boolean> = _isScanning

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val commonPorts = mapOf(
        20 to "FTP Data",
        21 to "FTP Control",
        22 to "SSH",
        23 to "Telnet",
        25 to "SMTP",
        53 to "DNS",
        80 to "HTTP",
        110 to "POP3",
        111 to "RPC",
        135 to "RPC",
        139 to "NetBIOS",
        143 to "IMAP",
        443 to "HTTPS",
        445 to "SMB",
        993 to "IMAPS",
        995 to "POP3S",
        1723 to "PPTP",
        3306 to "MySQL",
        3389 to "RDP",
        5432 to "PostgreSQL",
        5900 to "VNC",
        6379 to "Redis",
        8080 to "HTTP-Alt",
        8443 to "HTTPS-Alt",
        27017 to "MongoDB"
    )

    fun scanPorts(targetIp: String, startPort: Int, endPort: Int) {
        viewModelScope.launch {
            _isScanning.value = true
            _status.value = "Scanning ports $startPort-$endPort on $targetIp"
            _ports.value = emptyList()

            val openPorts = mutableListOf<PortInfo>()
            val totalPorts = endPort - startPort + 1

            for (port in startPort..endPort) {
                if (!_isScanning.value!!) break

                val isOpen = networkHelper.isPortOpen(targetIp, port)
                if (isOpen) {
                    val service = commonPorts[port] ?: "Unknown"
                    openPorts.add(PortInfo(port, service, "open"))
                    _ports.value = openPorts.toList()
                }

                val progress = ((port - startPort + 1) * 100 / totalPorts)
                _status.value = "Scanning... $progress%"
                delay(20)
            }

            _isScanning.value = false
            _status.value = "Scan complete. Found ${openPorts.size} open ports"
        }
    }

    fun stopScan() {
        _isScanning.value = false
        _status.value = "Scan stopped"
    }
}