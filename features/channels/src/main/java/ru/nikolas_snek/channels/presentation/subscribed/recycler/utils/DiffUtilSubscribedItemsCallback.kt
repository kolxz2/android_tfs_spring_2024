package ru.nikolas_snek.channels.presentation.subscribed.recycler.utils

import androidx.recyclerview.widget.DiffUtil

class DiffUtilSubscribedItemsCallback : DiffUtil.ItemCallback<SubscribedRecyclerItems>() {
	override fun areItemsTheSame(
		oldItem: SubscribedRecyclerItems,
		newItem: SubscribedRecyclerItems
	): Boolean {
		return when {
			oldItem is SubscribedRecyclerItems.StreamItem && newItem is SubscribedRecyclerItems.StreamItem -> oldItem.stream.streamId == newItem.stream.streamId
			oldItem is SubscribedRecyclerItems.TopicItem && newItem is SubscribedRecyclerItems.TopicItem -> oldItem.topic.title == newItem.topic.title
			else -> false
		}
	}

	override fun areContentsTheSame(
		oldItem: SubscribedRecyclerItems,
		newItem: SubscribedRecyclerItems
	): Boolean {
		return oldItem == newItem
	}
}