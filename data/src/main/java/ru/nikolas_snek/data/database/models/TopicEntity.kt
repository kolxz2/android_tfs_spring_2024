package ru.nikolas_snek.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(
            entity = StreamsEntity::class,
            parentColumns = ["streamId"],
            childColumns = ["streamId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["streamId"])]
)
data class TopicEntity(
    @PrimaryKey
    val title: String,
    val streamId: Int,
    val streamTitle: String,
    val unreadMessages: Int = 0
)