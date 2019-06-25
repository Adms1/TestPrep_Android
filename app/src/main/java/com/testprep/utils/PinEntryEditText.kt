package com.testprep.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText


class PinEntryEditText : EditText {

    val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"

    var mSpace = 10f //24 dp by default
    var mCharSize = 0f
    var mNumChars = 6f
    var mLineSpacing = 8f //8dp by default
    var mMaxLength = 6

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context, attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context, attrs: AttributeSet,
        defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        setBackgroundResource(0)

        val multi = context.resources.displayMetrics.density
        mSpace *= multi //convert to pixels for our density

        mLineSpacing *= multi //convert to pixels
        mMaxLength = attrs.getAttributeIntValue(
            XML_NAMESPACE_ANDROID, "maxLength", 6
        )
        mNumChars = mMaxLength.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas);
        val availableWidth = width - paddingRight - paddingLeft
        mCharSize = if (mSpace < 0) {
            availableWidth / (mNumChars * 2 - 1)
        } else {
            (availableWidth - mSpace * (mNumChars - 1)) / mNumChars
        }

        var startX: Float = paddingLeft.toFloat()
        val bottom: Float = height - paddingBottom.toFloat()
        val text = text
        val textLength = text.length
        val textWidths = FloatArray(textLength)

        for (i in 0 until mNumChars.toInt()) {
            canvas.drawLine(
                startX, bottom, startX + mCharSize, bottom, paint
            )

            if (text.length > i) {
                val middle = startX + mCharSize / 2
                canvas.drawText(
                    text,
                    i,
                    i + 1,
                    middle - textWidths[0] / 2,
                    bottom - mLineSpacing,
                    paint
                )
            }

            startX += if (mSpace < 0) {
                mCharSize * 2
            } else {
                mCharSize + mSpace
            }
        }
    }
}
