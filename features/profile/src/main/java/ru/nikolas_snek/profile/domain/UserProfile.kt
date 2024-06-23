package ru.nikolas_snek.profile.domain

import ru.nikolas_snek.data.utils.UserStatus

data class UserProfile(
    val userName: String,
    val userStatus: UserStatus,
    val userPhoto: String
)
