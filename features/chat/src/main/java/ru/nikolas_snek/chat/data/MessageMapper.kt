package ru.nikolas_snek.chat.data

import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.data.database.models.MessageEntity
import ru.nikolas_snek.data.database.models.ReactionEntity
import ru.nikolas_snek.data.network.models.messages.MessageEventDto
import ru.nikolas_snek.data.network.models.messages.MessageResponseDto
import ru.nikolas_snek.data.network.models.messages.ReactionDto
import javax.inject.Inject

class MessageMapper @Inject constructor(
    private val currentUserID: Int,
    private val userEmail: String,
) {
    private fun countReactions(reactions: List<ReactionDto>): List<Reaction> {
        val emojiMap = hashMapOf<String, Reaction>()

        reactions.forEach { reactionDto ->
            val isCurrentUserReaction = reactionDto.userId == currentUserID
            val reaction = emojiMap.getOrPut(reactionDto.emojiName) {
                Reaction(
                    emojiCode = reactionDto.emojiCode,
                    isUserSelect = false,
                    count = 0,
                    emojiName = reactionDto.emojiName
                )
            }
            emojiMap[reactionDto.emojiName] = reaction.copy(count = reaction.count + 1)

            if (isCurrentUserReaction) {
                emojiMap[reactionDto.emojiName] =
                    reaction.copy(isUserSelect = true, count = reaction.count + 1)
            }
        }

        return emojiMap.values.toList()
    }

    fun mappingMessageResponseDto(messageResponseDto: MessageResponseDto): List<Message> {
        return messageResponseDto.messages.map { messageDto ->
            Message(
                avatarUrl = messageDto.avatarUrl,
                client = messageDto.client,
                content = messageDto.content,
                id = messageDto.id,
                isMeMessage = userEmail == messageDto.senderEmail,
                reactions = countReactions(messageDto.reactions),
                senderEmail = messageDto.senderEmail,
                senderFullName = messageDto.senderFullName,
                senderId = messageDto.senderId,
                timestamp = messageDto.timestamp
            )
        }
    }

    fun mappingMessageEventDto(messageEventDto: MessageEventDto): Message {
        return Message(
            avatarUrl = messageEventDto.avatarUrl,
            client = messageEventDto.client,
            content = messageEventDto.content,
            id = messageEventDto.id,
            isMeMessage = userEmail == messageEventDto.senderEmail,
            reactions = countReactions(messageEventDto.reactions),
            senderEmail = messageEventDto.senderEmail,
            senderFullName = messageEventDto.senderFullName,
            senderId = messageEventDto.senderId,
            timestamp = messageEventDto.timestamp
        )
    }

    fun mappingEntitiesToMessage(
        messageEntity: MessageEntity,
        reactions: List<ReactionEntity>,
    ): Message {
        return Message(
            avatarUrl = messageEntity.avatarUrl,
            client = messageEntity.client,
            content = messageEntity.content,
            id = messageEntity.id,
            isMeMessage = userEmail == messageEntity.senderEmail,
            reactions = mappingEntitiesToReactions(reactions),
            senderEmail = messageEntity.senderEmail,
            senderFullName = messageEntity.senderFullName,
            senderId = messageEntity.senderId,
            timestamp = messageEntity.timestamp
        )
    }

    fun mappingMessageToEntity(
        messageEntity: Message,
        topicTitle: String,
        streamTitle: String,
    ): MessageEntity {
        return MessageEntity(
            avatarUrl = messageEntity.avatarUrl,
            client = messageEntity.client,
            content = messageEntity.content,
            id = messageEntity.id,
            isMeMessage = userEmail == messageEntity.senderEmail,
            senderEmail = messageEntity.senderEmail,
            senderFullName = messageEntity.senderFullName,
            senderId = messageEntity.senderId,
            timestamp = messageEntity.timestamp,
            streamTitle = streamTitle,
            topicTitle = topicTitle
        )
    }

    fun mappingReactionsToEntity(reaction: Reaction, messageId: Long): ReactionEntity {
        return ReactionEntity(
            emojiCode = reaction.emojiCode,
            isUserSelect = reaction.isUserSelect,
            count = reaction.count,
            emojiName = reaction.emojiName,
            messageId = messageId,
        )

    }

    private fun mappingEntitiesToReactions(reactions: List<ReactionEntity>): List<Reaction> {
        return reactions.map {
            Reaction(
                emojiCode = it.emojiCode,
                isUserSelect = it.isUserSelect,
                count = it.count,
                emojiName = it.emojiName
            )
        }
    }

}
