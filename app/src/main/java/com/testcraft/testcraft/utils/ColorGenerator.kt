package com.testcraft.testcraft.utils

import java.util.*


class ColorGenerator private constructor(private val mColors: List<Int>) {
    private val mRandom: Random

    val randomColor: Int
        get() = mColors[mRandom.nextInt(mColors.size)]

    init {
        mRandom = Random(System.currentTimeMillis())
    }

    fun getColor(key: Any): Int {
        return mColors[Math.abs(key.hashCode()) % mColors.size]
    }

    companion object {

        var DEFAULT: ColorGenerator

        var MATERIAL: ColorGenerator

        init {
            DEFAULT = create(
                Arrays.asList(
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
                Arrays.asList(
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
