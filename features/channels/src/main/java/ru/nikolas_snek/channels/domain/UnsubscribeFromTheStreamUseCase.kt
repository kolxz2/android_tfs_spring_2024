package ru.nikolas_snek.channels.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnsubscribeFromTheStreamUseCase(
    private val repository: ChannelsRepository
) {
    suspend operator fun invoke(streamTitle: String) = withContext(Dispatchers.IO) {
        repository.unsubscribeFromTheStream(streamTitle)
    }
}