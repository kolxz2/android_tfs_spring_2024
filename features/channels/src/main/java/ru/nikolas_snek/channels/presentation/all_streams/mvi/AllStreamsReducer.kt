package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviReducer
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import javax.inject.Inject

class AllStreamsReducer @Inject constructor() :
	MviReducer<AllStreamsPartialState, AllStreamsState> {

	override fun reduce(
		prevState: AllStreamsState,
		partialState: AllStreamsPartialState
	): AllStreamsState {
		return when (partialState) {
			is AllStreamsPartialState.DataLoaded -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.SUCCESS,
				listProfiles = partialState.data,
				searchQuery = partialState.searchQuery
			)

			AllStreamsPartialState.Error -> prevState.copy(
				loadingStatus = if (prevState.loadingStatus == LoadingAttemptStatus.FIRST_ATTEMPT)
					LoadingAttemptStatus.FAILURE else
					LoadingAttemptStatus.FIRST_ATTEMPT
			)

			AllStreamsPartialState.Loading -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.LOADING
			)

			AllStreamsPartialState.NoData -> prevState
		}
	}

}