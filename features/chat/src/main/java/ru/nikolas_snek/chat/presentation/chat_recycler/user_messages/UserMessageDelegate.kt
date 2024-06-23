package ru.nikolas_snek.chat.presentation.chat_recycler.user_messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.chat.databinding.UserMessageItemBinding
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.AdapterDelegate

class UserMessageDelegate(
    private val onMessageLongClickListener: ((Message) -> Unit),
    private val onEmojiClickListener: ((messageId: Long, emoji: Reaction) -> Unit)
) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = UserMessageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Message) {
        (holder as? UserMessageViewHolder)?.bind(
            item, onMessageLongClickListener, onEmojiClickListener
        )
        holder.itemView.setOnLongClickListener {
            onMessageLongClickListener.invoke(item)
            true
        }
    }
}
