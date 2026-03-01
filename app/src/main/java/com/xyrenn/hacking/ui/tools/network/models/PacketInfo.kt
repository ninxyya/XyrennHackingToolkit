package com.xyrenn.hacking.ui.tools.network.models

data class PacketInfo(
    val protocol: String,
    val sourceIp: String,
    val destIp: String,
    val sourcePort: Int,
    val destPort: Int,
    val size: Int,
    val data: String,
    val timestamp: Long
)