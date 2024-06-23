package ru.nikolas_snek.channels.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.nikolas_snek.channels.domain.ChannelsRepository
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.domain.models.Topic
import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.data.database.ZulipDao
import javax.inject.Inject

class ChannelsRepositoryImpl @Inject constructor(
	private val zulipRemoteSource: ZulipRemoteSource,
	private val zulipDao: ZulipDao
) : ChannelsRepository {
	companion object {
		const val RESPONSE_TYPE_SUBSCRIPTION = "subscription"
		const val RESPONSE_OPERATION_REMOVE = "remove"
		const val RESPONSE_OPERATION_ADD = "add"
	}

	override suspend fun getStreamTopics(stream: Stream): Flow<List<Topic>> = channelFlow {
		zulipDao.getTopic(stream.streamId).takeIf { it.isNotEmpty() }?.let {
			send(
				ChannelsMapper.mappingTopicEntityResponse(it)
					.sortedBy { topic: Topic -> topic.title })
		}
		val response = zulipRemoteSource.getStreamTopics(stream.streamId)
		val unreadMessagesResponse = zulipRemoteSource.registerUnreadMessages()
		val streamTopics =
			ChannelsMapper.mappingTopicResponseDto(response, stream, unreadMessagesResponse)
				.sortedBy { topic: Topic -> topic.title }
		send(streamTopics)
		streamTopics.map {
			withContext(Dispatchers.IO) {
				zulipDao.insertTopic(ChannelsMapper.mappingTopicToEntity(it, stream.streamId))
			}
		}
	}

	override suspend fun subscribeToTheStream(streamTitle: String) {
		zulipRemoteSource.subscribeToTheStream(streamTitle)
	}

	override suspend fun unsubscribeFromTheStream(streamTitle: String) {
		zulipRemoteSource.unsubscribeFromTheStream(streamTitle)
	}

	override suspend fun getStreams(): Flow<List<Stream>> = flow {
		zulipDao.getStreams().takeIf { it.isNotEmpty() }?.let {
			emit(
				ChannelsMapper.mappingEntityStreamsResponse(it)
					.sortedBy { stream: Stream -> stream.title }
			)
		}
		val initResponse = zulipRemoteSource.registerSubscribeStreams()
		val queueId = initResponse.queueId
		val lastEventId = -1
		val listStreams = ChannelsMapper.mappingSubscribeStreamsResponse(initResponse)
			.sortedBy { stream: Stream -> stream.title }
		emit(listStreams)
		updateStreamDatabase(listStreams)

		startLongPolling(queueId, lastEventId, listStreams).collect {
			emit(it)
		}
	}

	private suspend fun startLongPolling(
		queueId: String,
		initialEventId: Int,
		initialListStreams: List<Stream>
	): Flow<List<Stream>> = flow {
		var lastEventId = initialEventId
		var listStreams = initialListStreams

		while (true) {
			val response =
				zulipRemoteSource.getChangesSubscribeStreams(queueId, lastEventId).events.first()
			if (response.type == RESPONSE_TYPE_SUBSCRIPTION) {
				if (response.operation == RESPONSE_OPERATION_REMOVE) {
					listStreams = listStreams.map { stream ->
						if (stream.streamId == response.subscriptions?.first()?.streamId) {
							stream.copy(isSubscribed = false)
						} else {
							stream
						}
					}
					emit(listStreams)
					updateStreamDatabase(listStreams)
				} else if (response.operation == RESPONSE_OPERATION_ADD) {
					listStreams = listStreams.map { stream ->
						val requestStream = response.subscriptions?.first()
						if (stream.streamId == requestStream?.streamId) {
							stream.copy(isSubscribed = true, color = requestStream.color!!)
						} else {
							stream
						}
					}
					emit(listStreams)
					updateStreamDatabase(listStreams)
				}
			}
			lastEventId++
		}
	}

	private suspend fun updateStreamDatabase(listStreams: List<Stream>) {
		withContext(Dispatchers.IO) {
			listStreams.map {
				zulipDao.insertStreams(ChannelsMapper.mappingStreamsToEntity(it))
			}
		}
	}
}