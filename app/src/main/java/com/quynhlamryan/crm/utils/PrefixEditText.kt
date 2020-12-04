package com.quynhlamryan.crm.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.quynhlamryan.crm.R


class PrefixEditText : AppCompatEditText {
    private val icon: Bitmap by lazy {
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ic_vn_flag
        )
    }
    private var mOriginalLeftPadding = -1f
    private var extraTextPadding = 20f

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(
        context: Context, attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculatePrefix()
    }

    private fun calculatePrefix() {
        if (mOriginalLeftPadding == -1f) {
            val prefix = tag as String
            val widths = FloatArray(prefix.length)
            paint.getTextWidths(prefix, widths)
            var textWidth = 0f
            for (w in widths) {
                textWidth += w
            }
            mOriginalLeftPadding = compoundPaddingLeft.toFloat()

            setPadding(
                (textWidth + mOriginalLeftPadding + icon.width + extraTextPadding).toInt(),
                paddingRight, paddingTop,
                paddingBottom
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val prefix = tag as String


        canvas.drawBitmap(
            icon, mOriginalLeftPadding,  compoundPaddingTop.toFloat(), paint
        )

        canvas.drawText(
            prefix, icon.width.toFloat() + extraTextPadding,
            getLineBounds(0, null).toFloat(), paint
        )


    }
}