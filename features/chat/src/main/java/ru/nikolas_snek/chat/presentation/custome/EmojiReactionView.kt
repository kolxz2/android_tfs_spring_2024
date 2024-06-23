package ru.nikolas_snek.chat.presentation.custome

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import ru.nikolas_snek.chat.R
import ru.nikolas_snek.chat.domain.models.Reaction

class EmojiReactionView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0, defTheme: Int = 0
) : View(context, attributeSet, defStyle, defTheme) {
    var emoji: Reaction = Reaction("\uD83D\uDE00", true, 1, "funny")
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }
    var reactionCounter: Int = 0
        set(value) {
            if (value >= 0 && field != value) {
                field = value
                requestLayout()
            } else {
                field = 0
                requestLayout()
            }
        }
    private var reactionTextPaintColor: Int = Color.WHITE
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }
    private var reactionPaintSize: Float = 14f.sp(context)
        set(value) {
            if (field == value) return
            field = value
            requestLayout()
        }
    private val reactionToDrawn
        get() = "${emoji.getCodeString()}  $reactionCounter"
    private val reactionPaint = TextPaint().apply {
        color = reactionTextPaintColor
        textSize = reactionPaintSize
    }
    private val textRect = Rect()

    init {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.EmojiReactionView, defStyle, 0)
        val textStyleResId = typedArray.getResourceId(R.styleable.EmojiReactionView_textStyle, 0)
        if (textStyleResId != 0) {
            val textAppearance =
                context.theme.obtainStyledAttributes(textStyleResId, R.styleable.MyTextStyle)
            try {
                reactionTextPaintColor =
                    textAppearance.getColor(R.styleable.MyTextStyle_textColor, Color.BLACK)
                reactionPaintSize =
                    textAppearance.getDimensionPixelSize(R.styleable.MyTextStyle_textSize, 14)
                        .toFloat()
            } finally {
                textAppearance.recycle()
            }
        }
        typedArray.recycle()
    }

    private fun Float.sp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        reactionPaint.getTextBounds(reactionToDrawn, 0, reactionToDrawn.length, textRect)
        val actualWidth =
            resolveSize(textRect.width() + paddingLeft + paddingRight, widthMeasureSpec)
        val actualHeight =
            resolveSize(textRect.height() + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2 - textRect.exactCenterY()
        canvas.drawText(reactionToDrawn, paddingLeft.toFloat(), topOffset, reactionPaint)
    }
}