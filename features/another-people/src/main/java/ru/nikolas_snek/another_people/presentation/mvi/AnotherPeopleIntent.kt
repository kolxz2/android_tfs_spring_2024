package ru.nikolas_snek.another_people.presentation.mvi

import com.example.mvi.MviIntent

sealed interface AnotherPeopleIntent : MviIntent {
    data class Init(val userId: Int) : AnotherPeopleIntent
    data class Reload(val userId: Int) : AnotherPeopleIntent

    data object OnBackClicked : AnotherPeopleIntent
}