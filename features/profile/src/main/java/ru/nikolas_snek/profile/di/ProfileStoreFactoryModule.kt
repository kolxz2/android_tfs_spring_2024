package ru.nikolas_snek.profile.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nikolas_snek.profile.presentation.mvi.ProfileStoreFactory

@Module
interface ProfileStoreFactoryModule {
    @Binds
    fun bindViewModelFactory(profileStoreFactory: ProfileStoreFactory): ViewModelProvider.Factory

}