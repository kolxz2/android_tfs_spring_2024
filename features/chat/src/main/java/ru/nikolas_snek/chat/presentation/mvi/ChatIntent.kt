package ru.nikolas_snek.chat.presentation.mvi

import com.example.mvi.MviIntent

sealed interface ChatIntent : MviIntent {

	data class GetOldTopicMessages(
		val topicTitle: String,
		val streamTitle: String
	) : ChatIntent

	data class GetNewTopicMessages(
		val topicTitle: String,
		val streamTitle: String,
	) : ChatIntent

	data class Reload(val topicTitle: String, val streamTitle: String) : ChatIntent
	data object StopListeningProfileListChanges : ChatIntent
	data class StartListeningProfileListChanges(val topicTitle: String, val streamTitle: String) :
		ChatIntent

	data object NavigateToGoBack : ChatIntent

	data class SendMessage(val stream: String, val topic: String, val message: String) : ChatIntent

	data class SetReaction(val emojiName: String, val messageId: Long) : ChatIntent
	data class RemoveReaction(val emojiName: String, val messageId: Long) : ChatIntent
}