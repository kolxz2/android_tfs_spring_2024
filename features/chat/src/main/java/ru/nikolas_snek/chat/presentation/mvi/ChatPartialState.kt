package ru.nikolas_snek.chat.presentation.mvi

import com.example.mvi.MviPartialState
import ru.nikolas_snek.chat.domain.models.Message

sealed interface ChatPartialState : MviPartialState {
    data class DataLoaded(val data: List<Message>) : ChatPartialState
    data object Error : ChatPartialState
    data object StartLoading : ChatPartialState
    data object NoData : ChatPartialState
}