package ru.nikolas_snek.channels.presentation.all_streams.recycler

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.channels.R
import ru.nikolas_snek.channels.databinding.AllStreamsItemBinding
import ru.nikolas_snek.channels.domain.models.Stream

class AllStreamsViewHolder(private val binding: AllStreamsItemBinding) :
	RecyclerView.ViewHolder(binding.root) {
	fun bind(stream: Stream) {
		binding.tvStreamTitle.text = stream.title
		val color = Color.parseColor(stream.color)
		binding.root.setBackgroundColor(color)
		if (stream.isSubscribed) {
			binding.ivSubscribeStatus.setImageResource(R.drawable.ic_unsubscribe)
		} else {
			binding.ivSubscribeStatus.setImageResource(R.drawable.ic_subscribe)
		}
	}
}