package com.xyrenn.hacking.ui.tools.remote.models

data class IRCommand(
    val id: Int,
    val deviceType: String,
    val command: String,
    val hexCode: String,
    val frequency: Int = 38000
)