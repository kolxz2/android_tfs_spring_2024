package ru.nikolas_snek.chat.screens

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.nikolas_snek.channels.R
import ru.nikolas_snek.channels.presentation.subscribed.SubscribedFragment

object StreamListScreen : KScreen<StreamListScreen>() {
    val streamRecyclerView = KRecyclerView({
        withId(R.id.rvAllStreams)
    }, itemTypeBuilder = {
        itemType(::StreamItem)
        itemType(::TopicItem)
    })

    class StreamItem(parent: Matcher<View>) : KRecyclerItem<StreamItem>(parent) {
        val tvStreamTitle: KTextView = KTextView(parent) { withId(R.id.tvStreamTitle) }
        val arrowImageView: KImageView = KImageView(parent) { withId(R.id.arrowImageView) }
    }

    class TopicItem(parent: Matcher<View>) : KRecyclerItem<TopicItem>(parent) {
        val tvTopicTitle: KTextView = KTextView(parent) { withId(R.id.tvTopicTitle) }
        val tvTopicMes: KTextView = KTextView(parent) { withId(R.id.tvTopicMes) }
    }

    override val layoutId: Int get() = R.layout.fragment_subscribed
    override val viewClass: Class<*> get() = SubscribedFragment::class.java
}