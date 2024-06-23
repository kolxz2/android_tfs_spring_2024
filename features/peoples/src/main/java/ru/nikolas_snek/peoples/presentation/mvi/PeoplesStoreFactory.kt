package ru.nikolas_snek.peoples.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class PeoplesStoreFactory @Inject constructor(
    private val reducer: PeoplesReducer,
    private val actor: PeoplesActor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PeoplesStore(reducer, actor) as T
    }
}