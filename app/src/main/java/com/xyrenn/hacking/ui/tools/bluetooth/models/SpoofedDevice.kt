package com.xyrenn.hacking.ui.tools.bluetooth.models

data class SpoofedDevice(
    val name: String,
    val type: String,
    val address: String,
    val uuid: String,
    val createdAt: Long = System.currentTimeMillis()
)