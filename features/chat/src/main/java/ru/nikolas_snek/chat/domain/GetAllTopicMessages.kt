package ru.nikolas_snek.chat.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllTopicMessages(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(topicTitle: String, streamTitle: String) =
        withContext(Dispatchers.IO) {
            chatRepository.getTopicMessages(topicTitle, streamTitle)
        }
}

