package ru.nikolas_snek.chat.presentation.mvi

import com.example.mvi.MviStore

class ChatStore(
	reducer: ChatReducer,
	actor: ChatActor
) : MviStore<ChatPartialState, ChatIntent, ChatState, ChatEffect>(
	reducer,
	actor
) {
	override fun createInitialState(): ChatState = ChatState()
}