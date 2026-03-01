package com.xyrenn.hacking.ui.tools.spam.models

data class Contact(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val isSelected: Boolean = false
)