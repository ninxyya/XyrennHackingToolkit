package com.xyrenn.hacking.auth.models

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val isAdmin: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)