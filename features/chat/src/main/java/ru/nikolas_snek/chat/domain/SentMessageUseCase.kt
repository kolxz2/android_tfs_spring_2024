package ru.nikolas_snek.chat.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SentMessageUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(stream: String, topic: String, message: String) =
        withContext(Dispatchers.IO) {
            chatRepository.sentMessage(stream, topic, message)
        }
}