package com.xyrenn.hacking.ui.tools.advanced.models

data class Payload(
    val id: Int,
    val name: String,
    val type: String,
    val targetOs: String,
    val format: String,
    val lhost: String,
    val lport: Int,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)