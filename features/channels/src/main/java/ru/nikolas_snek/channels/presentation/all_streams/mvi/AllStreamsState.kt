package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviState
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.data.utils.LoadingAttemptStatus

data class AllStreamsState(
	val loadingStatus: LoadingAttemptStatus = LoadingAttemptStatus.LOADING,
	val listProfiles: List<Stream> = emptyList(),
	val searchQuery: String? = null
) : MviState


