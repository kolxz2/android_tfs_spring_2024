package ru.nikolas_snek.data.network.models.streams

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscriptionStreamDto(
    @Json(name = "audible_notifications") val audibleNotifications: Boolean?,
    val color: String,
    val description: String,
    @Json(name = "desktop_notifications") val desktopNotifications: Boolean?,
    @Json(name = "invite_only") val inviteOnly: Boolean,
    @Json(name = "is_muted") val isMuted: Boolean,
    val name: String,
    @Json(name = "pin_to_top") val pinToTop: Boolean,
    @Json(name = "push_notifications") val pushNotifications: Boolean?,
    @Json(name = "stream_id") val streamId: Int,
    val subscribers: List<Int>?
)

@JsonClass(generateAdapter = true)
data class SubscriptionStreamsResponseDto(
    val msg: String,
    val result: String,
    @Json(name = "subscriptions") val subscriptionStreamDtoList: List<SubscriptionStreamDto>
)
