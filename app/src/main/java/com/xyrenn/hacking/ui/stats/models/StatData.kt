package com.xyrenn.hacking.ui.stats.models

data class StatData(
    val totalAttacks: Int,
    val successRate: Int,
    val totalPackets: Int,
    val wifiAttacks: Int,
    val spamAttacks: Int,
    val networkAttacks: Int,
    val remoteAttacks: Int,
    val cryptoAttacks: Int,
    val activeTime: String
)