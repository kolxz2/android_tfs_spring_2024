package ru.nikolas_snek.data.network.models.messages

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageResponseDto(
    @Json(name = "result") val result: String,
    @Json(name = "msg") val msg: String?,
    @Json(name = "messages") val messages: List<MessageDto>,
    @Json(name = "found_anchor") val foundAnchor: Boolean,
    @Json(name = "found_oldest") val foundOldest: Boolean,
    @Json(name = "found_newest") val foundNewest: Boolean,
    @Json(name = "history_limited") val historyLimited: Boolean,
    @Json(name = "anchor") val anchor: Long
)

@JsonClass(generateAdapter = true)
data class MessageDto(
    @Json(name = "id") val id: Long,
    @Json(name = "sender_id") val senderId: Int,
    @Json(name = "content") val content: String,
    @Json(name = "recipient_id") val recipientId: Long,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "client") val client: String,
    @Json(name = "subject") val subject: String,
    @Json(name = "topic_links") val topicLinks: List<Any>,
    @Json(name = "is_me_message") val isMeMessage: Boolean,
    @Json(name = "reactions") val reactions: List<ReactionDto>,
    @Json(name = "submessages") val submessages: List<Any>,
    @Json(name = "flags") val flags: List<String>,
    @Json(name = "sender_full_name") val senderFullName: String,
    @Json(name = "sender_email") val senderEmail: String,
    @Json(name = "sender_realm_str") val senderRealmStr: String,
    @Json(name = "display_recipient") val displayRecipient: String,
    @Json(name = "type") val type: String,
    @Json(name = "stream_id") val streamId: Long,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "content_type") val contentType: String
)

@JsonClass(generateAdapter = true)
data class ReactionDto(
    @Json(name = "emoji_name") val emojiName: String,
    @Json(name = "emoji_code") val emojiCode: String,
    @Json(name = "reaction_type") val reactionType: String,
    @Json(name = "user") val reactionUserDto: ReactionUserDto,
    @Json(name = "user_id") val userId: Int?
)

@JsonClass(generateAdapter = true)
data class ReactionUserDto(
    @Json(name = "email") val email: String,
    @Json(name = "id") val id: Long,
    @Json(name = "full_name") val fullName: String
)
