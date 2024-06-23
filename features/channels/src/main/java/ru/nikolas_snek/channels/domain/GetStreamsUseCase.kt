package ru.nikolas_snek.channels.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetStreamsUseCase(
    private val repository: ChannelsRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository.getStreams()
    }
}