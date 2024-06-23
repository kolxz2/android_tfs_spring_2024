package ru.nikolas_snek.peoples.domain

import ru.nikolas_snek.data.utils.UserStatus

data class Profile(
    val photoUrl: String,
    val id: Int,
    val name: String,
    val email: String,
    val onlineStatus: UserStatus
)
