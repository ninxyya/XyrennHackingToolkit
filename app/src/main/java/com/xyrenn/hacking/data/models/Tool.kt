package com.xyrenn.hacking.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tools")
data class Tool(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val category: String,
    val icon: String,
    val isAvailable: Boolean = true,
    val requiresRoot: Boolean = false,
    val requiresPermission: List<String> = emptyList(),
    val order: Int = 0
)