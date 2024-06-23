package ru.nikolas_snek.channels.domain

import kotlinx.coroutines.flow.Flow
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.domain.models.Topic

interface ChannelsRepository {
    suspend fun getStreams(): Flow<List<Stream>>
    suspend fun getStreamTopics(stream: Stream): Flow<List<Topic>>
    suspend fun subscribeToTheStream(streamTitle: String)
    suspend fun unsubscribeFromTheStream(streamTitle: String)
}