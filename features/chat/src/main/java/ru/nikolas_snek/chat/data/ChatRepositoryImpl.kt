package ru.nikolas_snek.chat.data

import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import ru.nikolas_snek.chat.domain.ChatRepository
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.data.database.ZulipDao
import ru.nikolas_snek.data.database.models.OwnUserEntity
import ru.nikolas_snek.data.network.models.messages.TopicEventDto
import ru.nikolas_snek.data.network.models.users.OwnUserDto
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
	private val zulipRemoteSource: ZulipRemoteSource,
	private val zulipDao: ZulipDao,
) : ChatRepository {
	private lateinit var mapper: MessageMapper
	private lateinit var userOwner: OwnUserDto


	override suspend fun sentMessage(stream: String, topic: String, message: String) {
		zulipRemoteSource.sendMessageToTopic(stream, topic, message)
	}

	override suspend fun getTopicMessages(
		topicTitle: String,
		streamTitle: String,
	): Flow<List<Message>> = channelFlow {
		var cashedMessagesList: List<Message> = emptyList()
		initRepository()
		cashedMessagesList = getDataFromDatabase(topicTitle, streamTitle)
		if (cashedMessagesList.isNotEmpty()) send(cashedMessagesList)
		updateRepository()
		val initRequest = zulipRemoteSource.getInitialTopicMessages(
			topicTitle = topicTitle, streamTitle = streamTitle
		)
		cashedMessagesList = mergeAndSortMessages(
			cashedMessagesList, mapper.mappingMessageResponseDto(initRequest)
		)

		saveToDatabase(
			sublist50Messages(cashedMessagesList),
			streamTitle = streamTitle,
			topicTitle = topicTitle
		)
		send(cashedMessagesList)
		startLongPoolingWork(
			userOwner, cashedMessagesList, topicTitle = topicTitle, streamTitle = streamTitle
		)
	}

	private suspend fun initRepository() {
		val userOwner = zulipDao.getUserById()
		userOwner?.let {
			mapper = MessageMapper(userOwner.userId, userOwner.email)
		} ?: updateRepository()

	}

	private suspend fun updateRepository() {
		userOwner = zulipRemoteSource.getOwnUser()
		mapper = MessageMapper(userOwner.userId, userOwner.email!!)
		zulipDao.insertUser(OwnUserEntity(userOwner.userId, userOwner.email!!))
	}

	private fun sublist50Messages(allMessages: List<Message>): List<Message> {
		return if (allMessages.size <= 50) {
			allMessages
		} else {
			allMessages.subList(allMessages.size - 50, allMessages.size)
		}
	}

	private suspend fun saveToDatabase(
		cashedMessagesList: List<Message>,
		streamTitle: String,
		topicTitle: String,
	) {
		zulipDao.deleteMessagesByStream(
			streamTitle = streamTitle, topicTitle = topicTitle
		)
		cashedMessagesList.forEach { message ->
			val messageEntity = mapper.mappingMessageToEntity(
				messageEntity = message, topicTitle = topicTitle, streamTitle = streamTitle
			)
			zulipDao.insertMessage(messageEntity)
			message.reactions.forEach { reaction ->
				zulipDao.insertReaction(
					mapper.mappingReactionsToEntity(
						reaction, message.id
					)
				)
			}
		}
	}

	private fun mergeAndSortMessages(
		oldList: List<Message>,
		newList: List<Message>,
	): List<Message> {
		val messageMap = mutableMapOf<Long, Message>()
		oldList.forEach { message ->
			messageMap[message.id] = message
		}
		newList.forEach { message ->
			messageMap[message.id] = message
		}
		return messageMap.values.sortedBy { it.timestamp }
	}

	private suspend fun getDataFromDatabase(
		topicTitle: String, streamTitle: String
	): List<Message> {
		return zulipDao.getMessagesByStreamAndTopic(streamTitle, topicTitle)
			.takeIf { it.isNotEmpty() }
			?.let { messageList ->
				messageList.map { message ->
					val reactions = zulipDao.getReactionsForMessage(message.id)
					mapper.mappingEntitiesToMessage(message, reactions)
				}.sortedBy { it.timestamp }
			} ?: emptyList()
	}

	private suspend fun ProducerScope<List<Message>>.startLongPoolingWork(
		userOwner: OwnUserDto,
		cashedMessagesList: List<Message>,
		topicTitle: String,
		streamTitle: String
	) {
		val initQueueId = zulipRemoteSource.registerTopicEvent(
			topicTitle = topicTitle, streamTitle = streamTitle
		).queueId
		val updatedCashedMessagesList = cashedMessagesList.toMutableList()
		var lastEvent1 = -1
		while (isActive) {
			val response = zulipRemoteSource.getChangesTopicEvent(initQueueId, lastEvent1)
			val event = response.events.first()
			if (event.type == EVENT_TYPE_MESSAGE) {
				event.message?.let {
					val message = mapper.mappingMessageEventDto(it)

					updatedCashedMessagesList += message
					val immutableCashedMessagesList = updatedCashedMessagesList.toList()
					send(immutableCashedMessagesList)
					saveToDatabase(
						sublist50Messages(immutableCashedMessagesList),
						streamTitle = streamTitle,
						topicTitle = topicTitle
					)
				}
			} else if (event.type == EVENT_TYPE_REACTION) {
				val messageIndexInList =
					updatedCashedMessagesList.indexOfFirst { it.id == event.messageId }
				if (messageIndexInList == -1) {
					lastEvent1++
					continue
				}
				val tempList =
					updatedCashedMessagesList[messageIndexInList].reactions.toMutableList()
				if (event.op == EVENT_TYPE_REACTION_ADD) {
					onAddReactionEvent(tempList, event, userOwner, tempList.size)
				} else if (event.op ==EVENT_TYPE_REACTION_REMOVE ) {
					onRemoveReactionEvent(tempList, event, userOwner, tempList.size)
				}
				val updatedMessage =
					updatedCashedMessagesList[messageIndexInList].copy(reactions = tempList)
				updatedCashedMessagesList[messageIndexInList] = updatedMessage
				val immutableCashedMessagesList = updatedCashedMessagesList.toList()
				send(immutableCashedMessagesList)
				saveToDatabase(
					sublist50Messages(immutableCashedMessagesList),
					streamTitle = streamTitle,
					topicTitle = topicTitle
				)
			}
			lastEvent1++
		}
	}

	private fun onRemoveReactionEvent(
		tempList: MutableList<Reaction>,
		event: TopicEventDto,
		userOwner: OwnUserDto,
		reactionIndex: Int,
	) {
		var newReaction: Reaction? = null
		var newReactionIndex = reactionIndex
		tempList.forEachIndexed { index, reaction ->
			if (reaction.emojiName == event.emojiName) {
				newReaction = reaction.copy(
					count = reaction.count - 1,
					isUserSelect = if (userOwner.userId == event.userId) !reaction.isUserSelect else reaction.isUserSelect
				)
				newReactionIndex = index
			}
		}
		if (newReaction?.count == 0) {
			tempList.removeAt(newReactionIndex)
		} else {
			tempList[newReactionIndex] = newReaction!!
		}
	}

	private fun onAddReactionEvent(
		tempList: MutableList<Reaction>,
		event: TopicEventDto,
		userOwner: OwnUserDto,
		reactionIndex: Int,
	) {
		var newReaction: Reaction? = null
		var newReactionIndex = reactionIndex
		tempList.forEachIndexed { index, reaction ->
			if (reaction.emojiName == event.emojiName) {
				newReaction = reaction.copy(
					count = reaction.count + 1, isUserSelect = userOwner.userId == event.userId
				)
				newReactionIndex = index
			}
		}
		if (newReaction == null) {
			newReaction = Reaction(
				emojiCode = event.emojiCode!!,
				isUserSelect = userOwner.userId == event.userId,
				count = 1,
				emojiName = event.emojiName!!
			)
		}
		if (newReactionIndex < tempList.size) {
			tempList[newReactionIndex] = newReaction
		} else {
			tempList.add(newReaction)
		}
	}

	override suspend fun setReaction(emojiName: String, messageId: Long) {
		zulipRemoteSource.setReaction(emojiName, messageId)
	}

	override suspend fun removeReaction(emojiName: String, messageId: Long) {
		zulipRemoteSource.removeAnEmojiReaction(emojiName, messageId)
	}

	override suspend fun getOldTopicMessages(
		topicTitle: String, streamTitle: String, cashedMessagesList: List<Message>
	): Flow<List<Message>> = channelFlow {
		val firstMessageId = cashedMessagesList.first().id
		val response =
			zulipRemoteSource.getOldTopicMessages(topicTitle, streamTitle, firstMessageId)
		updateRepository()
		val updatedCashedMessagesList = mergeAndSortMessages(
			mapper.mappingMessageResponseDto(response), cashedMessagesList
		)
		send(updatedCashedMessagesList)
		saveToDatabase(
			updatedCashedMessagesList,
			topicTitle = topicTitle,
			streamTitle = streamTitle
		)
		startLongPoolingWork(
			userOwner,
			updatedCashedMessagesList,
			topicTitle = topicTitle,
			streamTitle = streamTitle
		)
	}
	companion object{
		const val EVENT_TYPE_REACTION = "reaction"
		const val EVENT_TYPE_MESSAGE = "message"
		const val EVENT_TYPE_REACTION_ADD = "add"
		const val EVENT_TYPE_REACTION_REMOVE = "remove"
	}

}