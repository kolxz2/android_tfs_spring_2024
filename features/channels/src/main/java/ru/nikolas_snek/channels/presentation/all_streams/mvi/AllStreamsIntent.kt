package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviIntent
import ru.nikolas_snek.channels.domain.models.Stream

sealed interface AllStreamsIntent : MviIntent {
    data object ReloadAllStreams : AllStreamsIntent
    data object StopListeningSubscribeChanges : AllStreamsIntent
    data object StartListeningSubscribeChanges : AllStreamsIntent
    data class SearchQuery(val query: String) : AllStreamsIntent
    data class ChangeSubscriptionStatus(val stream: Stream) : AllStreamsIntent
}