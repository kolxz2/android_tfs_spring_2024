package ru.nikolas_snek.profile.presentation

import com.example.mvi.MviStore
import ru.nikolas_snek.profile.domain.UserProfile
import ru.nikolas_snek.profile.presentation.mvi.ProfileActor
import ru.nikolas_snek.profile.presentation.mvi.ProfileEffect
import ru.nikolas_snek.profile.presentation.mvi.ProfileIntent
import ru.nikolas_snek.profile.presentation.mvi.ProfilePartialState
import ru.nikolas_snek.profile.presentation.mvi.ProfileReducer
import ru.nikolas_snek.profile.presentation.mvi.ProfileState

class ProfileStore(
    reducer: ProfileReducer,
    actor: ProfileActor
) : MviStore<ProfilePartialState, ProfileIntent, ProfileState<UserProfile>, ProfileEffect>(
    reducer,
    actor
) {
    override fun createInitialState(): ProfileState<UserProfile> = ProfileState.Loading
}