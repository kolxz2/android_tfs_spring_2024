package ru.nikolas_snek.peoples.presentation.mvi

import com.example.mvi.MviState
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import ru.nikolas_snek.peoples.domain.Profile

data class PeoplesState(
	val loadingStatus: LoadingAttemptStatus = LoadingAttemptStatus.LOADING,
	val listProfiles: List<Profile> = emptyList(),
	val searchQuery: String? = null
) : MviState



