package ru.nikolas_snek.chat.screens

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.nikolas_snek.chat.R
import ru.nikolas_snek.chat.presentation.ChatFragment

object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {
    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val frameLayout = KButton { withId(R.id.frameLayout) }
    val btGoBack = KButton { withId(R.id.btGoBack) }
    val tvStreamTitle = KTextView { withId(R.id.tvStreamTitle) }
    val tvTopicTitle = KTextView { withId(R.id.tvTopicTitle) }
    val pdChat = KProgressBar { withId(R.id.pdChat) }
    val recyclerView = KRecyclerView({ withId(R.id.rvMessages) }, itemTypeBuilder = {
        itemType(ChatFragmentScreen::ChatItemType1)
        itemType(ChatFragmentScreen::ChatItemType2)
    })
    val etMessageContent = KEditText { withId(R.id.etMessageContent) }
    val btSentFile = KButton { withId(R.id.btSentFile) }
    val btSentMessage = KButton { withId(R.id.btSentMessage) }

    class ChatItemType1(parent: Matcher<View>) : KRecyclerItem<ChatItemType1>(parent) {
        val ivUserPortrait = KImageView(parent) { withId(R.id.ivUserPortrait) }
        val tvUserName = KTextView(parent) { withId(R.id.tvUserName) }
        val tvUserMessage = KTextView(parent) { withId(R.id.tvUserMessage) }
        val emojiBox = KView(parent) { withId(R.id.emojiBox) }
    }

    class ChatItemType2(parent: Matcher<View>) : KRecyclerItem<ChatItemType2>(parent) {
        val tvUserName = KTextView(parent) { withId(R.id.tvUserName) }
        val tvUserMessage = KTextView(parent) { withId(R.id.tvUserMessage) }
        val emojiBox = KView(parent) { withId(R.id.emojiBox) }
    }
}
