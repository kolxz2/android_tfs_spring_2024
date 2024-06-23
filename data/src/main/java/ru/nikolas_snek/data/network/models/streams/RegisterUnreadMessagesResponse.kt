package ru.nikolas_snek.data.network.models.streams

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnreadMessageDto(
    @Json(name = "stream_id") val streamId: Int,
    @Json(name = "topic") val topicTitle: String,
    @Json(name = "unread_message_ids") val unreadMessageIds: List<Long>
)

@JsonClass(generateAdapter = true)
data class UnreadMsgsDto(
    val pms: List<Any>,
    val streams: List<UnreadMessageDto>,
    val huddles: List<Any>,
    val mentions: List<Any>,
    val count: Int,
    @Json(name = "old_unreads_missing") val oldUnreadsMissing: Boolean
)

@JsonClass(generateAdapter = true)
data class RegisterUnreadMessagesResponse(
    val result: String,
    val msg: String,
    @Json(name = "queue_id") val queueId: String,
    @Json(name = "zulip_version") val zulipVersion: String,
    @Json(name = "zulip_feature_level") val zulipFeatureLevel: Int,
    @Json(name = "zulip_merge_base") val zulipMergeBase: String,
    @Json(name = "max_message_id") val maxMessageId: Long,
    @Json(name = "unread_msgs") val unreadMsgsDto: UnreadMsgsDto,
    @Json(name = "last_event_id") val lastEventId: Long
)