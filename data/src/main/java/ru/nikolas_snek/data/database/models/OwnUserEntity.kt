package ru.nikolas_snek.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "own_user")
data class OwnUserEntity(
	@PrimaryKey
	val userId: Int,
	val email: String
)
