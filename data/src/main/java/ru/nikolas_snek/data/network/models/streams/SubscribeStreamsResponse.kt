package ru.nikolas_snek.data.network.models.streams

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscribeStreamsResponse(
    @Json(name = "result") val result: String,
    @Json(name = "msg") val msg: String?,
    @Json(name = "queue_id") val queueId: String,
    @Json(name = "zulip_version") val zulipVersion: String?,
    @Json(name = "zulip_feature_level") val zulipFeatureLevel: Int?,
    @Json(name = "zulip_merge_base") val zulipMergeBase: String?,
    @Json(name = "subscriptions") val subscriptionDto: List<SubscriptionDto>,
    @Json(name = "unsubscribed") val unsubscribed: List<SubscriptionDto>?,
    @Json(name = "never_subscribed") val neverSubscribed: List<NeverSubscribedStreamDetails>?,
    @Json(name = "last_event_id") val lastEventId: Int
)

@JsonClass(generateAdapter = true)
data class SubscriptionDto(
    @Json(name = "audible_notifications") val audibleNotifications: Boolean?,
    @Json(name = "can_remove_subscribers_group") val canRemoveSubscribersGroup: Int,
    @Json(name = "color") val color: String,
    @Json(name = "date_created") val dateCreated: Long,
    @Json(name = "description") val description: String?,
    @Json(name = "desktop_notifications") val desktopNotifications: Boolean,
    @Json(name = "email_notifications") val emailNotifications: Boolean,
    @Json(name = "first_message_id") val firstMessageId: Long,
    @Json(name = "history_public_to_subscribers") val historyPublicToSubscribers: Boolean,
    @Json(name = "in_home_view") val inHomeView: Boolean,
    @Json(name = "invite_only") val inviteOnly: Boolean,
    @Json(name = "is_announcement_only") val isAnnouncementOnly: Boolean,
    @Json(name = "is_muted") val isMuted: Boolean,
    @Json(name = "is_web_public") val isWebPublic: Boolean,
    @Json(name = "message_retention_days") val messageRetentionDays: Int?, // Change the type according to your API
    @Json(name = "name") val name: String,
    @Json(name = "pin_to_top") val pinToTop: Boolean,
    @Json(name = "push_notifications") val pushNotifications: Boolean,
    @Json(name = "rendered_description") val renderedDescription: String?,
    @Json(name = "stream_id") val streamId: Int,
    @Json(name = "stream_post_policy") val streamPostPolicy: Int,
    @Json(name = "stream_weekly_traffic") val streamWeeklyTraffic: Int?, // Change the type according to your API
    @Json(name = "wildcard_mentions_notify") val wildcardMentionsNotify: Int? // Change the type according to your API
)

@JsonClass(generateAdapter = true)
data class NeverSubscribedStreamDetails(
    @Json(name = "can_remove_subscribers_group") val canRemoveSubscribersGroup: Int,
    @Json(name = "creator_id") val creatorId: Int,
    @Json(name = "date_created") val dateCreated: Long,
    @Json(name = "description") val description: String,
    @Json(name = "first_message_id") val firstMessageId: Long,
    @Json(name = "history_public_to_subscribers") val historyPublicToSubscribers: Boolean,
    @Json(name = "invite_only") val inviteOnly: Boolean,
    @Json(name = "is_announcement_only") val isAnnouncementOnly: Boolean,
    @Json(name = "is_web_public") val isWebPublic: Boolean,
    @Json(name = "message_retention_days") val messageRetentionDays: Int?,
    @Json(name = "name") val name: String,
    @Json(name = "rendered_description") val renderedDescription: String,
    @Json(name = "stream_id") val streamId: Int,
    @Json(name = "stream_post_policy") val streamPostPolicy: Int,
    @Json(name = "stream_weekly_traffic") val streamWeeklyTraffic: Int?
)