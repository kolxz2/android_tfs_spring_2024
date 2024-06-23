package ru.nikolas_snek.channels.presentation.subscribed.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.domain.models.Topic
import ru.nikolas_snek.channels.presentation.subscribed.recycler.stream.StreamDelegate
import ru.nikolas_snek.channels.presentation.subscribed.recycler.topic.TopicDelegate
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.DiffUtilSubscribedItemsCallback
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.ListStreamsItemDelegate
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.SubscribedRecyclerItems

class SubscribedStreamsAdapter(
    onStreamClickListener: ((Stream) -> Unit),
    onTopiClickListener: ((Topic) -> Unit)
) : ListAdapter<SubscribedRecyclerItems, RecyclerView.ViewHolder>(DiffUtilSubscribedItemsCallback()) {
    private val delegates: Map<Int, ListStreamsItemDelegate> = mapOf(
        VIEW_TYPE_TOPIC to TopicDelegate(onTopiClickListener),
        VIEW_TYPE_STREAM to StreamDelegate(onStreamClickListener)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.getValue(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        delegates[getItemViewType(position)]?.onBindViewHolder(holder, item)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SubscribedRecyclerItems.TopicItem -> VIEW_TYPE_TOPIC
            is SubscribedRecyclerItems.StreamItem -> VIEW_TYPE_STREAM
        }
    }

    companion object {
        private const val VIEW_TYPE_TOPIC = 0
        private const val VIEW_TYPE_STREAM = 1
    }
}