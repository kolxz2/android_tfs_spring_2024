package ru.nikolas_snek.another_people.domain

interface UserInfoRepository {
    suspend fun getUserInfo(userId: Int): UserProfile
}