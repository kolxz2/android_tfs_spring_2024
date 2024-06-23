package ru.nikolas_snek.chat.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nikolas_snek.chat.domain.models.Message

class GetOldTopicMessages(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(topicTitle: String, streamTitle: String, cashedMessagesList: List<Message>) =
		withContext(Dispatchers.IO) {
			chatRepository.getOldTopicMessages(
				topicTitle,
				streamTitle,
				cashedMessagesList
			)
		}
}