package ru.nikolas_snek.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nikolas_snek.data.database.models.MessageEntity
import ru.nikolas_snek.data.database.models.OwnUserEntity
import ru.nikolas_snek.data.database.models.ProfileEntity
import ru.nikolas_snek.data.database.models.ReactionEntity
import ru.nikolas_snek.data.database.models.StreamsEntity
import ru.nikolas_snek.data.database.models.TopicEntity

@Dao
interface ZulipDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertMember(profileEntity: ProfileEntity)

	@Query("SELECT * FROM members_profile")
	suspend fun getMembers(): List<ProfileEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertTopic(topic: TopicEntity): Long

	@Query("SELECT * FROM topics WHERE streamId = :streamId")
	suspend fun getTopic(streamId: Int): List<TopicEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertStreams(stream: StreamsEntity): Long

	@Query("SELECT * FROM streams")
	suspend fun getStreams(): List<StreamsEntity>

	@Query("SELECT * FROM message_table WHERE streamTitle = :streamTitle AND topicTitle = :topicTitle")
	suspend fun getMessagesByStreamAndTopic(
		streamTitle: String,
		topicTitle: String,
	): List<MessageEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertMessage(message: MessageEntity)

	@Query("DELETE FROM message_table WHERE streamTitle = :streamTitle AND topicTitle = :topicTitle")
	suspend fun deleteMessagesByStream(streamTitle: String, topicTitle: String)


	@Query("SELECT * FROM reaction_table WHERE messageId = :messageId")
	suspend fun getReactionsForMessage(messageId: Long): List<ReactionEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertReaction(reaction: ReactionEntity)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertUser(user: OwnUserEntity)

	@Query("SELECT * FROM own_user LIMIT 1")
	suspend fun getUserById(): OwnUserEntity?
}