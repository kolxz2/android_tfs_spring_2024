package ru.nikolas_snek.channels.domain.models

data class Stream(
    val streamId: Int,
    val title: String,
    val color: String,
    val isExpanded: Boolean = false,
    val isSubscribed: Boolean = false
)
