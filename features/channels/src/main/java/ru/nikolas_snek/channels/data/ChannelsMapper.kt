package ru.nikolas_snek.channels.data

import android.graphics.Color
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.domain.models.Topic
import ru.nikolas_snek.data.database.models.StreamsEntity
import ru.nikolas_snek.data.database.models.TopicEntity
import ru.nikolas_snek.data.network.models.streams.RegisterUnreadMessagesResponse
import ru.nikolas_snek.data.network.models.streams.SubscribeStreamsResponse
import ru.nikolas_snek.data.network.models.streams.TopicResponseDto

@OptIn(ExperimentalStdlibApi::class)
object ChannelsMapper {


	fun mappingSubscribeStreamsResponse(subscribeStreamsResponse: SubscribeStreamsResponse): List<Stream> {
		val DEFAULT_COLOR = "#${Color.GRAY.toHexString()}"
		val subscriptionStreams = subscribeStreamsResponse.subscriptionDto.map { stream ->
			Stream(
				streamId = stream.streamId,
				title = stream.name,
				color = stream.color,
				isSubscribed = true
			)
		}.toMutableSet()
		val unsubscribedStreams = subscribeStreamsResponse.unsubscribed?.map { stream ->
			Stream(
				streamId = stream.streamId,
				title = stream.name,
				color = stream.color
			)
		}
		val neverSubscribed = subscribeStreamsResponse.neverSubscribed?.map { stream ->
			Stream(
				streamId = stream.streamId,
				title = stream.name,
				color = DEFAULT_COLOR
			)
		}
		return subscriptionStreams.apply {
			addAll(unsubscribedStreams ?: emptyList())
			addAll(neverSubscribed ?: emptyList())
		}.toList()

	}


	fun mappingEntityStreamsResponse(streams: List<StreamsEntity>): List<Stream> {
		return streams.map { stream ->
			Stream(
				streamId = stream.streamId,
				title = stream.title,
				color = stream.color,
				isSubscribed = stream.isSubscribed
			)
		}
	}

	fun mappingStreamsToEntity(stream: Stream): StreamsEntity {
		return StreamsEntity(
			streamId = stream.streamId,
			title = stream.title,
			color = stream.color,
			isSubscribed = stream.isSubscribed
		)

	}

	fun mappingTopicResponseDto(
		topicResponseDto: TopicResponseDto,
		stream: Stream,
		registerUnreadMessagesResponse: RegisterUnreadMessagesResponse
	): List<Topic> {
		val streamTopics = registerUnreadMessagesResponse.unreadMsgsDto.streams
			.filter { topic -> topic.streamId == stream.streamId }
			.map { topic ->
				Topic(
					title = topic.topicTitle,
					streamTitle = stream.title,
					unreadMessages = topic.unreadMessageIds.size
				)
			}.toMutableList()

		topicResponseDto.topicsDto.forEach { topic ->
			streamTopics.firstOrNull { it.title == topic.name } ?: run {
				streamTopics.add(
					Topic(
						title = topic.name,
						streamTitle = stream.title
					)
				)
			}
		}
		return streamTopics
	}

	fun mappingTopicEntityResponse(
		topicEntity: List<TopicEntity>,
	): List<Topic> {
		return topicEntity.map { topic ->
			Topic(
				title = topic.title,
				streamTitle = topic.streamTitle,
				unreadMessages = topic.unreadMessages
			)
		}
	}

	fun mappingTopicToEntity(
		topics: Topic,
		streamId: Int
	): TopicEntity {
		return TopicEntity(
			title = topics.title,
			streamTitle = topics.streamTitle,
			unreadMessages = topics.unreadMessages,
			streamId = streamId
		)
	}


}