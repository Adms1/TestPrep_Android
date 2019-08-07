package com.testprep.carouselPkg

import android.support.annotation.IntRange
import android.view.View
import com.testprep.carouselPkg.CarouselView1
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Simulate a Merry-go-round carousel. <br></br>
 * <br></br>
 * This is a 'flat' version that item views are not rotated and always face front. <br></br>
 * <br></br>
 * Available parameters: <br></br>
 *
 *  * [.setNumPies] number of item views to be distributed across the circle evenly. Default 7
 *  * [.setHorizontalViewPort] % of parent width to be used for display. Default 80%
 *  * [.setViewPerspective] normalized camera height. Default 0.6
 *  * [.setFarScale] scale of itemview when it is layout at a far distance (y = 0). Default 0.35
 *  * [.setFarAlpha] alpha of itemview when it is layout at a far distance (y = 0). Default 0
 *
 *
 * @author  sunny-chung
 */

class FlatMerryGoRoundTransformer : CarouselView1.ViewTransformer {
    var numPies = 10
        set(@IntRange(from = 1) numPies) {
            field = numPies
            mPieRad = 1.0
        }
    //    private double mPieRad = Math.PI * 2.0 / mNumPies; // depends on mNumPies
    private var mPieRad = 1.0 // depends on mNumPies

    var horizontalViewPort = 0.75
    var viewPerspective = 0.1
    var farScale = 0.0
    var farAlpha = 0.0

    override fun onAttach(layoutManager: CarouselLayoutManager) {
        layoutManager.setDrawOrder(CarouselView1.DrawOrder.CenterFront)
    }

    override fun transform(view: View, position: Float) {
        val width = view.measuredWidth
        val height = view.measuredHeight
        val parentWidth = (view.parent as View).measuredWidth
        val parentHeight = (view.parent as View).measuredHeight

        // create perspective view by
        // compressing height of circle linearly to ellipse
        // and proportioning scale and alpha linearly

        // ellipse formula:
        //   x(t) = a cos t
        //   y(t) = b sin t
        //  where t ∈ [0, 2π]

        val rotateRad = Math.PI * 1.5 + position * mPieRad
        val a = parentWidth * horizontalViewPort / 2.0
        val b = a * viewPerspective

        val x = a * cos(rotateRad)
        var y = b * (1 - sin(rotateRad)) // (1 - sin t) because y axis is reversed

        val maxY = 2 * b // since maximum of (1 - sin t) => 2

        // TODO scale should depend on mViewPerspective as well
        val scale = max(0.0, (farScale - 1) * (y - maxY) / (0 - maxY) + 1)
        val alpha = max(0.0, (farAlpha - 1) * (y - maxY) / (0 - maxY) + 1)

        y -= maxY / 2 // reposition center so that y ∈ [-maxY/2, maxY/2]

        //        y += (parentHeight - maxY - height) / 2; // center vertically

        view.translationX = x.toFloat()
        view.translationY = y.toFloat()
        view.scaleX = scale.toFloat()
        view.scaleY = scale.toFloat()
        view.alpha = alpha.toFloat()
    }
}
