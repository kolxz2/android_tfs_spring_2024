package ru.nikolas_snek.channels.presentation.subscribed.mvi

import com.example.mvi.MviState
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.SubscribedRecyclerItems
import ru.nikolas_snek.data.utils.LoadingAttemptStatus

data class SubscribedState(
	val loadingStatus: LoadingAttemptStatus = LoadingAttemptStatus.LOADING,
	val isLoadingTopics: Boolean = false,
	val dataLoaded: List<SubscribedRecyclerItems> = emptyList(),
	val searchQuery: String? = null
) : MviState