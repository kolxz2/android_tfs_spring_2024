package ru.nikolas_snek.chat.presentation.mvi

import com.example.mvi.MviReducer
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import javax.inject.Inject

class ChatReducer @Inject constructor() :
	MviReducer<ChatPartialState, ChatState> {

	override fun reduce(
		prevState: ChatState,
		partialState: ChatPartialState
	): ChatState {
		return when (partialState) {
			is ChatPartialState.DataLoaded -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.SUCCESS,
				listProfiles = mergeAndSortMessages(prevState.listProfiles, partialState.data)
			)

			ChatPartialState.Error -> prevState.copy(
				loadingStatus = determineErrorStatus(prevState)
			)

			ChatPartialState.NoData -> prevState
			ChatPartialState.StartLoading -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.LOADING
			)
		}
	}

	private fun determineErrorStatus(prevState: ChatState): LoadingAttemptStatus {
		return if (prevState.loadingStatus == LoadingAttemptStatus.FIRST_ATTEMPT)
			LoadingAttemptStatus.FAILURE
		else
			LoadingAttemptStatus.FIRST_ATTEMPT
	}

	private fun mergeAndSortMessages(
		oldList: List<Message>,
		newList: List<Message>
	): List<Message> {
		val messageMap = mutableMapOf<Long, Message>()
		oldList.associateByTo(messageMap) { it.id }
		newList.associateByTo(messageMap) { it.id }
		return messageMap.values.sortedBy { it.timestamp }
	}

}