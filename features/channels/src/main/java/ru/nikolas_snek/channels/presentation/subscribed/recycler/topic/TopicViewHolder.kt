package ru.nikolas_snek.channels.presentation.subscribed.recycler.topic

import android.view.View.GONE
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.channels.R
import ru.nikolas_snek.channels.databinding.TopicItemLayoutBinding
import ru.nikolas_snek.channels.domain.models.Topic

class TopicViewHolder(private val binding: TopicItemLayoutBinding) :
	RecyclerView.ViewHolder(binding.root) {
	fun bind(topic: Topic) {
		binding.tvTopicTitle.text = topic.title

		if (topic.unreadMessages == 0) {
			binding.tvTopicMes.visibility = GONE
		} else {
			binding.tvTopicMes.text = binding.root.context.getString(
				R.string.unread_messages,
				topic.unreadMessages.toString()
			)
		}
	}

}