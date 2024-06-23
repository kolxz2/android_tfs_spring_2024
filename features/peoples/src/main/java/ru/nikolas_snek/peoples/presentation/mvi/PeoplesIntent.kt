package ru.nikolas_snek.peoples.presentation.mvi

import com.example.mvi.MviIntent

sealed interface PeoplesIntent : MviIntent {
    data object Reload : PeoplesIntent
    data object StopListeningPeoplesListChanges : PeoplesIntent
    data object StartListeningPeoplesListChanges : PeoplesIntent
    data class SearchQuery(val query: String) : PeoplesIntent
    data class NavigateToUserPeoplesDetails(val userId: Int) : PeoplesIntent
}