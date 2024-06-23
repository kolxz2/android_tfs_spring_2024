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

class AllStreamsStore(
    private val reducer: AllStreamsReducer,
    private val actor: AllStreamsActor
) : MviStore<AllStreamsPartialState, AllStreamsIntent, AllStreamsState, AllStreamsEffect>(
    reducer,
    actor
){

    override fun createInitialState(): AllStreamsState = AllStreamsState()

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
                postIntent(AllStreamsIntent.SearchQuery(it))
            }.launchIn(viewModelScope)
    }



    override fun onCleared() {
        super.onCleared()
        actor.onDestroy()
    }

}