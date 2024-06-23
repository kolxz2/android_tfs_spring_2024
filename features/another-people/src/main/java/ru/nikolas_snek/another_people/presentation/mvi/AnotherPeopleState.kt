package ru.nikolas_snek.another_people.presentation.mvi

import com.example.mvi.MviState

sealed interface AnotherPeopleState<out T> : MviState {
    data object Error : AnotherPeopleState<Nothing>

    data object Loading : AnotherPeopleState<Nothing>

    data class DataLoaded<T>(val userProfileData: T) : AnotherPeopleState<T>
}


