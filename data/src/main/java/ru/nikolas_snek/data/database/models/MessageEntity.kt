package ru.nikolas_snek.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class MessageEntity(
    @PrimaryKey
    val id: Long,
    val avatarUrl: String,
    val client: String,
    val content: String,
    val isMeMessage: Boolean,
    val senderEmail: String,
    val senderFullName: String,
    val senderId: Int,
    val timestamp: Long,
    val streamTitle: String,
    val topicTitle: String
)