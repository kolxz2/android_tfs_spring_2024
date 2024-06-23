package ru.nikolas_snek.chat.presentation.mvi

import com.example.mvi.MviEffect

sealed interface ChatEffect : MviEffect {
    data object NavigateToGoBack : ChatEffect
    data object ErrorLoadMessages : ChatEffect

}