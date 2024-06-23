package ru.nikolas_snek.channels.presentation.subscribed.recycler.stream

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.channels.databinding.StreamItemLayoutBinding
import ru.nikolas_snek.channels.domain.models.Stream
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.ListStreamsItemDelegate
import ru.nikolas_snek.channels.presentation.subscribed.recycler.utils.SubscribedRecyclerItems

class StreamDelegate(
    private val onStreamClickListener: ((Stream) -> Unit)
) : ListStreamsItemDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = StreamItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StreamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: SubscribedRecyclerItems) {
        val stream = (item as? SubscribedRecyclerItems.StreamItem)?.stream ?: return
        (holder as? StreamViewHolder)?.bind(stream)
        holder.itemView.setOnClickListener {
            onStreamClickListener.invoke(stream)
        }
    }
}