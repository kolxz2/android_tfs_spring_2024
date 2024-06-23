package ru.nikolas_snek.chat.domain.models

data class Message(
    val avatarUrl: String,
    val client: String,
    val content: String,
    val id: Long,
    val isMeMessage: Boolean,
    val reactions: List<Reaction>,
    val senderEmail: String,
    val senderFullName: String,
    val senderId: Int,
    val timestamp: Long,
)
