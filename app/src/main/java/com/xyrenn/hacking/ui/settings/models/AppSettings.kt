package com.xyrenn.hacking.ui.settings.models

data class AppSettings(
    val id: String,
    val title: String,
    val description: String,
    val type: String, // "navigation", "switch", "action"
    val icon: String,
    val value: String,
    val isSwitchChecked: Boolean = false,
    val isDestructive: Boolean = false
)