package ru.nikolas_snek.peoples.presentation.mvi

import com.example.mvi.MviPartialState
import ru.nikolas_snek.peoples.domain.Profile

sealed interface PeoplesPartialState: MviPartialState {
	data class DataLoaded(val data: List<Profile>) : PeoplesPartialState
	data class DataLoadedWithSearch(val data: List<Profile>, val searchQuery: String) :
		PeoplesPartialState

	data object Error : PeoplesPartialState
	data object StartLoading : PeoplesPartialState
	data object NoData : PeoplesPartialState
}