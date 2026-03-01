package com.xyrenn.hacking.ui.tools.spam.models

data class SpamMessage(
    val id: Int,
    val phoneNumber: String,
    val message: String,
    val timestamp: Long,
    val status: String
)