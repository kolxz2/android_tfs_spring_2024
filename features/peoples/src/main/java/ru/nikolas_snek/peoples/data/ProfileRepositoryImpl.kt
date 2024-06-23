package ru.nikolas_snek.peoples.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.data.database.ZulipDao
import ru.nikolas_snek.data.utils.UserStatus
import ru.nikolas_snek.peoples.domain.Profile
import ru.nikolas_snek.peoples.domain.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
	private val zulipRemoteSource: ZulipRemoteSource,
	private val zulipDao: ZulipDao
) : ProfileRepository {
	override suspend fun getUserList(): Flow<List<Profile>> = flow {
		val databaseMembers = zulipDao.getMembers()
		if (databaseMembers.isNotEmpty()) {
			val rawMembersData = databaseMembers.map {
				ProfileMapper.mappingEntity(it)
			}
			emit(sortMembersByStatusAndName(rawMembersData))
		}
		while (true) {
			val profileList = arrayListOf<Profile>()
			val peoplesDto = zulipRemoteSource.getPeoples()
			for (people in peoplesDto.members) {
				val profileOnlineStatus = try {
					zulipRemoteSource.getUserOnlineStatus(people.userId)
				} catch (_: HttpException) {
					null
				}
				profileList.add(ProfileMapper.mappingDto(people, profileOnlineStatus))
				zulipDao.insertMember(ProfileMapper.mappingDtoToEntity(people))
			}
			emit(sortMembersByStatusAndName(profileList))
			delay(TIME_INTERVAL)
		}
	}

	private fun sortMembersByStatusAndName(listToSort: List<Profile>): List<Profile> {
		return listToSort.sortedWith(compareBy<Profile> {
			when (it.onlineStatus) {
				UserStatus.ACTIVE -> 0
				UserStatus.IDLE -> 1
				UserStatus.OFFLINE -> 2
			}
		}.thenBy {
			it.name
		})
	}

	companion object {
		private const val TIME_INTERVAL = 60000L
	}
}