package ru.nikolas_snek.chat.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ChatStoreFactory @Inject constructor(
    private val reducer: ChatReducer,
    private val actor: ChatActor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatStore(reducer, actor) as T
    }
}