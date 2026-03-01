package com.xyrenn.hacking.data.remote.api

import retrofit2.http.*

interface ApiService {

    @GET("tools")
    suspend fun getTools(): List<ToolResponse>

    @GET("tools/{id}")
    suspend fun getTool(@Path("id") id: String): ToolResponse

    @GET("exploits")
    suspend fun getExploits(@Query("query") query: String?): List<ExploitResponse>

    @GET("payloads")
    suspend fun getPayloads(@Query("os") os: String?): List<PayloadResponse>

    @POST("analytics")
    suspend fun sendAnalytics(@Body data: AnalyticsData)

    @GET("updates")
    suspend fun checkForUpdates(@Query("version") version: String): UpdateResponse

    data class ToolResponse(
        val id: String,
        val name: String,
        val description: String,
        val category: String,
        val version: String
    )

    data class ExploitResponse(
        val id: String,
        val name: String,
        val platform: String,
        val type: String,
        val rank: String,
        val description: String,
        val usage: String
    )

    data class PayloadResponse(
        val id: String,
        val name: String,
        val type: String,
        val targetOs: String,
        val format: String,
        val content: String
    )

    data class AnalyticsData(
        val event: String,
        val timestamp: Long,
        val data: Map<String, Any>
    )

    data class UpdateResponse(
        val available: Boolean,
        val version: String,
        val url: String,
        val changelog: String
    )
}