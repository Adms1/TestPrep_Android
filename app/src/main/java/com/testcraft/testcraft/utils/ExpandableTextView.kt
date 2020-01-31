package com.testcraft.testcraft.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

class ExpandableTextView {

    interface ViewMoreClickListener {
        fun viewMoreClicked(index: Int)
    }

    fun addViewMoreToTextView(
        position: Int,
        textView: TextView,
        text: String,
        expandText: String,
        maxLine: Int,
        listener: ViewMoreClickListener?
    ) {
        try {
            textView.post {
                val truncatedSpannableString: SpannableStringBuilder
                val startIndex: Int
                if (textView.lineCount > maxLine) {
                    val lastCharShown = textView.layout.getLineVisibleEnd(maxLine - 1)
                    val displayText =
                        text.substring(0, lastCharShown - expandText.length + 1) + " " + expandText
                    startIndex = displayText.indexOf(expandText)
                    truncatedSpannableString = SpannableStringBuilder(displayText)
                    textView.text = truncatedSpannableString
                    truncatedSpannableString.setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View?) { // this click event is not firing that's why we are adding click event for text view below.
                            }
                        },
                        startIndex,
                        startIndex + expandText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textView.text = truncatedSpannableString
                    textView.setOnClickListener {
                        if (listener != null) listener.viewMoreClicked(
                            position
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
