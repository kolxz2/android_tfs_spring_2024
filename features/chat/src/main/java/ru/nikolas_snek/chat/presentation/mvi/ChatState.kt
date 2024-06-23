package ru.nikolas_snek.chat.presentation.mvi

import com.example.mvi.MviState
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.data.utils.LoadingAttemptStatus

data class ChatState(
	val loadingStatus: LoadingAttemptStatus = LoadingAttemptStatus.LOADING,
	val listProfiles: List<Message> = emptyList(),
) : MviState


