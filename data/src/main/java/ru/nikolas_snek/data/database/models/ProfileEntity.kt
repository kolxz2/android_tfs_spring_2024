package ru.nikolas_snek.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members_profile")
data class ProfileEntity(
    @PrimaryKey
    val userId: Int,
    val email: String,
    val fullName: String,
    val avatarUrl: String?
)