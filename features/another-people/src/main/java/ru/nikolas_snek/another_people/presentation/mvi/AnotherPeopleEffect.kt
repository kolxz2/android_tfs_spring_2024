package ru.nikolas_snek.another_people.presentation.mvi

import com.example.mvi.MviEffect

sealed interface AnotherPeopleEffect : MviEffect {
    data object NavigateGoBack : AnotherPeopleEffect

}