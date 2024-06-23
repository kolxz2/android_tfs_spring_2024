package ru.nikolas_snek.profile.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nikolas_snek.profile.presentation.ProfileStore
import javax.inject.Inject

class ProfileStoreFactory @Inject constructor(
    private val reducer: ProfileReducer,
    private val actor: ProfileActor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileStore(reducer, actor) as T
    }
}