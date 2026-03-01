package com.xyrenn.hacking.ui.tools.wifi.models

data class Client(
    val mac: String,
    val ip: String,
    val deviceName: String,
    val rssi: Int,
    val isSelected: Boolean = false
)