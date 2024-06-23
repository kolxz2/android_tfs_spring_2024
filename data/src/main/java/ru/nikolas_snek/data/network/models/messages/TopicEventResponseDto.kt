package ru.nikolas_snek.data.network.models.messages

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopicEventResponseDto(
    @Json(name = "result") val result: String,
    @Json(name = "msg") val msg: String?,
    @Json(name = "events") val events: List<TopicEventDto>
)

@JsonClass(generateAdapter = true)
data class TopicEventDto(
    @Json(name = "type") val type: String,
    @Json(name = "op") val op: String?,
    @Json(name = "user_id") val userId: Int?,
    @Json(name = "user") val user: UserEventDto?,
    @Json(name = "message_id") val messageId: Long?,
    @Json(name = "emoji_name") val emojiName: String?,
    @Json(name = "emoji_code") val emojiCode: String?,
    @Json(name = "reaction_type") val reactionType: String?,
    @Json(name = "id") val id: Long?,
    @Json(name = "message") val message: MessageEventDto?,
    @Json(name = "flags") val flags: List<String>?
)

@JsonClass(generateAdapter = true)
data class UserEventDto(
    @Json(name = "user_id") val userId: Int?,
    @Json(name = "email") val email: String,
    @Json(name = "full_name") val fullName: String
)

@JsonClass(generateAdapter = true)
data class MessageEventDto(
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
    @Json(name = "submessages") val submessages: List<Any>?,
    @Json(name = "sender_full_name") val senderFullName: String,
    @Json(name = "sender_email") val senderEmail: String,
    @Json(name = "sender_realm_str") val senderRealmStr: String,
    @Json(name = "display_recipient") val displayRecipient: String,
    @Json(name = "type") val type: String,
    @Json(name = "stream_id") val streamId: Long,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "content_type") val contentType: String
)
