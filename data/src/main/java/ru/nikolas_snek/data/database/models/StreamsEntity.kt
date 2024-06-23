package ru.nikolas_snek.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "streams")
data class StreamsEntity(
    @PrimaryKey
    val streamId: Int,
    val title: String,
    val color: String,
    val isExpanded: Boolean = false,
    val isSubscribed: Boolean = false
)
