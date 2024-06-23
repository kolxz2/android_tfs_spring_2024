package ru.nikolas_snek.chat.presentation.chat_recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.chat.presentation.chat_recycler.other_messages.OtherMessageDelegate
import ru.nikolas_snek.chat.presentation.chat_recycler.user_messages.UserMessageDelegate
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.DelegateAdapterItemCallback

class MessageAdapter(
    onMessageLongClickListener: ((Message) -> Unit),
    onEmojiClickListener: ((messageId: Long, emoji: Reaction) -> Unit)
) : ListAdapter<Message, RecyclerView.ViewHolder>(DelegateAdapterItemCallback()) {
    private val delegates = mapOf(
        VIEW_TYPE_USER_MESSAGE to UserMessageDelegate(
            onMessageLongClickListener,
            onEmojiClickListener
        ),
        VIEW_TYPE_OTHER_MESSAGE to OtherMessageDelegate(
            onMessageLongClickListener,
            onEmojiClickListener
        )
    )
    companion object {
        private const val VIEW_TYPE_USER_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.getValue(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        delegates.getValue(holder.itemViewType).onBindViewHolder(holder, message)
    }

    override fun getItemViewType(position: Int): Int = if (getItem(position).isMeMessage) {
        VIEW_TYPE_USER_MESSAGE
    } else {
        VIEW_TYPE_OTHER_MESSAGE
    }
}

