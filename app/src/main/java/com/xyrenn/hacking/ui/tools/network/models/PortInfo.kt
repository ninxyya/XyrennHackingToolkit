package com.xyrenn.hacking.ui.tools.network.models

data class PortInfo(
    val port: Int,
    val service: String,
    val state: String,
    val isSelected: Boolean = false
)