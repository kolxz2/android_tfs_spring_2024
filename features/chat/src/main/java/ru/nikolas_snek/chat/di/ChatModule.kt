package ru.nikolas_snek.chat.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nikolas_snek.chat.data.ChatRepositoryImpl
import ru.nikolas_snek.chat.domain.ChatRepository
import ru.nikolas_snek.chat.presentation.mvi.ChatStoreFactory

@Module
interface ChatModule {
    @Binds
    fun bindViewModelFactory(chatStoreFactory: ChatStoreFactory): ViewModelProvider.Factory

    @Binds
    fun chatRepository(chatRepository: ChatRepositoryImpl): ChatRepository
}