package ru.nikolas_snek.chat.presentation.mvi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.nikolas_snek.chat.domain.ChatRepository
import ru.nikolas_snek.chat.domain.models.Message

@OptIn(ExperimentalCoroutinesApi::class)
class ChatActorTest {

	private lateinit var repository: ChatRepository
	private lateinit var serverCommunicationScope: CoroutineScope
	private lateinit var chatActor: ChatActor
	private val testDispatcher = StandardTestDispatcher()
	private val testScope = TestScope(testDispatcher)

	@Before
	fun setUp() {
		MockitoAnnotations.openMocks(this)
		repository = mock(ChatRepository::class.java)
		serverCommunicationScope = CoroutineScope(testDispatcher)
		chatActor = ChatActor(repository, serverCommunicationScope)
	}


	@Test
	fun `resolve GetOldTopicMessages intent`() = testScope.runTest {
		val intent = ChatIntent.GetOldTopicMessages("topicTitle", "streamTitle")
		val state = ChatState(listProfiles = emptyList())
		val messages = listOf(Message("avatarUrl", "client", "content", 1L, false, emptyList(), "email", "name", 1, 1000L))

		`when`(repository.getOldTopicMessages(anyString(), anyString(), anyList())).thenReturn(flowOf(messages))

		val flow = chatActor.resolve(intent, state).toList()

		assertTrue(flow.isNotEmpty())
		assertTrue(flow.first() is ChatPartialState.DataLoaded)
	}

	@Test
	fun `resolve NavigateToGoBack intent`() = testScope.runTest {
		val intent = ChatIntent.NavigateToGoBack
		val state = ChatState()

		val flow = chatActor.resolve(intent, state).toList()

		assertEquals(ChatPartialState.NoData, flow.first())
	}

	@Test
	fun `resolve Reload intent`() = testScope.runTest {
		val intent = ChatIntent.Reload("topicTitle", "streamTitle")
		val state = ChatState()
		val messages = listOf(Message("avatarUrl", "client", "content", 1L, false, emptyList(), "email", "name", 1, 1000L))

		`when`(repository.getTopicMessages(anyString(), anyString())).thenReturn(flowOf(messages))

		val flow = chatActor.resolve(intent, state).toList()

		assertTrue(flow.isNotEmpty())
		assertTrue(flow.first() is ChatPartialState.DataLoaded)
	}

	@Test
	fun `resolve RemoveReaction intent`() = testScope.runTest {
		val intent = ChatIntent.RemoveReaction("emojiName", 1L)
		val state = ChatState()

		val flow = chatActor.resolve(intent, state).toList()

		assertEquals(ChatPartialState.NoData, flow.first())
	}

	@Test
	fun `resolve SendMessage intent`() = testScope.runTest {
		val intent = ChatIntent.SendMessage("topic", "stream", "message")
		val state = ChatState()

		val flow = chatActor.resolve(intent, state).toList()

		assertEquals(ChatPartialState.NoData, flow.first())
	}

	@Test
	fun `resolve SetReaction intent`() = testScope.runTest {
		val intent = ChatIntent.SetReaction("emojiName", 1L)
		val state = ChatState()

		val flow = chatActor.resolve(intent, state).toList()

		assertEquals(ChatPartialState.NoData, flow.first())
	}

	@Test
	fun `resolve StartListeningProfileListChanges intent`() = testScope.runTest {
		val intent = ChatIntent.StartListeningProfileListChanges("topicTitle", "streamTitle")
		val state = ChatState()
		val messages = listOf(Message("avatarUrl", "client", "content", 1L, false, emptyList(), "email", "name", 1, 1000L))

		`when`(repository.getTopicMessages(anyString(), anyString())).thenReturn(flowOf(messages))

		val flow = chatActor.resolve(intent, state).toList()

		assertTrue(flow.isNotEmpty())
		assertTrue(flow.first() is ChatPartialState.DataLoaded)
	}

	@Test
	fun `resolve StopListeningProfileListChanges intent`() = testScope.runTest {
		val intent = ChatIntent.StopListeningProfileListChanges
		val state = ChatState()

		val flow = chatActor.resolve(intent, state).toList()

		assertEquals(ChatPartialState.NoData, flow.first())
	}
}
