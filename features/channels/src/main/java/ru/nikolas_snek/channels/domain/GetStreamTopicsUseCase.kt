package ru.nikolas_snek.channels.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nikolas_snek.channels.domain.models.Stream

class GetStreamTopicsUseCase(
    private val repository: ChannelsRepository
) {
    suspend operator fun invoke(stream: Stream) = withContext(Dispatchers.IO) {
        repository.getStreamTopics(stream)

    }
}