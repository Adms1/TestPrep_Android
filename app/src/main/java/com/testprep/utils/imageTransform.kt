package com.testprep.utils

import android.graphics.Bitmap
import com.squareup.picasso.Transformation


class imageTransform(val size: Int, val isHeightScale: Boolean) : Transformation {

    override fun transform(bitmap: Bitmap): Bitmap? {
        var scale: Float
        var newSize: Int
        var scaleBitmap: Bitmap?

        if (isHeightScale) {
            scale = (size / bitmap.height).toFloat()
            newSize = Math.round(bitmap.width * scale)

            scaleBitmap = Bitmap.createScaledBitmap(bitmap, newSize, size, true)

        } else {
            scale = (size / bitmap.width).toFloat()
            newSize = Math.round(bitmap.height * scale)
            scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, newSize, true)
        }
        if (scaleBitmap != bitmap) {
            bitmap.recycle()
        }

        return scaleBitmap!!
    }

    override fun key(): String? {
        return "scaleRespectRatio$size$isHeightScale"
    }
}

