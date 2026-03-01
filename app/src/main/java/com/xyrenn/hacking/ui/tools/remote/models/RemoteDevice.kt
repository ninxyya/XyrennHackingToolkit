package com.xyrenn.hacking.ui.tools.remote.models

data class RemoteDevice(
    val id: String,
    val ip: String,
    val name: String,
    val type: String,
    val isAvailable: Boolean = true,
    val isSelected: Boolean = false
)