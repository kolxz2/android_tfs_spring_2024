package ru.nikolas_snek.profile.presentation.mvi

import com.example.mvi.MviPartialState
import ru.nikolas_snek.profile.domain.UserProfile

sealed interface ProfilePartialState : MviPartialState {
    data class DataLoaded(val data: UserProfile) : ProfilePartialState
    data object Error : ProfilePartialState
}