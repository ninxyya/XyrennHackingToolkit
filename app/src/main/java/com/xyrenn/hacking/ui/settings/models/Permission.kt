package com.xyrenn.hacking.ui.settings.models

data class Permission(
    val id: String,
    val name: String,
    val androidPermission: String,
    val description: String,
    val rationale: String,
    val isGranted: Boolean = false,
    val shouldShowRationale: Boolean = false,
    val minSdk: Int? = null
)