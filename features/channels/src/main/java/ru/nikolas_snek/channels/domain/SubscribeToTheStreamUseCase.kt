package ru.nikolas_snek.channels.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubscribeToTheStreamUseCase(
    private val repository: ChannelsRepository
) {
    suspend operator fun invoke(streamTitle: String) = withContext(Dispatchers.IO) {
        repository.subscribeToTheStream(streamTitle)
    }
}