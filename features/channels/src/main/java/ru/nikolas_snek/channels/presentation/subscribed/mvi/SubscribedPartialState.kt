package ru.nikolas_snek.channels.presentation.subscribed.mvi

import com.example.mvi.MviPartialState
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.domain.models.Topic

sealed interface SubscribedPartialState : MviPartialState {
	data class StreamLoaded(val data: List<Stream>, val searchQuery: String) :
		SubscribedPartialState

	data class TopicLoaded(val data: Pair<Stream, List<Topic>>) : SubscribedPartialState
	data object Error : SubscribedPartialState
	data object LoadingStreams : SubscribedPartialState
	data object LoadingTopics : SubscribedPartialState
	data object NoData : SubscribedPartialState
}