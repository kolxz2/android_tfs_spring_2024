package ru.nikolas_snek.peoples.presentation.mvi

import com.example.mvi.MviActor
import com.example.mvi.Switcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import okio.IOException
import ru.nikolas_snek.peoples.domain.GetUserProfilesUseCase
import ru.nikolas_snek.peoples.domain.Profile
import ru.nikolas_snek.peoples.domain.ProfileRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


class PeoplesActor @Inject constructor(
	private val repositoryImpl: ProfileRepository
) : MviActor<PeoplesPartialState, PeoplesIntent, PeoplesState, PeoplesEffect>() {

	private val switcher = Switcher()


	override suspend fun resolve(
		intent: PeoplesIntent,
		state: PeoplesState
	): Flow<PeoplesPartialState> {
		return when (intent) {
			is PeoplesIntent.Reload -> switcher.switchInternal { startFetchingData(state = state) }
			is PeoplesIntent.SearchQuery -> switcher.switchInternal(1000) {
				startFetchingData(
					intent.query,
					state
				)
			}

			is PeoplesIntent.NavigateToUserPeoplesDetails -> flow {
				switcher.cancelInternal()
				_effects.emit(PeoplesEffect.NavigateToUserPeoplesDetails(intent.userId))
				emit(PeoplesPartialState.NoData)
			}

			PeoplesIntent.StopListeningPeoplesListChanges -> {
				switcher.cancelInternal()
				flowOf(PeoplesPartialState.NoData)
			}

			PeoplesIntent.StartListeningPeoplesListChanges -> switcher.switchInternal {
				startFetchingData(
					state = state
				)
			}
		}

	}

	private fun startFetchingData(
		searchQuery: String? = null,
		state: PeoplesState
	): Flow<PeoplesPartialState> {
		return flow {
			try {
				if (state.listProfiles.isNotEmpty()) {
					handleDataEmission(state, searchQuery, state.listProfiles)
				}

				fetchAndFilterProfiles(state, searchQuery)
					.let { flow ->
						if (shouldDropFirstResult(state))  flow.drop(1)  else flow
					}
					.collect { filteredProfiles ->
						emitFilteredProfiles(filteredProfiles, searchQuery)
					}
			} catch (e: CancellationException) {
				throw e
			} catch (_: IOException) {
				handleNetworkError(state)
			} catch (_: Exception) {
				emit(PeoplesPartialState.Error)
			}
		}
	}

	private suspend fun FlowCollector<PeoplesPartialState>.handleDataEmission(
		state: PeoplesState,
		searchQuery: String?,
		profiles: List<Profile>
	) {
		searchQuery?.let { query ->
			val filteredMembers = profiles.filter { it.name.contains(query, ignoreCase = true) }
			emit(PeoplesPartialState.DataLoadedWithSearch(filteredMembers, searchQuery))
		} ?: run {
			state.searchQuery?.let { query ->
				val filteredMembers = profiles.filter { it.name.contains(query, ignoreCase = true) }
				emit(PeoplesPartialState.DataLoadedWithSearch(filteredMembers, state.searchQuery))
			} ?: emit(PeoplesPartialState.DataLoaded(profiles))
		}
	}

	private fun shouldDropFirstResult(state: PeoplesState): Boolean {
		return state.listProfiles.isNotEmpty()
	}

	private suspend fun FlowCollector<PeoplesPartialState>.fetchAndFilterProfiles(
		state: PeoplesState,
		searchQuery: String?
	): Flow<List<Profile>> {
		return GetUserProfilesUseCase(repositoryImpl).invoke()
			.map { profiles ->
				searchQuery?.let { query ->
					profiles.filter { it.name.contains(query, ignoreCase = true) }
				} ?: run {
					state.searchQuery?.let { query ->
						state.listProfiles.filter { it.name.contains(query, ignoreCase = true) }
					} ?: profiles
				}
			}
	}

	private suspend fun FlowCollector<PeoplesPartialState>.emitFilteredProfiles(
		filteredProfiles: List<Profile>,
		searchQuery: String?
	) {
		searchQuery?.let { query ->
			emit(PeoplesPartialState.DataLoadedWithSearch(filteredProfiles, query))
		} ?: emit(PeoplesPartialState.DataLoaded(filteredProfiles))
	}

	private suspend fun FlowCollector<PeoplesPartialState>.handleNetworkError(state: PeoplesState) {
		if (state.listProfiles.isNotEmpty()) {
			_effects.emit(PeoplesEffect.NetworkError)
		} else {
			emit(PeoplesPartialState.Error)
		}
	}
}