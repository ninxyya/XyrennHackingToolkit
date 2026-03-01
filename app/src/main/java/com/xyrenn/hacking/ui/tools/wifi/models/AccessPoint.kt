package com.xyrenn.hacking.ui.tools.wifi.models

data class AccessPoint(
    val ssid: String,
    val bssid: String,
    val channel: Int,
    val rssi: Int,
    val isEncrypted: Boolean,
    val isSelected: Boolean = false
)