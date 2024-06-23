package ru.nikolas_snek.profile.data

import retrofit2.HttpException
import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.profile.domain.OwnUserRepository
import ru.nikolas_snek.profile.domain.UserProfile
import javax.inject.Inject

class OwnUserRepositoryImpl @Inject constructor(
    private val zulipRemoteSource: ZulipRemoteSource
) : OwnUserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val ownUserDto = zulipRemoteSource.getOwnUser()
        val ownUserStatusDto = try {
            zulipRemoteSource.getUserOnlineStatus(ownUserDto.userId)
        } catch (e: HttpException) {
            null
        }
        return UserProfileMapper.mappingDto(ownUserDto, ownUserStatusDto)

    }
}