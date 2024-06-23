package ru.nikolas_snek.profile.data

import ru.nikolas_snek.data.network.models.users.OwnUserDto
import ru.nikolas_snek.data.network.models.users.UserOnlineStatusDto
import ru.nikolas_snek.data.utils.UserStatus
import ru.nikolas_snek.profile.domain.UserProfile

object UserProfileMapper {
    fun mappingDto(ownUserDto: OwnUserDto, ownUserStatusDto: UserOnlineStatusDto?): UserProfile {
        return UserProfile(
            userPhoto = ownUserDto.avatarUrl.orEmpty(),
            userStatus = UserStatus.fromString(ownUserStatusDto?.presenceDto?.aggregated?.status.orEmpty()),
            userName = ownUserDto.fullName.orEmpty()
        )
    }
}