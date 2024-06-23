package ru.nikolas_snek.profile.presentation.mvi

import com.example.mvi.MviReducer
import ru.nikolas_snek.profile.domain.UserProfile
import javax.inject.Inject

class ProfileReducer @Inject constructor() :
    MviReducer<ProfilePartialState, ProfileState<UserProfile>> {
    override fun reduce(
        prevState: ProfileState<UserProfile>,
        partialState: ProfilePartialState
    ): ProfileState<UserProfile> {
        return when (partialState) {
            is ProfilePartialState.DataLoaded -> ProfileState.DataLoaded(partialState.data)
            ProfilePartialState.Error -> ProfileState.Error
        }
    }
}