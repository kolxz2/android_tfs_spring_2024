package ru.nikolas_snek.channels.presentation.subscribed.recycler.utils

import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.domain.models.Topic

sealed class SubscribedRecyclerItems {
    data class StreamItem(val stream: Stream) : SubscribedRecyclerItems()
    data class TopicItem(val topic: Topic) : SubscribedRecyclerItems()
}