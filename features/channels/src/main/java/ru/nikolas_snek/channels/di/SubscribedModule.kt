package ru.nikolas_snek.channels.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nikolas_snek.channels.presentation.mvi.SubscribedStoreFactory

@Module
interface SubscribedModule {
	@Binds
	fun bindViewModelFactory(subscribedStoreFactory: SubscribedStoreFactory): ViewModelProvider.Factory
}