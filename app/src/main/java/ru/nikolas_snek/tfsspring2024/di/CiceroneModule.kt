package ru.nikolas_snek.tfsspring2024.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides

@Module
class CiceroneModule {
    @Provides
    fun provideCicerone(): Cicerone<Router> {
        return Cicerone.create()
    }
}