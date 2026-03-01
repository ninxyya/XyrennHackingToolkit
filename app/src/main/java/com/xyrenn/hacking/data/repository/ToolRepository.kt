package com.xyrenn.hacking.data.repository

import com.xyrenn.hacking.data.local.dao.ToolDao
import com.xyrenn.hacking.data.local.entities.ToolEntity
import com.xyrenn.hacking.data.models.Tool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolRepository @Inject constructor(
    private val toolDao: ToolDao
) {

    fun getAllTools(): Flow<List<Tool>> {
        return toolDao.getAllTools().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getToolsByCategory(category: String): Flow<List<Tool>> {
        return toolDao.getToolsByCategory(category).map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getToolById(id: Int): Flow<Tool?> {
        return toolDao.getToolById(id).map { entity ->
            entity?.toModel()
        }
    }

    suspend fun insertTool(tool: Tool) {
        toolDao.insertTool(ToolEntity.fromModel(tool))
    }

    suspend fun insertTools(tools: List<Tool>) {
        toolDao.insertAll(tools.map { ToolEntity.fromModel(it) })
    }

    suspend fun updateTool(tool: Tool) {
        toolDao.updateTool(ToolEntity.fromModel(tool))
    }

    suspend fun deleteTool(tool: Tool) {
        toolDao.deleteTool(ToolEntity.fromModel(tool))
    }

    suspend fun searchTools(query: String): List<Tool> {
        return toolDao.searchTools("%$query%").map { it.toModel() }
    }
}