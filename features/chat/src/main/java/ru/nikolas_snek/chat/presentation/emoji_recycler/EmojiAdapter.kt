package ru.nikolas_snek.chat.presentation.emoji_recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.chat.databinding.EmojiItemBinding
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.EmojiCNCS

class EmojiAdapter : ListAdapter<EmojiCNCS, EmojiAdapter.EmojiViewHolder>(DiffCallback) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
		val binding = EmojiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return EmojiViewHolder(binding)
	}

	override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class EmojiViewHolder(private val binding: EmojiItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(emoji: EmojiCNCS) {
			binding.emojiTextView.text = emoji.getCodeString()
		}
	}

	companion object DiffCallback : DiffUtil.ItemCallback<EmojiCNCS>() {
		override fun areItemsTheSame(oldItem: EmojiCNCS, newItem: EmojiCNCS): Boolean {
			return oldItem.code == newItem.code
		}

		override fun areContentsTheSame(oldItem: EmojiCNCS, newItem: EmojiCNCS): Boolean {
			return oldItem == newItem
		}
	}
}