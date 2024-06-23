package ru.nikolas_snek.data.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
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

interface ZulipAPI {
	@GET("users/me")
	suspend fun getOwnUser(): OwnUserDto

	@GET("users")
	suspend fun getUserProfiles(): MembersResponseDto

	@GET("users/{userId}")
	suspend fun getUserProfileInfo(@Path("userId") userId: Int): UserProfileDto

	@GET("users/{userId}/presence")
	suspend fun getUserOnlineStatus(@Path("userId") userId: Int): UserOnlineStatusDto

	@GET("users/me/subscriptions")
	suspend fun getSubscribedStreams(): SubscriptionStreamsResponseDto

	@GET("streams")
	suspend fun getAllStreams(): AllStreamsResponseDto

	@GET("users/me/{streamId}/topics")
	suspend fun getStreamTopics(@Path("streamId") streamId: Int): TopicResponseDto

	@POST("register")
	@FormUrlEncoded
	suspend fun registerUnreadMessages(
		@Field("event_types") eventTypes: String = "[\"message\", \"update_message_flags\"]"
	): RegisterUnreadMessagesResponse

	@POST("register")
	@FormUrlEncoded
	suspend fun registerSubscribeStreams(
		@Field("event_types") eventTypes: String = "[\"subscription\"]"
	): SubscribeStreamsResponse

	@GET("events")
	suspend fun getChangesSubscribeStreams(
		@Query("queue_id") queueId: String,
		@Query("last_event_id") lastEventId: Int
	): StreamSubscriptionResult

	@POST("users/me/subscriptions")
	@FormUrlEncoded
	suspend fun subscribeToTheStream(
		@Field("subscriptions") subscriptions: String
	)

	@FormUrlEncoded
	@HTTP(method = "DELETE", path = "users/me/subscriptions", hasBody = true)
	suspend fun unsubscribeFromTheStream(
		@Field("subscriptions") subscriptions: String
	)

	@GET("messages")
	suspend fun getInitialTopicMessages(
		@Query("anchor") anchor: String = "newest",
		@Query("num_before") numBefore: Int = 20,
		@Query("num_after") numAfter: Int = 0,
		@Query("narrow") narrow: String
	): MessageResponseDto

	@GET("messages")
	suspend fun getOldTopicMessages(
		@Query("anchor") anchor: Long,
		@Query("num_before") numBefore: Int = 20,
		@Query("num_after") numAfter: Int = 0,
		@Query("narrow") narrow: String
	): MessageResponseDto

	@GET("messages")
	suspend fun getNewTopicMessages(
		@Query("anchor") anchor: Long,
		@Query("num_before") numBefore: Int = 0,
		@Query("num_after") numAfter: Int = 20,
		@Query("narrow") narrow: String
	): MessageResponseDto

	@POST("register")
	@FormUrlEncoded
	suspend fun registerTopicEvent(
		@Field("event_types") eventTypes: String = "[\"message\", \"reaction\"]",
		@Field("narrow") narrow: String
	): RegisterTopicEventResponseDto

	@GET("events")
	suspend fun getChangesTopicEvent(
		@Query("queue_id") queueId: String,
		@Query("last_event_id") lastEventId: Int
	): TopicEventResponseDto

	@POST("messages")
	@FormUrlEncoded
	suspend fun sendMessageToTopic(
		@Field("type") eventTypes: String = "stream",
		@Field("to") stream: String,
		@Field("topic") topic: String,
		@Field("content") text: String,
	)

	@POST("messages/{messageId}/reactions")
	@FormUrlEncoded
	suspend fun setReaction(
		@Field("emoji_name") emojiName: String,
		@Path("messageId") messageId: Long
	)

	@FormUrlEncoded
	@HTTP(method = "DELETE", path = "messages/{messageId}/reactions", hasBody = true)
	suspend fun removeAnEmojiReaction(
		@Field("emoji_name") emojiName: String,
		@Path("messageId") messageId: Long
	)
}