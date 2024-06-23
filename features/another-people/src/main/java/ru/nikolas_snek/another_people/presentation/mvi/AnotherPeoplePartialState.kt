package ru.nikolas_snek.another_people.presentation.mvi

import com.example.mvi.MviPartialState
import ru.nikolas_snek.another_people.domain.UserProfile

sealed interface AnotherPeoplePartialState : MviPartialState {
    data class DataLoaded(val userProfileData: UserProfile) : AnotherPeoplePartialState
    data object Error : AnotherPeoplePartialState
}