package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviEffect

sealed interface SubscribedEffect : MviEffect {
	data class NavigateToTopicChat(val streamTitle: String, val topicTitle: String) :
		SubscribedEffect

	data object NetworkError : SubscribedEffect

}