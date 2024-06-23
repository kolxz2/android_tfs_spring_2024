package ru.nikolas_snek.data.network.models.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserOnlineStatusDto(
    @Json(name = "result") val result: String?,
    @Json(name = "msg") val msg: String?,
    @Json(name = "presence") val presenceDto: PresenceDto?
)

@JsonClass(generateAdapter = true)
data class PresenceDto(
    @Json(name = "aggregated") val aggregated: PresenceStatusDto?,
    @Json(name = "website") val website: PresenceStatusDto?
)

@JsonClass(generateAdapter = true)
data class PresenceStatusDto(
    @Json(name = "status") val status: String?,
    @Json(name = "timestamp") val timestamp: Long?
)