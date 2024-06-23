package ru.nikolas_snek.chat.presentation.chat_recycler.utils

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.nikolas_snek.chat.R
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.chat.presentation.custome.EmojiReactionView

abstract class MessageViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    open fun bind(
        message: Message,
        onMessageLongClickListener: ((Message) -> Unit)?,
        onEmojiClickListener: ((messageId: Long, emoji: Reaction) -> Unit)?
    ) {
    }

    companion object {
        fun createEmojiReactionView(
            context: Context,
            message: Message,
            reaction: Reaction,
            onEmojiClickListener: ((messageId: Long, emoji: Reaction) -> Unit)?
        ): EmojiReactionView {
            return with(EmojiReactionView(context)) {
                setOnClickListener {
                    onEmojiClickListener?.invoke(message.id, emoji)
                }
                emoji = reaction
                isSelected = reaction.isUserSelect
                reactionCounter = reaction.count
                setBackgroundResource(R.drawable.emoji_bg)
                setPadding(20, 15, 25, 10)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(10, 10, 10, 10)
                setLayoutParams(layoutParams)
                this
            }
        }

        fun addEmojiReactionImageView(
            context: Context, message: Message, listener: ((Message) -> Unit)?
        ) = with(ImageView(context)) {
            setImageResource(R.drawable.ic_add)
            setOnClickListener {
                listener?.invoke(message)
            }
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            layoutParams.setMargins(10, 10, 10, 10)
            setLayoutParams(layoutParams)
            this
        }
    }
}