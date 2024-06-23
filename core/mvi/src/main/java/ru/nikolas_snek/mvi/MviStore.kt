package com.example.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

abstract class MviStore<
        PartialState : MviPartialState,
        Intent : MviIntent,
        State : MviState,
        Effect : MviEffect>(
    private val reducer: MviReducer<PartialState, State>,
    private val actor: MviActor<PartialState, Intent, State, Effect>
) : ViewModel() {
    private val initialState: State by lazy { createInitialState() }

    abstract fun createInitialState(): State

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    private val _intent: MutableSharedFlow<Intent> = MutableSharedFlow()
    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    init {
        subscribe()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun subscribe() {
        _intent
            .flatMapMerge { actor.resolve(it, _uiState.value) }
            .scan(initialState, reducer::reduce)
            .onEach { _uiState.emit(it) }
            .launchIn(viewModelScope)

        actor.effects.onEach { _effect.emit(it) }.launchIn(viewModelScope)
    }

    fun postIntent(intent: Intent) {
        viewModelScope.launch { _intent.emit(intent) }
    }
}