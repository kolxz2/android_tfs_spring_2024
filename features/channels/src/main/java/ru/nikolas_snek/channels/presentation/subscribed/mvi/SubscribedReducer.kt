package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviReducer
import ru.nikolas_snek.channels.domain.models.Topic
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedPartialState
import ru.nikolas_snek.channels.presentation.subscribed.mvi.SubscribedState
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.SubscribedRecyclerItems
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.SubscribedRecyclerItems.*
import ru.nikolas_snek.data.utils.LoadingAttemptStatus
import javax.inject.Inject

class SubscribedReducer @Inject constructor() :
	MviReducer<SubscribedPartialState, SubscribedState> {

	private fun modifierListRecyclerItem(
		oldState: SubscribedState,
		partialState: SubscribedPartialState.TopicLoaded
	): List<SubscribedRecyclerItems> {
		val stream = partialState.data.first
		val allStreamsList = oldState.dataLoaded.toMutableList()
		val streamItemIndex =
			allStreamsList.indexOfFirst { it is StreamItem && it.stream == stream }
		if (streamItemIndex != -1) {
			val foundStreamItem =
				allStreamsList[streamItemIndex] as StreamItem
			val newStreamItem =
				foundStreamItem.stream.copy(isExpanded = !foundStreamItem.stream.isExpanded)
			allStreamsList[streamItemIndex] = StreamItem(newStreamItem)
			if (stream.isExpanded) {
				removeStreamTopics(allStreamsList, streamItemIndex)
			} else {
				addTopicsToStream(
					partialState.data.second,
					allStreamsList,
					streamItemIndex
				)
			}
		}
		return allStreamsList.toList()
	}

	private fun removeStreamTopics(
		allStreamsList: MutableList<SubscribedRecyclerItems>,
		streamItemIndex: Int
	) {
		val endStreamIndex =
			allStreamsList.indexOfFirst {
				it is StreamItem &&
						allStreamsList.indexOf(it) > streamItemIndex
			}
		val finalEndStreamIndex = if (endStreamIndex == -1) {
			allStreamsList.size
		} else {
			endStreamIndex
		}
		val topicsToRemove =
			allStreamsList.subList(streamItemIndex + 1, finalEndStreamIndex)
				.filterIsInstance<TopicItem>()
		allStreamsList.removeAll(topicsToRemove)
	}

	private fun addTopicsToStream(
		profiles: List<Topic>,
		allStreamsList: MutableList<SubscribedRecyclerItems>,
		streamItemIndex: Int
	) {
		allStreamsList.addAll(
			streamItemIndex + 1,
			profiles.map { TopicItem(it) })
	}

	fun updateRecyclerItems(
		prevState: SubscribedState,
		partialState: SubscribedPartialState.StreamLoaded,
	): SubscribedState {
		val updatedStreams = partialState.data.filter { it.isSubscribed }
			.map { StreamItem(it.copy(isExpanded = false)) }
		val updatedStreamIds = updatedStreams.map { it.stream.streamId }.toSet()

		val existingStreamIds =
			prevState.dataLoaded.filterIsInstance<StreamItem>().map { it.stream.streamId }
		val newStreams = updatedStreams.filterNot { it.stream.streamId in existingStreamIds }

		val currentStreams = filterCurrentStreams(prevState, updatedStreamIds).toMutableList()

		integrateNewStreams(currentStreams, newStreams)

		return prevState.copy(
			loadingStatus = LoadingAttemptStatus.SUCCESS,
			isLoadingTopics = false,
			dataLoaded = currentStreams,
			searchQuery = partialState.searchQuery
		)
	}

	private fun filterCurrentStreams(
		prevState: SubscribedState,
		updatedStreamIds: Set<Int>
	): List<SubscribedRecyclerItems> {
		return prevState.dataLoaded.filter {
			when (it) {
				is StreamItem -> it.stream.streamId in updatedStreamIds
				is TopicItem -> prevState.dataLoaded.any { prevItem ->
					prevItem is StreamItem && prevItem.stream.title == it.topic.streamTitle && prevItem.stream.streamId in updatedStreamIds
				}

				else -> true
			}
		}
	}

	private fun integrateNewStreams(
		currentStreams: MutableList<SubscribedRecyclerItems>,
		newStreams: List<StreamItem>
	) {
		newStreams.forEach { newStream ->
			val position =
				currentStreams.indexOfLast { it is StreamItem && it.stream.streamId < newStream.stream.streamId }
			val insertPosition = findInsertPosition(currentStreams, position)
			currentStreams.add(insertPosition, newStream)
		}
	}

	private fun findInsertPosition(
		currentStreams: MutableList<SubscribedRecyclerItems>,
		position: Int
	): Int {
		var insertPosition = position + 1
		while (insertPosition < currentStreams.size && currentStreams[insertPosition] is TopicItem) {
			insertPosition++
		}
		return insertPosition
	}

	override fun reduce(
		prevState: SubscribedState,
		partialState: SubscribedPartialState
	): SubscribedState {
		return when (partialState) {
			SubscribedPartialState.Error -> prevState.copy(
				loadingStatus = if (prevState.loadingStatus == LoadingAttemptStatus.FIRST_ATTEMPT) LoadingAttemptStatus.FIRST_ATTEMPT else LoadingAttemptStatus.FIRST_ATTEMPT
			)

			SubscribedPartialState.LoadingStreams -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.LOADING,
				isLoadingTopics = false,
			)

			SubscribedPartialState.NoData -> prevState
			is SubscribedPartialState.StreamLoaded -> updateRecyclerItems(prevState, partialState)

			is SubscribedPartialState.TopicLoaded -> {
				prevState.copy(
					loadingStatus = LoadingAttemptStatus.SUCCESS,
					isLoadingTopics = false,
					dataLoaded = modifierListRecyclerItem(
						prevState,
						partialState
					),
				)

			}

			SubscribedPartialState.LoadingTopics -> prevState.copy(
				loadingStatus = LoadingAttemptStatus.SUCCESS,
				isLoadingTopics = true

			)
		}
	}
}