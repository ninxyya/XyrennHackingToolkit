package com.xyrenn.hacking.ui.tools.crypto.models

data class CryptoKey(
    val id: Int,
    val name: String,
    val algorithm: String,
    val keySize: Int,
    val keyValue: String,
    val createdAt: Long = System.currentTimeMillis()
)