package ru.nikolas_snek.peoples.presentation.mvi

import com.example.mvi.MviReducer
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import javax.inject.Inject

class PeoplesReducer @Inject constructor() :
	MviReducer<PeoplesPartialState, PeoplesState> {
	override fun reduce(
		prevState: PeoplesState,
		partialState: PeoplesPartialState
	): PeoplesState =
		when (partialState) {
			is PeoplesPartialState.DataLoaded -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.SUCCESS,
				listProfiles = partialState.data
			)

			PeoplesPartialState.Error -> prevState.copy(
				loadingStatus = if (prevState.loadingStatus == LoadingAttemptStatus.FIRST_ATTEMPT) LoadingAttemptStatus.FIRST_ATTEMPT else LoadingAttemptStatus.FIRST_ATTEMPT
			)

			PeoplesPartialState.StartLoading -> prevState.copy(loadingStatus = LoadingAttemptStatus.LOADING)
			PeoplesPartialState.NoData -> prevState
			is PeoplesPartialState.DataLoadedWithSearch -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.SUCCESS,
				listProfiles = partialState.data,
				searchQuery = partialState.searchQuery
			)
		}
}