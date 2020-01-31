package com.testcraft.testcraft.utils

import java.util.*
import kotlin.math.abs

class ColorGenerator private constructor(private val mColors: List<Int>) {

    private val mRandom: Random = Random(System.currentTimeMillis())

    val randomColor: Int
        get() = mColors[mRandom.nextInt(mColors.size)]

    fun getColor(key: Any): Int {
        return mColors[abs(key.hashCode()) % mColors.size]
    }

    companion object {

        var DEFAULT: ColorGenerator

        var MATERIAL: ColorGenerator

        init {
            DEFAULT = create(
                listOf(
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD
                )
            )

            MATERIAL = create(
                listOf(
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD,
                    -0x1295BD
                )
            )

        }

        fun create(colorList: List<Int>): ColorGenerator {
            return ColorGenerator(colorList)
        }
    }
}
