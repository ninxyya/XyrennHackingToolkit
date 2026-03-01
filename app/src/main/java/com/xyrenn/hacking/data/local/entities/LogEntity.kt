package com.xyrenn.hacking.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val target: String,
    val status: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Int,
    val packets: Int,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)