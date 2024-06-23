package ru.nikolas_snek.channels.presentation.all_streams.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nikolas_snek.channels.databinding.AllStreamsItemBinding
import ru.nikolas_snek.channels.domain.models.Stream

class AllStreamsAdapter(
    private val onStreamClickListener: ((Stream) -> Unit)
) : ListAdapter<Stream, AllStreamsViewHolder>(DiffUtilAllStreamsItemsCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllStreamsViewHolder {
        val binding = AllStreamsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AllStreamsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllStreamsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onStreamClickListener.invoke(item)
        }
    }

}

