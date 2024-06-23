package ru.nikolas_snek.peoples.data

import ru.nikolas_snek.data.database.models.ProfileEntity
import ru.nikolas_snek.data.network.models.users.MemberDto
import ru.nikolas_snek.data.network.models.users.UserOnlineStatusDto
import ru.nikolas_snek.data.utils.UserStatus
import ru.nikolas_snek.peoples.domain.Profile

object ProfileMapper {
    fun mappingDto(
        profileDto: MemberDto,
        profileOnlineStatusDto: UserOnlineStatusDto?
    ): Profile {
        return Profile(
            photoUrl = profileDto.avatarUrl.orEmpty(),
            id = profileDto.userId,
            name = profileDto.fullName,
            email = profileDto.email,
            onlineStatus = UserStatus.fromString(profileOnlineStatusDto?.presenceDto?.aggregated?.status.orEmpty())
        )
    }

    fun mappingEntity(
        profileEntity: ProfileEntity
    ): Profile {
        return Profile(
            photoUrl = profileEntity.avatarUrl.orEmpty(),
            id = profileEntity.userId,
            name = profileEntity.fullName,
            email = profileEntity.email,
            onlineStatus = UserStatus.OFFLINE
        )
    }

    fun mappingDtoToEntity(
        profileDto: MemberDto
    ): ProfileEntity {
        return ProfileEntity(
            avatarUrl = profileDto.avatarUrl.orEmpty(),
            userId = profileDto.userId,
            fullName = profileDto.fullName,
            email = profileDto.email
        )
    }
}