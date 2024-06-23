package ru.nikolas_snek.channels.presentation.mvi

import androidx.lifecycle.viewModelScope
import com.example.mvi.MviStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedIntent
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedPartialState
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedState

class SubscribedStore(
	reducer: SubscribedReducer,
	actor: SubscribedActor
) : MviStore<SubscribedPartialState, SubscribedIntent, SubscribedState, SubscribedEffect>(
	reducer,
	actor
) {
	val searchQueryPublisher = MutableSharedFlow<String>(extraBufferCapacity = 1)

	init {
		listenToSearchQuery()
	}

	@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
	private fun listenToSearchQuery() {
		searchQueryPublisher
			.distinctUntilChanged()
			.debounce(1000)
			.mapLatest {
				postIntent(SubscribedIntent.SearchQuery(it))
			}.launchIn(viewModelScope)
	}

	override fun createInitialState(): SubscribedState = SubscribedState()
}