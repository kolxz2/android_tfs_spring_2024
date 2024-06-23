package ru.nikolas_snek.tfsspring2024.di

import dagger.Binds
import dagger.Module
import ru.nikolas_snek.navigation.AppScreens
import ru.nikolas_snek.tfsspring2024.AppScreensImpl

@Module
interface AppScreensModule {
    @Binds
    fun bindAppScreens(impl: AppScreensImpl): AppScreens
}