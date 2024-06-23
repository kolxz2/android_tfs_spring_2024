package ru.nikolas_snek.channels.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class SubscribedStoreFactory @Inject constructor(
    private val reducer: SubscribedReducer,
    private val actor: SubscribedActor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubscribedStore(reducer, actor) as T
    }
}