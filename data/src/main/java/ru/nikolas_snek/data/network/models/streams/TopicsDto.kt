package ru.nikolas_snek.data.network.models.streams

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopicDto(
    @Json(name = "max_id") val maxId: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class TopicResponseDto(
    val msg: String,
    val result: String,
    @Json(name = "topics") val topicsDto: List<TopicDto>
)