package ru.nikolas_snek.data.network.models.streams

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamSubscriptionResult(
    @Json(name = "result") val result: String,
    @Json(name = "msg") val msg: String,
    @Json(name = "events") val events: List<EventDto>
)

@JsonClass(generateAdapter = true)
data class EventDto(
    @Json(name = "type") val type: String,
    @Json(name = "op") val operation: String?,
    @Json(name = "subscriptions") val subscriptions: List<SubscriptionEventDto>?,
    @Json(name = "id") val id: Int
)

@JsonClass(generateAdapter = true)
data class SubscriptionEventDto(
    @Json(name = "audible_notifications") val audibleNotifications: Boolean?,
    @Json(name = "color") val color: String?,
    @Json(name = "desktop_notifications") val desktopNotifications: Boolean?,
    @Json(name = "email_notifications") val emailNotifications: Boolean?,
    @Json(name = "is_muted") val isMuted: Boolean?,
    @Json(name = "pin_to_top") val pinToTop: Boolean?,
    @Json(name = "push_notifications") val pushNotifications: Boolean?,
    @Json(name = "wildcard_mentions_notify") val wildcardMentionsNotify: Boolean?,
    @Json(name = "in_home_view") val inHomeView: Boolean?,
    @Json(name = "stream_weekly_traffic") val streamWeeklyTraffic: Int?,
    @Json(name = "subscribers") val subscribers: List<Int>?,
    @Json(name = "can_remove_subscribers_group") val canRemoveSubscribersGroup: Int?,
    @Json(name = "date_created") val dateCreated: Long?,
    @Json(name = "description") val description: String?,
    @Json(name = "first_message_id") val firstMessageId: Long?,
    @Json(name = "history_public_to_subscribers") val historyPublicToSubscribers: Boolean?,
    @Json(name = "invite_only") val inviteOnly: Boolean?,
    @Json(name = "is_web_public") val isWebPublic: Boolean?,
    @Json(name = "message_retention_days") val messageRetentionDays: Int?,
    @Json(name = "name") val name: String,
    @Json(name = "rendered_description") val renderedDescription: String?,
    @Json(name = "stream_id") val streamId: Int,
    @Json(name = "stream_post_policy") val streamPostPolicy: Int?,
    @Json(name = "is_announcement_only") val isAnnouncementOnly: Boolean?
)
