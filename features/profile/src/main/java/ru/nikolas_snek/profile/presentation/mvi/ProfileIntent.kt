package ru.nikolas_snek.profile.presentation.mvi

import com.example.mvi.MviIntent

sealed interface ProfileIntent : MviIntent {
    data object Init : ProfileIntent
    data object Reload : ProfileIntent
}