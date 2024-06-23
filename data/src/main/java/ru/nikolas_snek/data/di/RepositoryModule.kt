package ru.nikolas_snek.data.di

import dagger.Module
import dagger.Provides
import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.data.ZulipRemoteSourceImpl
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(impl: ZulipRemoteSourceImpl): ZulipRemoteSource {
        return impl
    }
}