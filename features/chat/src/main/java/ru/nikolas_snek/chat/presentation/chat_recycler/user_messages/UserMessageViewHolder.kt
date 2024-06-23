package ru.nikolas_snek.chat.presentation.chat_recycler.user_messages


import android.graphics.Color
import android.text.Html
import androidx.core.view.isVisible
import ru.nikolas_snek.chat.R
import ru.nikolas_snek.chat.databinding.UserMessageItemBinding
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.MessageViewHolder

class UserMessageViewHolder(
	private val binding: UserMessageItemBinding
) : MessageViewHolder(binding) {
	override fun bind(
		message: Message,
		onMessageLongClickListener: ((Message) -> Unit)?,
		onEmojiClickListener: ((messageId: Long, emoji: Reaction) -> Unit)?
	) {
		with(binding) {
			messageBox.messageContentLayout.setBackgroundResource(R.drawable.user_message_text_shape)
			messageBox.tvUserName.isVisible = false
			messageBox.tvUserMessage.setTextColor(Color.WHITE)
			messageBox.tvUserMessage.text =
				Html.fromHtml(message.content, Html.FROM_HTML_MODE_COMPACT).replace(Regex("\\n+$"), "")
			emojiBox.removeAllViews()
			message.reactions.forEach { reaction ->
				if (emojiBox.childCount == 0) {
					emojiBox.addView(
						createEmojiReactionView(
							context = binding.root.context,
							message = message,
							reaction = reaction,
							onEmojiClickListener = onEmojiClickListener
						)
					)
					emojiBox.addView(
						addEmojiReactionImageView(
							binding.root.context, message, onMessageLongClickListener
						)
					)
				} else {
					emojiBox.addView(
						createEmojiReactionView(
							context = binding.root.context,
							message = message,
							reaction = reaction,
							onEmojiClickListener = onEmojiClickListener
						)
					)
				}
			}
		}
	}
}