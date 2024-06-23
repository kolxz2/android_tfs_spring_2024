package ru.nikolas_snek.peoples.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nikolas_snek.peoples.data.ProfileRepositoryImpl
import ru.nikolas_snek.peoples.domain.ProfileRepository
import ru.nikolas_snek.peoples.presentation.mvi.PeoplesStoreFactory

@Module
interface ProfileModule {
    @Binds
    fun bindProfileRepositoryImpl(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun bindViewModelFactory(profileStoreFactory: PeoplesStoreFactory): ViewModelProvider.Factory

}