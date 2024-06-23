package ru.nikolas_snek.channels.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class AllStreamsStoreFactory @Inject constructor(
    private val reducer: AllStreamsReducer,
    private val actor: AllStreamsActor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllStreamsStore(reducer, actor) as T
    }
}