package com.xyrenn.hacking.ui.history.models

data class AttackLog(
    val id: Int,
    val type: String,
    val target: String,
    val status: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Int,
    val details: String
)