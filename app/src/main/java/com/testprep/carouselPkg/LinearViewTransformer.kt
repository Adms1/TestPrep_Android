package com.testprep.carouselPkg

/**
 *
 * Linear carousel.
 *
 *
 * Default it is a horizontal linear carousel.
 * You may set parameter offsetXPercent=0, offsetYPercent=1 for a vertical linear carousel.
 *
 * Available parameters: <br></br>
 *
 *  * [.setOffsetXPercent] linear X-axis translation rate. Default 1
 *  * [.setOffsetYPercent] linear Y-axis translation rate. Default 0
 *  * [.setScaleXFactor] linear X-axis scale rate
 *  * [.setScaleYFactor] linear Y-axis scale rate
 *  * [.setMinScaleX] minimum X-axis scale
 *  * [.setMinScaleY] minimum Y-axis scale
 *  * [.setMaxScaleX] maximum X-axis scale
 *  * [.setMaxScaleY] maximum Y-axis scale
 *  * [.setScaleLargestAtCenter] whether the current item should be scaled largest (inversely to scale rate). Default false
 *
 *
 * @author  sunny-chung
 */

class LinearViewTransformer : ParameterizableViewTransformer() {
    init {
        offsetXPercent = 1f
        offsetYPercent = 0f
    }

    public override fun getOffsetXPercent(): Float {
        return super.getOffsetXPercent()
    }

    public override fun getOffsetYPercent(): Float {
        return super.getOffsetYPercent()
    }

    public override fun setOffsetXPercent(offsetXPercent: Float) {
        super.setOffsetXPercent(offsetXPercent)
    }

    public override fun setOffsetYPercent(offsetYPercent: Float) {
        super.setOffsetYPercent(offsetYPercent)
    }

    public override fun getScaleXFactor(): Float {
        return super.getScaleXFactor()
    }

    public override fun setScaleXFactor(scaleXFactor: Float) {
        super.setScaleXFactor(scaleXFactor)
    }

    public override fun setScaleYFactor(scaleYFactor: Float) {
        super.setScaleYFactor(scaleYFactor)
    }

    public override fun getScaleYFactor(): Float {
        return super.getScaleYFactor()
    }

    public override fun getMaxScaleX(): Float {
        return super.getMaxScaleX()
    }

    public override fun getMaxScaleY(): Float {
        return super.getMaxScaleY()
    }

    public override fun getMinScaleX(): Float {
        return super.getMinScaleX()
    }

    public override fun getMinScaleY(): Float {
        return super.getMinScaleY()
    }

    public override fun setMaxScaleX(maxScaleX: Float) {
        super.setMaxScaleX(maxScaleX)
    }

    public override fun setMaxScaleY(maxScaleY: Float) {
        super.setMaxScaleY(maxScaleY)
    }

    public override fun setMinScaleX(minScaleX: Float) {
        super.setMinScaleX(minScaleX)
    }

    public override fun setMinScaleY(minScaleY: Float) {
        super.setMinScaleY(minScaleY)
    }

    public override fun isScaleLargestAtCenter(): Boolean {
        return super.isScaleLargestAtCenter()
    }

    public override fun setScaleLargestAtCenter(scaleLargestAtCenter: Boolean) {
        super.setScaleLargestAtCenter(scaleLargestAtCenter)
    }
}
