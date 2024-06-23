package ru.nikolas_snek.channels.presentation.subscribed.recycler.stream

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.channels.R
import ru.nikolas_snek.channels.databinding.StreamItemLayoutBinding
import ru.nikolas_snek.channels.domain.models.Stream

class StreamViewHolder(private val binding: StreamItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(stream: Stream) {
        binding.tvStreamTitle.text = stream.title
        val color = Color.parseColor(stream.color)
        binding.root.setBackgroundColor(color)
        if (!stream.isExpanded) {
            binding.arrowImageView.setImageResource(R.drawable.ic_arrow_down)
        } else {
            binding.arrowImageView.setImageResource(R.drawable.ic_arrow_up)
        }
    }

}