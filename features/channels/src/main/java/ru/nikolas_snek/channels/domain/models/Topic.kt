package ru.nikolas_snek.channels.domain.models


data class Topic(
    val title: String,
    val streamTitle: String,
    val unreadMessages: Int = 0
)