package ru.nikolas_snek.profile.di

import dagger.Binds
import dagger.Module
import ru.nikolas_snek.profile.data.OwnUserRepositoryImpl
import ru.nikolas_snek.profile.domain.OwnUserRepository

@Module
interface OwnUserRepositoryModule {
    @Binds
    fun bindRepository(impl: OwnUserRepositoryImpl): OwnUserRepository
}