package ru.nikolas_snek.another_people.domain

import ru.nikolas_snek.data.utils.UserStatus

data class UserProfile(
    val userName: String,
    val userStatus: UserStatus,
    val userPhoto: String
)
