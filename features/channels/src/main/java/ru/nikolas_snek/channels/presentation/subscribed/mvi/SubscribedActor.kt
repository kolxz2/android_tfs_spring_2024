package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviActor
import com.example.mvi.Switcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import ru.nikolas_snek.channels.domain.ChannelsRepository
import ru.nikolas_snek.channels.domain.GetStreamTopicsUseCase
import ru.nikolas_snek.channels.domain.GetStreamsUseCase
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.presentation.mvi.SubscribedEffect.*
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedIntent
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedPartialState
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedState
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SubscribedActor @Inject constructor(
	private val repository: ChannelsRepository,
) : MviActor<SubscribedPartialState, SubscribedIntent, SubscribedState, SubscribedEffect>() {
	private val switcher = Switcher()

	override suspend fun resolve(
		intent: SubscribedIntent,
		state: SubscribedState
	): Flow<SubscribedPartialState> {
		return when (intent) {
			is SubscribedIntent.LoadStreamTopics -> fetchingStreamTopics(intent.stream, state)

			is SubscribedIntent.NavigateToTopicChat -> {
				_effects.emit(
					NavigateToTopicChat(
						streamTitle = intent.streamTitle,
						topicTitle = intent.topicTitle
					)
				)
				flowOf(SubscribedPartialState.NoData)
			}

			SubscribedIntent.ReloadSubscribed -> switcher.switchInternal {
				startFetchingStreams(searchQuery = state.searchQuery, state = state)
			}

			is SubscribedIntent.SearchQuery -> switcher.switchInternal {
				startFetchingStreams(intent.searchQuery, state)
			}

			SubscribedIntent.StartListeningSubscribeChanges -> switcher.switchInternal {
				startFetchingStreams(searchQuery = state.searchQuery, state = state)
			}

			SubscribedIntent.StopListeningSubscribeChanges -> {
				switcher.cancelInternal()
				flowOf(SubscribedPartialState.NoData)
			}
		}
	}

	private fun fetchingStreamTopics(stream: Stream, state: SubscribedState) = channelFlow {
		try {
			if (!stream.isExpanded) send(SubscribedPartialState.LoadingTopics)
			GetStreamTopicsUseCase(repository).invoke(stream).collect { streamTopics ->
				send(SubscribedPartialState.TopicLoaded(stream to streamTopics))
			}

		} catch (e: CancellationException) {
			throw e
		} catch (_: IOException) {
			if (state.dataLoaded.isNotEmpty()) {
				_effects.emit(NetworkError)
			} else {
				send(SubscribedPartialState.Error)
			}
		} catch (_: Exception) {
			send(SubscribedPartialState.Error)
		}
	}

	private fun startFetchingStreams(searchQuery: String? = null, state: SubscribedState) = flow {
		try {
			GetStreamsUseCase(repository).invoke()
				.collect { newData ->
					val subscribedStreams =
						searchQuery?.let {
							if (it.isNotBlank()) {
								newData.filter { profile ->
									profile.title.contains(
										searchQuery,
										ignoreCase = true
									)
								}
							} else {
								newData
							}
						} ?: newData
					emit(
						SubscribedPartialState.StreamLoaded(
							subscribedStreams,
							searchQuery.orEmpty()
						)
					)
				}
		} catch (e: CancellationException) {
			throw e
		} catch (_: IOException) {
			if (state.dataLoaded.isNotEmpty()) {
				_effects.emit(NetworkError)
			} else {
				emit(SubscribedPartialState.Error)
			}
		} catch (_: Exception) {
			emit(SubscribedPartialState.Error)
		}
	}
}