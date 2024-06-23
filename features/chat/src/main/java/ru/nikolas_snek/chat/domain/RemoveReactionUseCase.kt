package ru.nikolas_snek.chat.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoveReactionUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(emojiName: String, messageId: Long) = withContext(Dispatchers.IO) {
        chatRepository.removeReaction(emojiName, messageId)
    }
}