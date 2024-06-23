package ru.nikolas_snek.profile.presentation.mvi

import com.example.mvi.MviState

sealed interface ProfileState<out T> : MviState {
    data object Error : ProfileState<Nothing>

    data object Loading : ProfileState<Nothing>

    data class DataLoaded<T>(val data: T) : ProfileState<T>
}


