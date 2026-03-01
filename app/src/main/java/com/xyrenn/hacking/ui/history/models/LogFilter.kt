package com.xyrenn.hacking.ui.history.models

data class LogFilter(
    val type: String? = null,
    val status: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val searchQuery: String? = null
)