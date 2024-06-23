package ru.nikolas_snek.chat.presentation.chat_recycler.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.nikolas_snek.ui_kit.R
import ru.nikolas_snek.chat.presentation.chat_recycler.MessageAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateHeaderDecoration(
	private val adapter: MessageAdapter,
	private val context: Context,
	private val headerHeight: Int
) : RecyclerView.ItemDecoration() {
	private val dateFormatDM = SimpleDateFormat("dd MMM", Locale.getDefault())
	private val dateFormatYDM = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
	private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		color = ContextCompat.getColor(context, R.color.onSecondBackground)
		textSize = 40f
	}
	private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		color = ContextCompat.getColor(context, R.color.secondBackground)
		style = Paint.Style.FILL
	}

	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		val position = parent.getChildAdapterPosition(view)
		if (position == RecyclerView.NO_POSITION) {
			return
		}

		if (position == 0 || isDifferentDate(position)) {
			outRect.top = headerHeight
		}
	}

	override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		super.onDrawOver(canvas, parent, state)
		val itemCount = parent.adapter?.itemCount ?: 0

		for (i in 0 until itemCount) {
			val view = parent.getChildAt(i)
			val position = parent.getChildAdapterPosition(view)

			if (position != RecyclerView.NO_POSITION) {
				if (position == 0 || isDifferentDate(position)) {
					val currentDate = dateFormatDM.format(Date(getTimeStamp(position)))
					drawHeader(canvas, view, currentDate)
				}
			}
		}
	}

	private fun drawHeader(canvas: Canvas, view: View, date: String) {
		val textWidth = textPaint.measureText(date)
		val fontMetrics = textPaint.fontMetrics
		val textHeight = fontMetrics.descent - fontMetrics.ascent
		val rectWidth = textWidth + TEXT_PADDING * 2
		val rectHeight = textHeight + TEXT_PADDING * 2
		val rectLeft = (view.left + view.right - rectWidth) / 2
		val rectTop = view.top - rectHeight - TEXT_PADDING
		val cornerRadius = 16f
		val rect = RectF(rectLeft, rectTop, rectLeft + rectWidth, rectTop + rectHeight)
		canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint)
		val xPos = (view.left + view.right - textWidth) / 2
		val yPos = rectTop + TEXT_PADDING + textHeight - fontMetrics.bottom
		canvas.drawText(date, xPos, yPos, textPaint)
	}

	private fun isDifferentDate(position: Int): Boolean {
		if (position == 0) return true
		val previousTimeStamp = getTimeStamp(position - 1)
		val currentTimeStamp = getTimeStamp(position)
		val previousDate = dateFormatYDM.format(Date(previousTimeStamp))
		val currentDate = dateFormatYDM.format(Date(currentTimeStamp))

		return previousDate != currentDate
	}

	private fun getTimeStamp(position: Int): Long {
		return if (position != RecyclerView.NO_POSITION) {
			val timestampSec = adapter.currentList[position]?.timestamp ?: 0L
			timestampSec * 1000
		} else {
			0L
		}
	}

	companion object {
		private const val TEXT_PADDING = 8
	}
}