package ru.nikolas_snek.channels.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nikolas_snek.channels.presentation.mvi.AllStreamsStoreFactory

@Module
interface AllStreamsModule {
	@Binds
	fun bindViewModelFactory(allStreamsStoreFactory: AllStreamsStoreFactory): ViewModelProvider.Factory
}

