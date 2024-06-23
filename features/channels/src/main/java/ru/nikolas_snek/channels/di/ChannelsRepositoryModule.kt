package ru.nikolas_snek.channels.di

import dagger.Binds
import dagger.Module
import ru.nikolas_snek.channels.data.ChannelsRepositoryImpl
import ru.nikolas_snek.channels.domain.ChannelsRepository

@Module
interface ChannelsRepositoryModule {
	@Binds
	fun bindChannelsRepository(channelsRepository: ChannelsRepositoryImpl): ChannelsRepository
}