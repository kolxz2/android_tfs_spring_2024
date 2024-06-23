package ru.nikolas_snek.chat.presentation.chat_recycler.utils

import androidx.recyclerview.widget.DiffUtil
import ru.nikolas_snek.chat.domain.models.Message

class DelegateAdapterItemCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}