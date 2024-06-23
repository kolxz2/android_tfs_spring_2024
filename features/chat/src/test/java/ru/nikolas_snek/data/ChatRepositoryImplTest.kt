import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import ru.nikolas_snek.chat.data.ChatRepositoryImpl
import ru.nikolas_snek.data.ZulipRemoteSource
import ru.nikolas_snek.data.database.ZulipDao

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImplTest {
    @Mock
    private lateinit var zulipRemoteSource: ZulipRemoteSource

    @Mock
    private lateinit var zulipDao: ZulipDao

    private lateinit var chatRepository: ChatRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        chatRepository = ChatRepositoryImpl(zulipRemoteSource, zulipDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sentMessage should call sendMessageToTopic`() = runTest {
        chatRepository.sentMessage("stream", "topic", "message")
        verify(zulipRemoteSource).sendMessageToTopic("stream", "topic", "message")
    }

    @Test
    fun `setReaction should call setReaction in appRepository`() = runTest {
        chatRepository.setReaction("emojiName", 123L)
        verify(zulipRemoteSource).setReaction("emojiName", 123L)
    }

    @Test
    fun `removeReaction should call removeAnEmojiReaction in appRepository`() = runTest {
        chatRepository.removeReaction("emojiName", 123L)
        verify(zulipRemoteSource).removeAnEmojiReaction("emojiName", 123L)
    }



}
