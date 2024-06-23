package ru.nikolas_snek.chat.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.nikolas_snek.chat.R
import ru.nikolas_snek.chat.databinding.BottomSheetEmojiDialogBinding
import ru.nikolas_snek.chat.presentation.chat_recycler.utils.emojiSetCNCS
import ru.nikolas_snek.chat.presentation.emoji_recycler.EmojiAdapter
import ru.nikolas_snek.chat.presentation.emoji_recycler.ItemClickListener

class EmojiSheetDialog : BottomSheetDialogFragment(R.layout.bottom_sheet_emoji_dialog) {
	private val binding by viewBinding(BottomSheetEmojiDialogBinding::bind)
	private lateinit var emojiAdapter: EmojiAdapter

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.rvEmojis.isNestedScrollingEnabled = true
		emojiAdapter = EmojiAdapter()
		binding.rvEmojis.adapter = emojiAdapter
		binding.rvEmojis.layoutManager =
			GridLayoutManager(context, 10)
		emojiAdapter.submitList(emojiSetCNCS)
		binding.rvEmojis.addOnItemClickListener { _, _, position, _ ->
			val selectedItem = emojiAdapter.currentList[position]
			(parentFragment as EmojiPickerListener).onSelectEmoji(selectedItem)
			dismiss()
		}
	}

	private fun RecyclerView.addOnItemClickListener(onClick: (View, Int, Int, Long) -> Unit) {
		this.addOnItemTouchListener(ItemClickListener(this, onClick))
	}

	override fun onDismiss(dialog: DialogInterface) {
		super.onDismiss(dialog)
		(parentFragment as EmojiPickerListener).onEmojiPickerDismissed()
	}
}


