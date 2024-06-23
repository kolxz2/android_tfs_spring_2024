package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviPartialState
import ru.nikolas_snek.channels.domain.models.Stream

sealed interface AllStreamsPartialState : MviPartialState {
	data class DataLoaded(val data: List<Stream>, val searchQuery: String) : AllStreamsPartialState
	data object Error : AllStreamsPartialState
	data object Loading : AllStreamsPartialState
	data object NoData : AllStreamsPartialState
}