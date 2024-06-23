package ru.nikolas_snek.chat.presentation.mvi

import com.example.mvi.MviActor
import com.example.mvi.Switcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import ru.nikolas_snek.chat.domain.ChatRepository
import ru.nikolas_snek.chat.domain.GetAllTopicMessages
import ru.nikolas_snek.chat.domain.GetOldTopicMessages
import ru.nikolas_snek.chat.domain.RemoveReactionUseCase
import ru.nikolas_snek.chat.domain.SentMessageUseCase
import ru.nikolas_snek.chat.domain.SetReactionUseCase
import ru.nikolas_snek.chat.domain.models.Message
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ChatActor @Inject constructor(
	private val repository: ChatRepository,
	private val serverCommunicationScope: CoroutineScope
) : MviActor<ChatPartialState, ChatIntent, ChatState, ChatEffect>() {
	private val switcher = Switcher()

	override suspend fun resolve(
		intent: ChatIntent,
		state: ChatState
	): Flow<ChatPartialState> {
		return when (intent) {
			is ChatIntent.GetNewTopicMessages -> switcher.switchInternal {
				getOldTopicMessages(
					intent.topicTitle,
					intent.streamTitle,
					state.listProfiles
				)
			}

			is ChatIntent.GetOldTopicMessages -> switcher.switchInternal {
				getOldTopicMessages(
					intent.topicTitle,
					intent.streamTitle,
					state.listProfiles
				)
			}

			ChatIntent.NavigateToGoBack -> {
				_effects.emit(ChatEffect.NavigateToGoBack)
				flowOf(ChatPartialState.NoData)
			}

			is ChatIntent.Reload -> switcher.switchInternal {
				startFetchingData(intent.topicTitle, intent.streamTitle)
			}

			is ChatIntent.RemoveReaction -> {
				removeReaction(emojiName = intent.emojiName, messageId = intent.messageId)
				flowOf(ChatPartialState.NoData)
			}

			is ChatIntent.SendMessage -> {
				sendMessage(topic = intent.topic, stream = intent.stream, message = intent.message)
				flowOf(ChatPartialState.NoData)
			}

			is ChatIntent.SetReaction -> {
				setReaction(emojiName = intent.emojiName, messageId = intent.messageId)
				flowOf(ChatPartialState.NoData)
			}

			is ChatIntent.StartListeningProfileListChanges -> switcher.switchInternal {
				startFetchingData(intent.topicTitle, intent.streamTitle)
			}

			ChatIntent.StopListeningProfileListChanges -> {
				switcher.cancelInternal()
				flowOf(ChatPartialState.NoData)
			}
		}
	}

	private fun sendMessage(stream: String, topic: String, message: String) {
		serverCommunicationScope.launch(Dispatchers.IO) {
			performAction { SentMessageUseCase(repository).invoke(stream, topic, message) }
		}
	}

	private fun setReaction(emojiName: String, messageId: Long) {
		serverCommunicationScope.launch(Dispatchers.IO) {
			performAction { SetReactionUseCase(repository).invoke(emojiName, messageId) }
		}
	}

	private fun removeReaction(emojiName: String, messageId: Long) {
		serverCommunicationScope.launch(Dispatchers.IO) {
			performAction { RemoveReactionUseCase(repository).invoke(emojiName, messageId) }
		}
	}

	private fun startFetchingData(topicTitle: String, streamTitle: String) = flow {
		performAction {
			GetAllTopicMessages(repository).invoke(topicTitle, streamTitle)
				.collect { newData ->
					emit(ChatPartialState.DataLoaded(newData))
				}
		}
	}

	private fun getOldTopicMessages(
		topicTitle: String,
		streamTitle: String,
		cashedMessagesList: List<Message>
	) = flow {
		performAction {
			GetOldTopicMessages(repository).invoke(topicTitle, streamTitle, cashedMessagesList)
				.collect { newData ->
					emit(ChatPartialState.DataLoaded(newData))
				}
		}
	}

	private suspend fun performAction(action: suspend () -> Unit) {
		try {
			action()
		} catch (e: CancellationException) {
			throw e
		} catch (_: Exception) {
			_effects.emit(ChatEffect.ErrorLoadMessages)
		}
	}
}