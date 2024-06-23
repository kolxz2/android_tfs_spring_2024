package ru.nikolas_snek.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.nikolas_snek.data.database.ZulipDao
import ru.nikolas_snek.data.database.models.ProfileEntity
import ru.nikolas_snek.data.network.models.users.MemberDto
import ru.nikolas_snek.data.network.models.users.MembersResponseDto
import ru.nikolas_snek.data.network.models.users.PresenceDto
import ru.nikolas_snek.data.network.models.users.PresenceStatusDto
import ru.nikolas_snek.data.network.models.users.UserOnlineStatusDto
import ru.nikolas_snek.data.utils.UserStatus
import ru.nikolas_snek.peoples.data.ProfileMapper
import ru.nikolas_snek.peoples.data.ProfileRepositoryImpl
import ru.nikolas_snek.peoples.domain.Profile

@ExperimentalCoroutinesApi
class ProfileRepositoryImplTest {

    private lateinit var zulipRemoteSource: ZulipRemoteSource
    private lateinit var zulipDao: ZulipDao
    private lateinit var profileRepository: ProfileRepositoryImpl
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        zulipRemoteSource = mock(ZulipRemoteSource::class.java)
        zulipDao = mock(ZulipDao::class.java)
        profileRepository = ProfileRepositoryImpl(zulipRemoteSource, zulipDao)
    }

    @Test
    fun `test getUserList with non-empty database`() = testScope.runTest {
        val mockMembers = listOf(
            ProfileEntity(1, "john@example.com", "John", "avatarUrl1"),
            ProfileEntity(2, "doe@example.com", "Doe", "avatarUrl2")
        )
        `when`(zulipDao.getMembers()).thenReturn(mockMembers)
        val expectedProfiles = mockMembers.map { ProfileMapper.mappingEntity(it) }
            .sortedWith(compareBy<Profile> {
                when (it.onlineStatus) {
                    UserStatus.ACTIVE -> 0
                    UserStatus.IDLE -> 1
                    UserStatus.OFFLINE -> 2
                }
            }.thenBy { it.name })

        val result = profileRepository.getUserList().first()

        assertEquals(expectedProfiles, result)
        verify(zulipDao, times(1)).getMembers()
    }



    @Test
    fun `test getUserList emits values at intervals`() = runTest {
        val zulipRemoteSource: ZulipRemoteSource = mockk()
        val zulipDao: ZulipDao = mockk()
        val TIME_INTERVAL = 60000L

        // Given
        coEvery { zulipDao.getMembers() } returns emptyList()
        coEvery { zulipDao.insertMember(any()) } returns Unit

        val memberDto = MemberDto(
            email = "test@example.com",
            userId = 1,
            avatarVersion = 1,
            isAdmin = false,
            isOwner = false,
            isGuest = false,
            isBillingAdmin = false,
            role = 1,
            isBot = false,
            fullName = "Test User",
            timezone = "UTC",
            isActive = true,
            dateJoined = "2021-01-01",
            avatarUrl = "http://example.com/avatar.png"
        )

        val membersResponseDto = MembersResponseDto(
            result = "success",
            msg = "",
            members = listOf(memberDto)
        )

        val presenceStatusDto = PresenceStatusDto(
            status = "active",
            timestamp = 1627847272L
        )

        val presenceDto = PresenceDto(
            aggregated = presenceStatusDto,
            website = presenceStatusDto
        )

        val userOnlineStatusDto = UserOnlineStatusDto(
            result = "success",
            msg = null,
            presenceDto = presenceDto
        )

        val profile = Profile(
            photoUrl = memberDto.avatarUrl.orEmpty(),
            id = memberDto.userId,
            name = memberDto.fullName,
            email = memberDto.email,
            onlineStatus = UserStatus.OFFLINE
        )
        val profileEntity = ProfileEntity(
            avatarUrl = memberDto.avatarUrl.orEmpty(),
            userId = memberDto.userId,
            fullName = memberDto.fullName,
            email = memberDto.email
        )

        mockkObject(ProfileMapper)

        coEvery { zulipRemoteSource.getPeoples() } returns membersResponseDto
        coEvery { zulipRemoteSource.getUserOnlineStatus(any()) } returns userOnlineStatusDto
        every { ProfileMapper.mappingDto(memberDto, userOnlineStatusDto) } returns profile
        every { ProfileMapper.mappingDtoToEntity(memberDto) } returns profileEntity

        profileRepository = ProfileRepositoryImpl(zulipRemoteSource, zulipDao)

        // When
        val flow = profileRepository.getUserList()

        val collectedProfiles = mutableListOf<List<Profile>>()
        val job = launch {
            flow.toList(collectedProfiles)
        }

        advanceTimeBy(TIME_INTERVAL * 2)

        // Then
        assert(collectedProfiles.size > 1)
        assertEquals(profile, collectedProfiles[1][0])
        coVerify(exactly = 2) { zulipRemoteSource.getPeoples() }
        coVerify(exactly = 2) { zulipDao.insertMember(profileEntity) }

        job.cancel()
    }
}
