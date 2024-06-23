package ru.nikolas_snek.chat.presentation.chat_recycler.other_messages

import android.graphics.Bitmap
import android.text.Html
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import ru.nikolas_snek.chat.databinding.OtherUserMessageItemBinding
import ru.nikolas_snek.chat.domain.models.Message
import ru.nikolas_snek.chat.domain.models.Reaction
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.MessageViewHolder

class OtherMessageViewHolder(
	private val binding: OtherUserMessageItemBinding
) : MessageViewHolder(binding) {
	private val transformation = object : Transformation {
		override fun transform(source: Bitmap): Bitmap {
			val width = source.width
			val height = source.height
			val size = minOf(width, height)
			val result =
				Bitmap.createBitmap(source, (width - size) / 2, (height - size) / 2, size, size)
			if (result != source) {
				source.recycle()
			}
			return result
		}

		override fun key(): String {
			return "square()"
		}
	}

	override fun bind(
		message: Message,
		onMessageLongClickListener: ((Message) -> Unit)?,
		onEmojiClickListener: ((messageId: Long, emoji: Reaction) -> Unit)?
	) {
		with(binding) {
			Picasso.get().load(message.avatarUrl).transform(transformation).into(ivUserPortrait)
			val messageContent =
				Html.fromHtml(message.content, Html.FROM_HTML_MODE_COMPACT).replace(Regex("\\n+$"), "")
			messageBox.tvUserMessage.text = messageContent
			messageBox.tvUserName.text = message.senderFullName
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