package ru.nikolas_snek.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import ru.nikolas_snek.data.network.models.users.OwnUserDto
import ru.nikolas_snek.data.network.models.users.PresenceDto
import ru.nikolas_snek.data.network.models.users.PresenceStatusDto
import ru.nikolas_snek.data.network.models.users.UserOnlineStatusDto
import ru.nikolas_snek.profile.data.OwnUserRepositoryImpl

class OwnUserRepositoryImplTest {

    @Mock
    private lateinit var zulipRemoteSource: ZulipRemoteSource

    private lateinit var ownUserRepository: OwnUserRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        ownUserRepository = OwnUserRepositoryImpl(zulipRemoteSource)
    }

    @Test
    fun `test getUserProfile returns correct UserProfile`() = runBlocking {
        val ownUserDto = OwnUserDto(
            avatarUrl = "https://example.com/avatar.jpg",
            avatarVersion = 1,
            dateJoined = "2021-01-01",
            deliveryEmail = "user@example.com",
            email = "user@example.com",
            fullName = "John Doe",
            isActive = true,
            isAdmin = true,
            isBillingAdmin = false,
            isBot = false,
            isGuest = false,
            isOwner = true,
            maxMessageId = 123,
            msg = "Success",
            result = "success",
            role = 2,
            timezone = "UTC",
            userId = 1
        )
        val userOnlineStatusDto = UserOnlineStatusDto(
            result = "success",
            msg = "online status retrieved",
            presenceDto = PresenceDto(
                aggregated = PresenceStatusDto(
                    status = "active",
                    timestamp = 1622548800L
                ),
                website = PresenceStatusDto(
                    status = "active",
                    timestamp = 1622548800L
                )
            )
        )
        whenever(zulipRemoteSource.getOwnUser()).thenReturn(ownUserDto)
        whenever(zulipRemoteSource.getUserOnlineStatus(1)).thenReturn(userOnlineStatusDto)
        val userProfile = ownUserRepository.getUserProfile()
        assertEquals("https://example.com/avatar.jpg", userProfile.userPhoto)
        assertEquals("active", userProfile.userStatus)
        assertEquals("John Doe", userProfile.userName)
    }

}