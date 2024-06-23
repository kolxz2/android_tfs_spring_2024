package ru.nikolas_snek.another_people.presentation.mvi

import com.example.mvi.MviReducer
import ru.nikolas_snek.another_people.domain.UserProfile
import javax.inject.Inject

class AnotherPeopleReducer @Inject constructor() :
    MviReducer<AnotherPeoplePartialState, AnotherPeopleState<UserProfile>> {
    override fun reduce(
        prevState: AnotherPeopleState<UserProfile>,
        partialState: AnotherPeoplePartialState
    ): AnotherPeopleState<UserProfile> {
        return when (partialState) {
            is AnotherPeoplePartialState.DataLoaded -> AnotherPeopleState.DataLoaded(partialState.userProfileData)
            AnotherPeoplePartialState.Error -> AnotherPeopleState.Error
        }
    }
}