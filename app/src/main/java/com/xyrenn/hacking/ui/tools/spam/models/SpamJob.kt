package com.xyrenn.hacking.ui.tools.spam.models

data class SpamJob(
    val id: Int,
    val phoneNumber: String,
    val message: String,
    val count: Int,
    val scheduledTime: Long,
    val isActive: Boolean
)