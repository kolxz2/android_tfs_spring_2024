package ru.nikolas_snek.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "reaction_table",
	foreignKeys = [ForeignKey(
		entity = MessageEntity::class,
		parentColumns = ["id"],
		childColumns = ["messageId"],
		onDelete = ForeignKey.CASCADE
	)],
	indices = [Index(value = ["messageId"])]
)
data class ReactionEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val emojiCode: String,
	val isUserSelect: Boolean,
	val count: Int,
	val emojiName: String,
	val messageId: Long,
)