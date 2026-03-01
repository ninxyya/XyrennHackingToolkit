package com.xyrenn.hacking.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xyrenn.hacking.data.models.Tool

@Entity(tableName = "tools")
data class ToolEntity(
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
) {
    fun toModel(): Tool {
        return Tool(
            id = id,
            name = name,
            description = description,
            category = category,
            icon = icon,
            isAvailable = isAvailable,
            requiresRoot = requiresRoot,
            requiresPermission = requiresPermission,
            order = order
        )
    }

    companion object {
        fun fromModel(tool: Tool): ToolEntity {
            return ToolEntity(
                id = tool.id,
                name = tool.name,
                description = tool.description,
                category = tool.category,
                icon = tool.icon,
                isAvailable = tool.isAvailable,
                requiresRoot = tool.requiresRoot,
                requiresPermission = tool.requiresPermission,
                order = tool.order
            )
        }
    }
}