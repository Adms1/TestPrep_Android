package com.testprep.carouselPkg

/**
 * Normal scroller.
 *
 * @author  sunny-chung
 */

class NormalScroller : CarouselView1.Scroller {
    override fun tweakScrollDx(dx: Int): Int {
        return dx
    }

    override fun tweakScrollDy(dy: Int): Int {
        return dy
    }

    override fun inverseTweakScrollDx(dx: Int): Int {
        return dx
    }

    override fun inverseTweakScrollDy(dy: Int): Int {
        return dy
    }

    override fun tweakScrollDx(dx: Float): Float {
        return dx
    }

    override fun tweakScrollDy(dy: Float): Float {
        return dy
    }
}
