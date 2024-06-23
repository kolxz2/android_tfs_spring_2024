package ru.nikolas_snek.peoples.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserList(): Flow<List<Profile>>
}