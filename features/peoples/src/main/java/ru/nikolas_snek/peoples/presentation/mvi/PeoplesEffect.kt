package ru.nikolas_snek.peoples.presentation.mvi

import com.example.mvi.MviEffect

sealed interface PeoplesEffect : MviEffect {
    data class NavigateToUserPeoplesDetails(val userId: Int) : PeoplesEffect
    data object NetworkError : PeoplesEffect

}