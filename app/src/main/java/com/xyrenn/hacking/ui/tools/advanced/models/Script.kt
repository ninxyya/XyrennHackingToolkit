package com.xyrenn.hacking.ui.tools.advanced.models

data class Script(
    val id: Int,
    val name: String,
    val language: String,
    val content: String,
    val isSelected: Boolean = false
)