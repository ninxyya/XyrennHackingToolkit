package com.xyrenn.hacking.ui.tools.network.models

data class Target(
    val ip: String,
    val hostname: String? = null,
    val isAlive: Boolean = false,
    val openPorts: List<Int> = emptyList(),
    val lastSeen: Long = System.currentTimeMillis()
)