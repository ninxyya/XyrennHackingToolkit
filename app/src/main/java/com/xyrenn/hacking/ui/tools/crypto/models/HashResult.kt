package com.xyrenn.hacking.ui.tools.crypto.models

data class HashResult(
    val input: String,
    val algorithm: String,
    val hash: String,
    val timestamp: Long = System.currentTimeMillis()
)