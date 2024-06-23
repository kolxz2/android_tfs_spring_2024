package ru.nikolas_snek.chat.presentation.emoji_recycler

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemClickListener(
	private val recyclerView: RecyclerView,
	private val onClick: (View, Int, Int, Long) -> Unit
) : RecyclerView.SimpleOnItemTouchListener() {
	private val gestureDetector =
		GestureDetector(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
			override fun onSingleTapUp(e: MotionEvent): Boolean = true

			override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
				val view = recyclerView.findChildViewUnder(e.x, e.y)
				if (view != null) {
					onClick(
						view,
						recyclerView.getChildAdapterPosition(view),
						recyclerView.getChildLayoutPosition(view),
						recyclerView.getChildItemId(view)
					)
				}
				return true
			}
		})

	override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
		gestureDetector.onTouchEvent(e)
		return false
	}
}