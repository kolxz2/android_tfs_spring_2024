package ru.nikolas_snek.chat.domain.models

data class Reaction(
    val emojiCode: String,
    val isUserSelect: Boolean,
    val count: Int,
    val emojiName: String
) {
    fun getCodeString(): String {
        val cleanedEmojiCodes = emojiCode.split("-")
        val chars = cleanedEmojiCodes.map { code ->
            try {
                String(Character.toChars(code.toInt(16)))
            } catch (_: Exception) {
            }
        }
        return chars.joinToString(separator = "")
    }
}
