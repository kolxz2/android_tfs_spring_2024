package ru.nikolas_snek.data.network.models.streams

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllStreamsDto(
    @Json(name = "can_remove_subscribers_group") val canRemoveSubscribersGroup: Int,
    @Json(name = "date_created") val dateCreated: Long,
    val description: String,
    @Json(name = "first_message_id") val firstMessageId: Int,
    @Json(name = "history_public_to_subscribers") val historyPublicToSubscribers: Boolean,
    @Json(name = "invite_only") val inviteOnly: Boolean,
    @Json(name = "is_announcement_only") val isAnnouncementOnly: Boolean,
    @Json(name = "is_default") val isDefault: Boolean?,
    @Json(name = "is_web_public") val isWebPublic: Boolean,
    @Json(name = "message_retention_days") val messageRetentionDays: Int?,
    val name: String,
    @Json(name = "rendered_description") val renderedDescription: String,
    @Json(name = "stream_id") val streamId: Int,
    @Json(name = "stream_post_policy") val streamPostPolicy: Int,
    @Json(name = "stream_weekly_traffic") val streamWeeklyTraffic: Int?
)

@JsonClass(generateAdapter = true)
data class AllStreamsResponseDto(
    val msg: String,
    val result: String,
    @Json(name = "streams") val allStreamsDto: List<AllStreamsDto>
)
