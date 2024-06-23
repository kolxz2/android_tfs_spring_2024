package ru.nikolas_snek.another_people.data

import ru.nikolas_snek.another_people.domain.UserProfile
import ru.nikolas_snek.data.network.models.users.UserOnlineStatusDto
import ru.nikolas_snek.data.network.models.users.UserProfileDto
import ru.nikolas_snek.data.utils.UserStatus

object UserAnotherPeopleMapper {
    fun mappingDto(
        ownUserDto: UserProfileDto, ownUserStatusDto: UserOnlineStatusDto?
    ): UserProfile {
        return UserProfile(
            userPhoto = ownUserDto.userDto?.avatarUrl.orEmpty(),
            userStatus = UserStatus.fromString(ownUserStatusDto?.presenceDto?.aggregated?.status.orEmpty()),
            userName = ownUserDto.userDto?.fullName.orEmpty()
        )
    }
}