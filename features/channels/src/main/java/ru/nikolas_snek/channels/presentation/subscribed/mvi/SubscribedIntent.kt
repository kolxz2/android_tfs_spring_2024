package ru.nikolas_snek.channels.presentation.subscribed.mvi

import com.example.mvi.MviIntent
import ru.nikolas_snek.channels.domain.models.Stream

sealed interface SubscribedIntent : MviIntent {
    data object ReloadSubscribed : SubscribedIntent
    data object StopListeningSubscribeChanges : SubscribedIntent
    data object StartListeningSubscribeChanges : SubscribedIntent
    data class NavigateToTopicChat(val streamTitle: String, val topicTitle: String) : SubscribedIntent
    data class LoadStreamTopics(val stream: Stream) : SubscribedIntent
    data class SearchQuery(val searchQuery: String) : SubscribedIntent
}