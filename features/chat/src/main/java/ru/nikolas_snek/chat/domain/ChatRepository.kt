package ru.nikolas_snek.chat.domain

import kotlinx.coroutines.flow.Flow
import ru.nikolas_snek.chat.domain.models.Message

interface ChatRepository {
	suspend fun sentMessage(stream: String, topic: String, message: String)

	suspend fun getTopicMessages(topicTitle: String, streamTitle: String): Flow<List<Message>>

	suspend fun setReaction(emojiName: String, messageId: Long)

	suspend fun removeReaction(emojiName: String, messageId: Long)

	suspend fun getOldTopicMessages(
		topicTitle: String, streamTitle: String, cashedMessagesList: List<Message>
	): Flow<List<Message>>

}