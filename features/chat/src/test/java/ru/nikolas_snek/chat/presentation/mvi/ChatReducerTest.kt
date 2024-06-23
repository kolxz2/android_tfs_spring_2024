package ru.nikolas_snek.chat.presentation.mvi

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.data.utils.LoadingAttemptStatus

class ChatReducerTest {

	private lateinit var chatReducer: ChatReducer

	@Before
	fun setUp() {
		chatReducer = ChatReducer()
	}

	@Test
	fun `reduce with DataLoaded state`() {
		val prevState = ChatState(
			loadingStatus = LoadingAttemptStatus.LOADING,
			listProfiles = listOf(
				Message("avatarUrl1", "client1", "content1", 1, false, emptyList(), "email1", "name1", 1, 1000)
			)
		)
		val newMessages = listOf(
			Message("avatarUrl2", "client2", "content2", 2, false, emptyList(), "email2", "name2", 2, 2000)
		)
		val partialState = ChatPartialState.DataLoaded(newMessages)

		val newState = chatReducer.reduce(prevState, partialState)

		val expectedState = prevState.copy(
			loadingStatus = LoadingAttemptStatus.SUCCESS,
			listProfiles = listOf(
				prevState.listProfiles[0],
				newMessages[0]
			).sortedBy { it.timestamp }
		)

		assertEquals(expectedState, newState)
	}

	@Test
	fun `reduce with Error state`() {
		val prevState = ChatState(
			loadingStatus = LoadingAttemptStatus.FIRST_ATTEMPT,
			listProfiles = emptyList()
		)
		val partialState = ChatPartialState.Error

		val newState = chatReducer.reduce(prevState, partialState)

		val expectedState = prevState.copy(
			loadingStatus = LoadingAttemptStatus.FAILURE
		)

		assertEquals(expectedState, newState)
	}

	@Test
	fun `reduce with NoData state`() {
		val prevState = ChatState(
			loadingStatus = LoadingAttemptStatus.LOADING,
			listProfiles = emptyList()
		)
		val partialState = ChatPartialState.NoData

		val newState = chatReducer.reduce(prevState, partialState)

		assertEquals(prevState, newState)
	}

	@Test
	fun `reduce with StartLoading state`() {
		val prevState = ChatState(
			loadingStatus = LoadingAttemptStatus.FAILURE,
			listProfiles = emptyList()
		)
		val partialState = ChatPartialState.StartLoading

		val newState = chatReducer.reduce(prevState, partialState)

		val expectedState = prevState.copy(
			loadingStatus = LoadingAttemptStatus.LOADING
		)

		assertEquals(expectedState, newState)
	}
}
