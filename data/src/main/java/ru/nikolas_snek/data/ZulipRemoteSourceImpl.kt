package ru.nikolas_snek.data

import ru.nikolas_snek.data.network.ZulipAPI
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
import javax.inject.Inject

class ZulipRemoteSourceImpl @Inject constructor(
    private val apiService: ZulipAPI
) : ZulipRemoteSource {
    override suspend fun getAllStreams(): AllStreamsResponseDto {
        return apiService.getAllStreams()
    }

    override suspend fun registerUnreadMessages(): RegisterUnreadMessagesResponse {
        return apiService.registerUnreadMessages()
    }

    override suspend fun getSubscribedStreams(): SubscriptionStreamsResponseDto {
        return apiService.getSubscribedStreams()
    }

    override suspend fun getStreamTopics(streamId: Int): TopicResponseDto {
        return apiService.getStreamTopics(streamId)
    }

    override suspend fun registerSubscribeStreams(): SubscribeStreamsResponse {
        return apiService.registerSubscribeStreams()
    }

    override suspend fun getChangesSubscribeStreams(
        queueId: String,
        lastEventId: Int
    ): StreamSubscriptionResult {
        return apiService.getChangesSubscribeStreams(queueId, lastEventId)
    }

    override suspend fun getOwnUser(): OwnUserDto {
        return apiService.getOwnUser()
    }

    override suspend fun getUserOnlineStatus(userId: Int): UserOnlineStatusDto {
        return apiService.getUserOnlineStatus(userId)
    }

    override suspend fun getPeoples(): MembersResponseDto {

        return apiService.getUserProfiles()
    }

    override suspend fun getPeopleInfo(userId: Int): UserProfileDto {
        return apiService.getUserProfileInfo(userId)
    }

    override suspend fun subscribeToTheStream(streamTitle: String) {
        val formatString = String.format("[{\"name\": \"%s\"}]", streamTitle)
        return apiService.subscribeToTheStream(formatString)
    }

    override suspend fun unsubscribeFromTheStream(streamTitle: String) {
        val formatString = String.format("[\"%s\"]", streamTitle)
        return apiService.unsubscribeFromTheStream(formatString)
    }

    override suspend fun getInitialTopicMessages(
        topicTitle: String,
        streamTitle: String
    ): MessageResponseDto {
        val formatString =
            String.format("[[\"stream\", \"%s\"], [\"topic\", \"%s\"]]", streamTitle, topicTitle)
        return apiService.getInitialTopicMessages(narrow = formatString)
    }

    override suspend fun getOldTopicMessages(
        topicTitle: String,
        streamTitle: String,
        messageId: Long
    ): MessageResponseDto {
        val formatString =
            String.format("[[\"stream\", \"%s\"], [\"topic\", \"%s\"]]", streamTitle, topicTitle)
        return apiService.getOldTopicMessages(narrow = formatString, anchor = messageId)
    }

    override suspend fun getNewTopicMessages(
        topicTitle: String,
        streamTitle: String,
        messageId: Long
    ): MessageResponseDto {
        val formatString =
            String.format("[[\"stream\", \"%s\"], [\"topic\", \"%s\"]]", streamTitle, topicTitle)
        return apiService.getNewTopicMessages(narrow = formatString, anchor = messageId)
    }

    override suspend fun registerTopicEvent(
        topicTitle: String,
        streamTitle: String
    ): RegisterTopicEventResponseDto {
        val formatString =
            String.format("[[\"stream\", \"%s\"], [\"topic\", \"%s\"]]", streamTitle, topicTitle)
        return apiService.registerTopicEvent(narrow = formatString)
    }

    override suspend fun getChangesTopicEvent(
        queueId: String,
        lastEventId: Int
    ): TopicEventResponseDto {
        return apiService.getChangesTopicEvent(queueId, lastEventId)
    }

    override suspend fun sendMessageToTopic(stream: String, topic: String, text: String) {
        apiService.sendMessageToTopic(
            stream = stream,
            topic = topic,
            text = text
        )
    }

    override suspend fun setReaction(emojiName: String, messageId: Long) {
        apiService.setReaction(emojiName, messageId)
    }

    override suspend fun removeAnEmojiReaction(emojiName: String, messageId: Long) {
        apiService.removeAnEmojiReaction(emojiName, messageId)
    }
}