package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviActor
import com.example.mvi.Switcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import ru.nikolas_snek.channels.domain.ChannelsRepository
import ru.nikolas_snek.channels.domain.GetStreamsUseCase
import ru.nikolas_snek.channels.domain.SubscribeToTheStreamUseCase
import ru.nikolas_snek.channels.domain.UnsubscribeFromTheStreamUseCase
import ru.nikolas_snek.channels.domain.models.Stream
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AllStreamsActor @Inject constructor(
	private val repository: ChannelsRepository,
	private val loadingStreamsScope: CoroutineScope,
) : MviActor<AllStreamsPartialState, AllStreamsIntent, AllStreamsState, AllStreamsEffect>() {

	private val switcher = Switcher()
	override suspend fun resolve(
		intent: AllStreamsIntent,
		state: AllStreamsState
	): Flow<AllStreamsPartialState> {
		return when (intent) {
			is AllStreamsIntent.ChangeSubscriptionStatus -> switcher.switchInternal {
				changeSubscriptionStatus(intent.stream)
				startFetchingStreams(searchQuery = state.searchQuery, state = state)
			}

			AllStreamsIntent.ReloadAllStreams -> switcher.switchInternal {
				startFetchingStreams(searchQuery = state.searchQuery, state = state)
			}

			is AllStreamsIntent.SearchQuery -> switcher.switchInternal {
				startFetchingStreams(searchQuery = intent.query, state = state)
			}

			AllStreamsIntent.StartListeningSubscribeChanges -> switcher.switchInternal {
				startFetchingStreams(searchQuery = state.searchQuery, state = state)
			}

			AllStreamsIntent.StopListeningSubscribeChanges -> {
				switcher.cancelInternal()
				flowOf(AllStreamsPartialState.NoData)
			}
		}
	}


	private fun startFetchingStreams(
		searchQuery: String? = null,
		state: AllStreamsState
	) = flow {
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
						AllStreamsPartialState.DataLoaded(
							subscribedStreams,
							searchQuery.orEmpty()
						)
					)
				}
		} catch (e: CancellationException) {
			throw e
		} catch (_: IOException) {
			if (state.listProfiles.isNotEmpty()) {
				_effects.emit(AllStreamsEffect.NetworkError)
			} else {
				emit(AllStreamsPartialState.Error)
			}
		} catch (_: Exception) {
			emit(AllStreamsPartialState.Error)
		}

	}


	private fun changeSubscriptionStatus(stream: Stream) {
		loadingStreamsScope.launch {
			try {
				if (stream.isSubscribed) {
					UnsubscribeFromTheStreamUseCase(repository).invoke(stream.title)
				} else {
					SubscribeToTheStreamUseCase(repository).invoke(stream.title)
				}
			} catch (e: CancellationException) {
				throw e
			} catch (_: Exception) {
				_effects.emit(AllStreamsEffect.NetworkError)
			}
		}
	}

	fun onDestroy() {
		loadingStreamsScope.cancel()
	}
}