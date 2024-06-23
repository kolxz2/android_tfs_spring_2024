package ru.nikolas_snek.data

import ru.nikolas_snek.data.network.models.messages.MessageResponseDto
import ru.nikolas_snek.data.network.models.messages.RegisterTopicEventResponseDto
import ru.nikolas_snek.data.network.models.messages.TopicEventResponseDto
import ru.nikolas_snek.data.network.models.streams.AllStreamsResponseDto
import ru.nikolas_snek.data.network.models.streams.RegisterUnreadMessagesResponse
import ru.nikolas_snek.data.network.models.streams.StreamSubscriptionResult
import ru.nikolas_snek.data.network.models.streams.SubscribeStreamsResponse
import ru.nikolas_snek.data.network.models.streams.SubscriptionStreamsResponseDto
import ru.nikolas_snek.data.network.models.streams.TopicResponseDto
import ru.nikolas_snek.data.network.models.users.MembersResponseDto
import ru.nikolas_snek.data.network.models.users.OwnUserDto
import ru.nikolas_snek.data.network.models.users.UserOnlineStatusDto
import ru.nikolas_snek.data.network.models.users.UserProfileDto

interface ZulipRemoteSource {
    suspend fun getAllStreams(): AllStreamsResponseDto
    suspend fun getSubscribedStreams(): SubscriptionStreamsResponseDto
    suspend fun getStreamTopics(streamId: Int): TopicResponseDto
    suspend fun getOwnUser(): OwnUserDto
    suspend fun getPeoples(): MembersResponseDto
    suspend fun getUserOnlineStatus(userId: Int): UserOnlineStatusDto
    suspend fun getPeopleInfo(userId: Int): UserProfileDto
    suspend fun registerUnreadMessages(): RegisterUnreadMessagesResponse
    suspend fun registerSubscribeStreams(): SubscribeStreamsResponse

    suspend fun getChangesSubscribeStreams(
        queueId: String,
        lastEventId: Int
    ): StreamSubscriptionResult

    suspend fun subscribeToTheStream(streamTitle: String)
    suspend fun unsubscribeFromTheStream(streamTitle: String)

    suspend fun getInitialTopicMessages(topicTitle: String, streamTitle: String): MessageResponseDto
    suspend fun registerTopicEvent(
        topicTitle: String,
        streamTitle: String
    ): RegisterTopicEventResponseDto

    suspend fun getChangesTopicEvent(queueId: String, lastEventId: Int): TopicEventResponseDto
    suspend fun sendMessageToTopic(stream: String, topic: String, text: String)
    suspend fun setReaction(emojiName: String, messageId: Long)
    suspend fun removeAnEmojiReaction(emojiName: String, messageId: Long)
    suspend fun getOldTopicMessages(
        topicTitle: String,
        streamTitle: String,
        messageId: Long
    ): MessageResponseDto

    suspend fun getNewTopicMessages(
        topicTitle: String,
        streamTitle: String,
        messageId: Long
    ): MessageResponseDto
}