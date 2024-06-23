package ru.nikolas_snek.channels.presentation.all_streams.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.nikolas_snek.channels.domain.models.Stream

class DiffUtilAllStreamsItemsCallback : DiffUtil.ItemCallback<Stream>() {
    override fun areItemsTheSame(oldItem: Stream, newItem: Stream): Boolean {
        return oldItem.streamId == newItem.streamId
    }

    override fun areContentsTheSame(oldItem: Stream, newItem: Stream): Boolean {
        return oldItem == newItem
    }
}