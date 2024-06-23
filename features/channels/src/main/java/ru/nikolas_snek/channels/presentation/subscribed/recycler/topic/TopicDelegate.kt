package ru.nikolas_snek.channels.presentation.subscribed.recycler.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.channels.databinding.TopicItemLayoutBinding
import ru.nikolas_snek.channels.domain.models.Topic
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.ListStreamsItemDelegate
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.SubscribedRecyclerItems

class TopicDelegate(
    private val onTopiClickListener: ((Topic) -> Unit)
) : ListStreamsItemDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = TopicItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: SubscribedRecyclerItems) {
        val topic = (item as? SubscribedRecyclerItems.TopicItem)?.topic ?: return
        (holder as? TopicViewHolder)?.bind(topic)
        holder.itemView.setOnClickListener {
            onTopiClickListener.invoke(topic)
        }
    }
}