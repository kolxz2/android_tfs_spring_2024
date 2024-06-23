package ru.nikolas_snek.data.network.models.messages

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterTopicEventResponseDto(
    @Json(name = "result") val result: String,
    @Json(name = "msg") val msg: String?,
    @Json(name = "queue_id") val queueId: String,
    @Json(name = "zulip_version") val zulipVersion: String,
    @Json(name = "zulip_feature_level") val zulipFeatureLevel: Int,
    @Json(name = "zulip_merge_base") val zulipMergeBase: String,
    @Json(name = "max_message_id") val maxMessageId: Long,
    @Json(name = "last_event_id") val lastEventId: Long
)
