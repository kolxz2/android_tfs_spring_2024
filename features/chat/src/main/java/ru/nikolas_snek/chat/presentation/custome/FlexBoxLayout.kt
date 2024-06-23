package ru.nikolas_snek.chat.presentation.custome

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class FlexBoxLayout @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		var currentLineLeft = paddingLeft
		var topOffset = paddingTop
		var maxChildHeight = 0
		var lineNeedsBreak = false
		for (i in 0 until childCount) {
			val child = getChildAt(i)
			if (child.visibility != GONE) {
				val params = child.layoutParams as MarginLayoutParams
				val horizontalSpace = child.measuredWidth + params.leftMargin + params.rightMargin
				if (lineNeedsBreak) {
					lineNeedsBreak = false
				} else {
					val isOutside = (child.left < paddingLeft || child.right > width - paddingRight)
					val isExceedingWidth = currentLineLeft + horizontalSpace > width - paddingRight
					if (isExceedingWidth || isOutside) {
						currentLineLeft = paddingLeft
						topOffset += maxChildHeight
						maxChildHeight = 0
					}
					lineNeedsBreak = true
				}
				val right = minOf(
					currentLineLeft + horizontalSpace, width - paddingRight
				)
				val bottom = minOf(
					topOffset + child.measuredHeight + params.topMargin + params.bottomMargin,
					b - paddingBottom
				)
				child.layout(
					currentLineLeft + params.leftMargin,
					topOffset + params.topMargin,
					right - params.rightMargin,
					bottom - params.bottomMargin
				)
				currentLineLeft += horizontalSpace
				maxChildHeight = maxOf(
					maxChildHeight, child.measuredHeight + params.topMargin + params.bottomMargin
				)
			}
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		measureChildren(widthMeasureSpec, heightMeasureSpec)
		var width = paddingLeft + paddingRight
		var height = paddingTop + paddingBottom

		var currentLineWidth = 0
		var maxChildHeight = 0

		for (i in 0 until childCount) {
			val child = getChildAt(i)
			val params = child.layoutParams as MarginLayoutParams

			if (child.visibility != GONE) {
				if (currentLineWidth + child.measuredWidth + params.leftMargin + params.rightMargin > MeasureSpec.getSize(
						widthMeasureSpec
					) - paddingRight
				) {
					width = maxOf(
						width, currentLineWidth + paddingLeft + paddingRight
					)
					height += maxChildHeight
					currentLineWidth = 0
					maxChildHeight = 0
				}
				currentLineWidth += child.measuredWidth + params.leftMargin + params.rightMargin
				maxChildHeight = maxOf(
					maxChildHeight, child.measuredHeight + params.topMargin + params.bottomMargin
				)
			}
		}
		width = maxOf(
			width, currentLineWidth + paddingLeft + paddingRight
		)
		height += maxChildHeight + paddingBottom
		height = maxOf(height, suggestedMinimumHeight)
		setMeasuredDimension(
			resolveSizeAndState(width, widthMeasureSpec, 0),
			resolveSizeAndState(height, heightMeasureSpec, 0)
		)
	}

	override fun addView(child: View?, index: Int, params: LayoutParams?) {
		super.addView(child, if (child !is ImageView) 0 else index, params)
	}

	override fun addView(child: View?, width: Int, height: Int) {
		super.addView(child, if (child !is ImageView) 0 else width, LayoutParams(width, height))
	}

	override fun generateDefaultLayoutParams(): LayoutParams =
		MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

	override fun generateLayoutParams(attrs: AttributeSet): LayoutParams =
		MarginLayoutParams(context, attrs)

	override fun generateLayoutParams(p: LayoutParams): LayoutParams = MarginLayoutParams(p)

}
