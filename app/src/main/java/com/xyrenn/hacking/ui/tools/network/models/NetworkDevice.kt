package com.xyrenn.hacking.ui.tools.network.models

data class NetworkDevice(
    val ip: String,
    val mac: String = "Unknown",
    val hostname: String = "Unknown",
    val vendor: String = "Unknown",
    val openPorts: List<Int> = emptyList(),
    val isSelected: Boolean = false
)