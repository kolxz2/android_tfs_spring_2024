package ru.nikolas_snek.another_people.data

import ru.nikolas_snek.another_people.domain.UserInfoRepository
import ru.nikolas_snek.another_people.domain.UserProfile
import ru.nikolas_snek.data.ZulipRemoteSource
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
	private val zulipRemoteSource: ZulipRemoteSource
) : UserInfoRepository {
	override suspend fun getUserInfo(userId: Int): UserProfile {
		val ownUserDto = zulipRemoteSource.getPeopleInfo(userId)
		val ownUserStatusDto =
			ownUserDto.userDto?.userId?.let { zulipRemoteSource.getUserOnlineStatus(it) }
		return UserAnotherPeopleMapper.mappingDto(ownUserDto, ownUserStatusDto)
	}
}