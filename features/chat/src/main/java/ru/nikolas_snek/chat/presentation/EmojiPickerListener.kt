package ru.nikolas_snek.chat.presentation

import ru.nikolas_snek.chat.presentation.chat_recycler.utils.EmojiCNCS

interface EmojiPickerListener {
    fun onSelectEmoji(emoji: EmojiCNCS)
    fun onEmojiPickerDismissed()
}