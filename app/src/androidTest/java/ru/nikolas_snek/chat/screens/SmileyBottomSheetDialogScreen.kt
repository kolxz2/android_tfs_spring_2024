package ru.nikolas_snek.chat.screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import ru.nikolas_snek.chat.R
import ru.nikolas_snek.chat.presentation.EmojiSheetDialog

object SmileyBottomSheetDialogScreen : KScreen<SmileyBottomSheetDialogScreen>() {
    override val layoutId: Int = R.layout.bottom_sheet_emoji_dialog
    override val viewClass: Class<*> = EmojiSheetDialog::class.java

    val gridView = KView { withId(R.id.rvEmojis) }
}