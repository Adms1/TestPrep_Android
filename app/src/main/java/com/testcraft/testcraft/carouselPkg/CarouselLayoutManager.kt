package com.testcraft.testcraft.carouselPkg

import android.graphics.PointF
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import java.util.*

/**
 * Layout Manager used to help view layout in CarouselView1. Normal users are not expected to access this class directly.
 *
 *
 *
 * @author sunny-chung
 */
class CarouselLayoutManager : LayoutManager() {
    //	private boolean mInverseScrollDirection = false;
    private var mOnItemClickListener: CarouselView1.OnItemClickListener? = null
    private var mInfinite = true
    private var mDrawOrder: CarouselView1.DrawOrder = CarouselView1.DrawOrder.FirstBack
    /**
     * Returns the number of extra children per side to be preserved and managed by transformations.
     *
     * @return
     */
    var extraVisibleChilds = 0
        private set
    /**
     * Returns how itemviews are positioned.
     *
     * @return
     */
    /**
     * Set how itemviews are positioned.
     *
     * @param gravity
     */
    var gravity = Gravity.CENTER_HORIZONTAL
        set(gravity) {
            field = gravity
            requestLayout()
        }

    //	private SparseArray<WeakReference<View>> mViewAtPosition = new SparseArray<>(1000);

    //	private PerformanceTimer mPerformanceTimer = new PerformanceTimer("CarouselLayoutManager");
    private var mPendingTasks: Queue<Runnable> = LinkedList()

    private val mHandler = Handler()

    // temp cached values
    private var mRecyclerView: RecyclerView? = null
    private var mDecoratedChildWidth: Int = 0
    private var mDecoratedChildHeight: Int = 0
    private var mLeftOffset = 0
    private var mTopOffset = 0
    private var mMeasuredWidth = 0
    private var mMeasuredHeight =
        0 // mark down measuredWidth, measuredHeight ourselves because getWidth()/getHeight() doesn't update after onMeasure()
    private var mHasDatasetUpdated = false

    private var mScrollPositionUpdated = false

    // state
    /**
     * Returns the current X-axis scrolling position in pixels.
     *
     * @return
     */
    var scrollX = 0
        private set

    // TODO assume same width & height for all childrens

    private var mTransformer = DEFAULT_TRANSFORMER
    private var mScroller: CarouselView1.Scroller? = null

    private val contentLeftX: Int
        get() = scrollX - contentWidth / 2

    private val contentRightX: Int
        get() = scrollX + contentWidth / 2

    private val leftmostVisiblePosition: Int
        get() {
            val pos = Math.floor(pixelToPosition(contentLeftX).toDouble()).toInt() - extraVisibleChilds
            return if (mInfinite) pos else Math.max(pos, 0)
        }

    private val rightmostVisiblePosition: Int
        get() {
            val pos = Math.ceil(pixelToPosition(contentRightX).toDouble()).toInt() + extraVisibleChilds
            return if (mInfinite) pos else Math.min(pos, itemCount - 1)
        }

    /**
     * Returns the current position. It can be negative or very large if
     * the items are repeating.
     *
     * @return
     */
    val currentPosition: Int
        get() = Math.round(pixelToPosition(scrollX))

    /**
     * Returns the current position in floating points.
     *
     * @return
     */
    val currentPositionPoint: Float
        get() = pixelToPosition(scrollX)

    /**
     * Returns the scrolling position in pixel.
     *
     * @return
     */
    val currentOffset: Float
        get() {
            val totalOffset = pixelToPosition(scrollX)
            return Math.abs(totalOffset - Math.floor(totalOffset.toDouble()).toFloat())
        }

    private val contentWidth: Int
        get() = mMeasuredWidth - paddingRight - paddingLeft

    private val visibleChildCount: Int
        get() = contentWidth / mDecoratedChildWidth + 1

    /**
     * Retrieve the underlying CarouselView1.
     *
     * @return
     */
    protected val carouselView: CarouselView1?
        get() = mRecyclerView as CarouselView1?

    init {
        setTransformer(null)
        resetOptions()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val ss = SavedState()
        ss.scrollOffset = scrollX
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state !is SavedState) {
            return
        }

        val ss = state as SavedState?
        scrollX = ss!!.scrollOffset
    }

    /**
     * Reset options that may be modified by built-in transformers.
     *
     *
     * Note: Other options, e.g. extraVisibleChilds, that possibly set by
     * custom transformers may have to reset by developers manually.
     */
    fun resetOptions() {
        setScroller(null)
        setDrawOrder(CarouselView1.DrawOrder.FirstBack)
    }

    /**
     * Returns the transformer currently in use.
     *
     * @return
     */
    fun getTransformer(): CarouselView1.ViewTransformer {
        return mTransformer
    }

    /**
     * Provide a custom transformation implementation.
     *
     * @param transformer
     * @return
     */
    fun setTransformer(transformer: CarouselView1.ViewTransformer?): CarouselLayoutManager {
        val oldTransformer = this.mTransformer
        this.mTransformer = transformer ?: DEFAULT_TRANSFORMER
        if (mTransformer !== oldTransformer) {
            resetOptions()
            transformer!!.onAttach(this)
        }
        return this
    }

    /**
     * Set the number of extra children per side to be preserved and managed by transformations.
     *
     *
     * Number of cached children views = (num + 2) * 2 + 1
     *
     * @param carouselView
     * @param num
     * @return
     */
    fun setExtraVisibleChilds(carouselView: CarouselView1, num: Int): CarouselLayoutManager {
        extraVisibleChilds = num
        carouselView.setItemViewCacheSize((num + 2) * 2 + 1)
        return this
    }

    /**
     * Returns whether the items are recurring.
     *
     * @return
     */
    fun isInfinite(): Boolean {
        return mInfinite
    }

    /**
     * Set whether the items are recurring.
     *
     * @param infinite
     * @return
     */
    fun setInfinite(infinite: Boolean): CarouselLayoutManager {
        this.mInfinite = infinite
        return this
    }

    /**
     * Return the scroller currently in use.
     *
     * @return
     */
    fun getScroller(): CarouselView1.Scroller? {
        return mScroller
    }

    /**
     * Set the scrolling behaviour as the given implementation.
     *
     * @param scroller
     * @return
     */
    fun setScroller(scroller: CarouselView1.Scroller?): CarouselLayoutManager {
        mScroller = scroller ?: DEFAULT_SCROLLER
        return this
    }

    /**
     * Returns the drawing order of the centermost item.
     *
     * @return
     */
    fun getDrawOrder(): CarouselView1.DrawOrder {
        return mDrawOrder
    }

    /**
     * Set the drawing order of the centermost item.
     *
     * @param drawOrder
     * @return
     */
    fun setDrawOrder(drawOrder: CarouselView1.DrawOrder): CarouselLayoutManager {
        mDrawOrder = drawOrder
        return this
    }

    /**
     * Set an OnItemClickListener.
     *
     * @param onItemClickListener
     * @return
     */
    fun setOnItemClickListener(onItemClickListener: CarouselView1.OnItemClickListener): CarouselLayoutManager {
        mOnItemClickListener = onItemClickListener
        return this
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    /**
     * @return true
     */
    override fun canScrollHorizontally(): Boolean {
        return true
    }

    /**
     * @return false
     */
    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        var dx = dx
        if (mScroller != null) dx = mScroller!!.tweakScrollDx(dx)

        if (!mInfinite) {
            if (scrollX + dx < 0) {
                dx = if (scrollX > 0) -scrollX else 0
            } else {
                val rightmostOffset = mDecoratedChildWidth * (itemCount - 1)
                if (scrollX + dx > rightmostOffset) {
                    dx = if (scrollX < rightmostOffset) rightmostOffset - scrollX else 0
                }
            }
        }
        //		log("scroll pos " + mScrollOffset + ", off " + dx);
        if (dx != 0) {
            scrollX += dx
            fillChildrenView(recycler, state)
        }

        if (mScroller != null) dx = mScroller!!.inverseTweakScrollDx(dx)
        return dx
    }

    private fun positionOfIndex(childIndex: Int): Int {
        return childIndex
    }

    /**
     * Returns a position based on a given pixel.
     *
     * @param pixel
     * @return
     */
    protected fun pixelToPosition(pixel: Int): Float {
        return if (mDecoratedChildWidth != 0) {
            pixel.toFloat() / mDecoratedChildWidth
        } else {
            0F
        }
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        mDecoratedChildWidth = 0
        mDecoratedChildHeight = 0
        super.onMeasure(recycler, state, widthSpec, heightSpec)
        adjustHostDimension(recycler, state, widthSpec, heightSpec)
        log("carousel width = $mMeasuredWidth, height = $mMeasuredHeight")

        if (CarouselView1.isDebug()) {
            val widthMode = View.MeasureSpec.getMode(widthSpec)
            val heightMode = View.MeasureSpec.getMode(heightSpec)
            val widthSize = View.MeasureSpec.getSize(widthSpec)
            val heightSize = View.MeasureSpec.getSize(heightSpec)
            Log.d(TAG, String.format("carousel onMeasure %d %d %d %d", widthMode, heightMode, widthSize, heightSize))
        }
    }

    internal fun adjustHostDimension(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)

        val width: Int
        val height: Int

        mMeasuredWidth = 0
        mMeasuredHeight = 0
        measureChildSize(recycler)

        val neededWidth = Math.max(mDecoratedChildWidth, minimumWidth)
        val neededHeight = Math.max(mDecoratedChildHeight, minimumHeight)

        when (widthMode) {
            View.MeasureSpec.EXACTLY -> {
                width = widthSize
            }

            View.MeasureSpec.AT_MOST -> {
                width = Math.min(neededWidth, widthSize)
            }

            View.MeasureSpec.UNSPECIFIED -> {
                width = neededWidth
            }
            else -> {
                width = neededWidth
            }
        }

        when (heightMode) {
            View.MeasureSpec.EXACTLY -> {
                height = heightSize
            }

            View.MeasureSpec.AT_MOST -> {
                height = Math.min(neededHeight, heightSize)
            }

            View.MeasureSpec.UNSPECIFIED -> {
                height = neededHeight
            }
            else -> {
                height = neededHeight
            }
        }
        mMeasuredWidth = width
        mMeasuredHeight = height

        setMeasuredDimension(width, height)
    }

    override fun setMeasuredDimension(widthSize: Int, heightSize: Int) {
        super.setMeasuredDimension(widthSize, heightSize)
        mMeasuredWidth = widthSize
        mMeasuredHeight = heightSize
    }

    private fun measureChildSize(recycler: RecyclerView.Recycler?) {
        if (itemCount > 0 && (childCount == 0 || mDecoratedChildWidth * mDecoratedChildHeight == 0)) {
            // Scrap measure one child
            val scrap = recycler!!.getViewForPosition(0)
            addView(scrap)
            measureChildWithMargins(scrap, 0, 0)

            // Assume every child has the same size.
            mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap)
            mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap)

            log("child width = $mDecoratedChildWidth, height = $mDecoratedChildHeight, my width = $width")
            log("scrap width = " + scrap.measuredWidth + ", height = " + scrap.measuredHeight)

            detachAndScrapView(scrap, recycler)
        }
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        var widthUsed = widthUsed
        var heightUsed = heightUsed
        val lp = child.layoutParams as RecyclerView.LayoutParams

        val insets = Rect()
        calculateItemDecorationsForChild(child, insets)
        widthUsed += insets.left + insets.right
        heightUsed += insets.top + insets.bottom

        val width = if (mRecyclerView != null) mRecyclerView!!.width else mMeasuredWidth
        val height = if (mRecyclerView != null) mRecyclerView!!.height else mMeasuredHeight

        val widthSpec = LayoutManager.getChildMeasureSpec(
            width,
            paddingLeft + paddingRight +
                    lp.leftMargin + lp.rightMargin + widthUsed, lp.width,
            false && canScrollHorizontally()
        )
        val heightSpec = LayoutManager.getChildMeasureSpec(
            height,
            paddingTop + paddingBottom +
                    lp.topMargin + lp.bottomMargin + heightUsed, lp.height,
            false && canScrollVertically()
        )
        child.measure(widthSpec, heightSpec)
    }

    private fun updateWindowVariables() {
        when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.LEFT -> {
                mLeftOffset = paddingLeft
            }

            Gravity.RIGHT -> {
                mLeftOffset = mMeasuredWidth - paddingRight - mDecoratedChildWidth
            }

            Gravity.CENTER_HORIZONTAL -> {
                mLeftOffset = (mMeasuredWidth - paddingLeft - paddingRight - mDecoratedChildWidth) / 2 + paddingLeft
            }
            else -> {
                mLeftOffset = (mMeasuredWidth - paddingLeft - paddingRight - mDecoratedChildWidth) / 2 + paddingLeft
            }
        }

        when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> {
                mTopOffset = paddingTop
            }

            Gravity.BOTTOM -> {
                mTopOffset = mMeasuredHeight - paddingBottom - mDecoratedChildHeight
            }

            Gravity.CENTER_VERTICAL -> {
                mTopOffset = (mMeasuredHeight - paddingTop - paddingBottom - mDecoratedChildHeight) / 2 + paddingTop
            }
            else -> {
                mTopOffset = paddingTop
            }
        }
    }

    /**
     * Major layout pass to layout children views.
     *
     * @param recycler
     * @param state
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        //		super.onLayoutChildren(recycler, state);
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler!!)
            return
        }
        logv("onLayoutChildren ==============", Exception())
        measureChildSize(recycler)
        updateWindowVariables()
        if (state!!.didStructureChange() || mHasDatasetUpdated || mScrollPositionUpdated) {
            detachAndScrapAttachedViews(recycler!!)
            mHasDatasetUpdated = false
            mScrollPositionUpdated = false
        }
        fillChildrenView(recycler, state)

        // queue pending tasks to execute after current layout pass
        logv("onLayoutChildren : Queue Pending Tasks")
        val pendingTasks: Queue<Runnable>
        synchronized(this) {
            pendingTasks = mPendingTasks
            mPendingTasks = LinkedList() // swap mPendingTasks with a local variable to prevent infinite looping
        }
        post(Runnable {
            while (!pendingTasks.isEmpty()) {
                pendingTasks.poll().run()
            }
        })

        logv("onLayoutChildren ============== end")
    }

    /**
     * Returns an adapter position based on a given absolute position.
     *
     * @param position
     * @return
     */
    fun translatePosition(position: Int): Int {
        var position = position
        if (!mInfinite) return position
        val itemCount = itemCount
        position %= itemCount
        if (position < 0) position += itemCount
        return position
    }

    /**
     * Returns whether a given absolution position is valid, which the rules are also based on current value of [.isInfinite].
     *
     * @param position
     * @return
     */
    fun isValidPosition(position: Int): Boolean {
        val itemCount = itemCount
        return if (itemCount == 0) false else mInfinite || 0 <= position && position < itemCount
    }

    //	public View getViewAtPosition(int position) {
    //		WeakReference<View> viewRef = mViewAtPosition.get(position);
    //		return viewRef != null ? viewRef.get() : null;
    //	}

    /**
     * Re-layout all children views. This is one of the major process during layout, and are frequently called,
     * and thus maximum optimization is needed.
     *
     * @param recycler
     * @param state
     */
    private fun fillChildrenView(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        logv("fillChildrenView ==============")
        // use MultiSparseArray instead of SparseArray because getPosition can be repeated, otherwise viewCache.put() would replace the views and cause memory leak
        val viewCache =
            MultiSparseArray<View>(childCount) // at the end of iteration, all views in viewCache will be recycled

        //		mPerformanceTimer.time("fillChildrenView start");

        // Cache all views by their existing position and detach them
        logv("getChildCount() = $childCount")
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            val position = getPosition(child!!)
            viewCache.put(position, child)
            logv(String.format("viewCache[%d] = %s", position, child))
            detachView(child)
        }

        //		mPerformanceTimer.time("fillChildrenView detachView");

        val leftmostPosition = leftmostVisiblePosition
        val rightmostPosition = rightmostVisiblePosition
        val currentPosition = currentPosition

        // ASSERT if mInfinite, 0 <= leftmostPosition <= rightmostPosition < getItemCount()

        if (leftmostPosition <= rightmostPosition) {
            // draw all the children views that are in range of [leftmostPosition, rightmostPosition]

            when (mDrawOrder) {
                CarouselView1.DrawOrder.FirstBack, CarouselView1.DrawOrder.FirstFront -> {
                    val posStart: Int
                    val posEnd: Int
                    val posInc: Int
                    if (mDrawOrder === CarouselView1.DrawOrder.FirstFront) {
                        posStart = rightmostPosition
                        posEnd = leftmostPosition
                        posInc = -1
                    } else {
                        posStart = leftmostPosition
                        posEnd = rightmostPosition
                        posInc = 1
                    }

                    var pos = posStart - posInc
                    do {
                        pos += posInc
                        drawChild(pos, viewCache, recycler, state)
                    } while (pos != posEnd)
                }

                CarouselView1.DrawOrder.CenterFront -> {
                    var left = leftmostPosition
                    var right = rightmostPosition
                    while (currentPosition - left > right - currentPosition) {
                        drawChild(left, viewCache, recycler, state)
                        ++left
                    }
                    while (currentPosition - left < right - currentPosition) {
                        drawChild(right, viewCache, recycler, state)
                        --right
                    }
                    // now currentPosition - left == right - currentPosition
                    while (left < right) {
                        drawChild(left, viewCache, recycler, state)
                        drawChild(right, viewCache, recycler, state)
                        ++left
                        --right
                    }
                    drawChild(currentPosition, viewCache, recycler, state)
                }

                CarouselView1.DrawOrder.CenterBack -> {
                    drawChild(currentPosition, viewCache, recycler, state)
                    var left = currentPosition - 1
                    var right = rightmostPosition
                    while (left >= leftmostPosition || right <= rightmostPosition) {
                        if (left >= leftmostPosition) {
                            drawChild(left, viewCache, recycler, state)
                            --left
                        }
                        if (right <= rightmostPosition) {
                            drawChild(right, viewCache, recycler, state)
                            ++right
                        }
                    }
                }
            }

        }

        //		mPerformanceTimer.time("fillChildrenView drawView");

        // Recycle views that are not re-attached
        for (i in viewCache.size() - 1 downTo 0) {
            logv(String.format("recycleView (%d) %s", viewCache.keyAt(i), viewCache.valuesAt(i)))
            //			mViewAtPosition.remove(viewCache.keyAt(i));
            //			recycler.recycleView(viewCache.valueAt(i));
            for (v in viewCache.valuesAt(i)) {
                recycler!!.recycleView(v)
            }
        }

        //		mPerformanceTimer.time("fillChildrenView recycleView");

        logv("getChildCount() = $childCount")

        logv("fillChildrenView ============== end")
    }

    /**
     * Draw a children view at a given position. This is one of the major process during layout, and are frequently called,
     * and thus maximum optimization is needed.
     *
     * @param position
     * @param viewCache
     * @param recycler
     * @param state
     */
    private fun drawChild(
        position: Int,
        viewCache: MultiSparseArray<View>,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ) {
        logv(String.format("drawChild (%d)", position))
        // layout this position

        val translatedPosition = translatePosition(position)
        var view = viewCache.pop(translatedPosition)
        if (view == null) {
            // retrieve a new/recycled view
            view = recycler!!.getViewForPosition(translatedPosition)
            view.setOnClickListener { v ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(null, v, position, translatePosition(position))
                }
            }
            addView(view)
            //			mViewAtPosition.put(position, new WeakReference<>(view));
            logv(String.format("addView (%d [%d]) %s", position, translatedPosition, view))

        } else {
            // re-attach the cached view
            attachView(view)
            //			viewCache.remove(translatedPosition);
        }

        measureChildWithMargins(view, 0, 0)

        if (state!!.isPreLayout) {
            return
        }

        layoutDecorated(
            view, mLeftOffset, mTopOffset,
            mLeftOffset + mDecoratedChildWidth,
            mTopOffset + mDecoratedChildHeight
        )

        mTransformer.transform(view, -(pixelToPosition(scrollX) - position))
    }

    override fun scrollToPosition(position: Int) {
        if (mDecoratedChildWidth == 0 && itemCount > 0) {
            mPendingTasks.add(Runnable { scrollToPosition(position) })
            return
        }
        val newOffset = position * mDecoratedChildWidth
        log("scrollToPosition " + position + "scrollOffset " + scrollX + " -> " + newOffset)
        if (Math.abs(newOffset - scrollX) > mDecoratedChildWidth * 1.5) {
            mScrollPositionUpdated = true
            log("scrollToPosition " + position + "set mScrollPositionUpdated")
        }
        scrollX = newOffset
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && mRecyclerView != null && !mRecyclerView!!.isInLayout) {
            requestLayout()
        }
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?, state: RecyclerView.State?,
        position: Int
    ) {
        var position = position
        //		LinearSmoothScroller linearSmoothScroller =
        //				new LinearSmoothScroller(recyclerView.getContext()) {
        //					@Override
        //					public PointF computeScrollVectorForPosition(int targetPosition) {
        //						return CarouselLayoutManager.this
        //								.computeScrollVectorForPosition(targetPosition);
        //					}
        //				};
        //		linearSmoothScroller.setTargetPosition(position);
        //		startSmoothScroll(linearSmoothScroller);
        log("smoothScrollToPosition $position $recyclerView")
        var minScrollOffset = Integer.MAX_VALUE
        val nChilds = itemCount

        if (mDecoratedChildWidth == 0 && nChilds > 0) {
            val finalPosition = position
            mPendingTasks.add(Runnable { smoothScrollToPosition(recyclerView, state, finalPosition) })
            return
        }
        if (mDecoratedChildWidth * nChilds == 0) return

        if (!isInfinite()) {
            position = Math.max(0, Math.min(nChilds - 1, position))
        } else {
            position %= nChilds
        }

        for (round in -1..1) {
            if (isInfinite() || round == 0) {
                val offset =
                    (position + round * nChilds) * mDecoratedChildWidth - scrollX % (mDecoratedChildWidth * nChilds)
                if (Math.abs(offset) < Math.abs(minScrollOffset))
                    minScrollOffset = offset
            }
        }
        recyclerView!!.smoothScrollBy(minScrollOffset, 0)
    }

    /**
     * NOT USED.
     *
     * @param targetPosition
     * @return
     */
    private fun computeScrollVectorForPosition(targetPosition: Int): PointF {
        //		if (getChildCount() == 0) {
        //			return null;
        //		}
        val targetOffset = targetPosition * mDecoratedChildWidth
        val direction = if (targetOffset < scrollX) -1 else 1
        //		if (mOrientation == HORIZONTAL) {
        return PointF(direction.toFloat() /*targetOffset - mScrollOffset*/, 0f)
        //		} else {
        //			return new PointF(0, direction);
        //		}
    }

    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
        super.onAdapterChanged(oldAdapter, newAdapter)
        removeAllViews()
    }

    override fun onItemsChanged(recyclerView: RecyclerView) {
        super.onItemsChanged(recyclerView)
        mHasDatasetUpdated = true
    }

    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsUpdated(recyclerView, positionStart, itemCount)
        mHasDatasetUpdated = true
        for (i in 0 until itemCount) {
            val view = findViewByPosition(positionStart + i)
            view?.forceLayout()
            //			removeAndRecycleView(view, recyclerView.recyc);
        }
    }

    override fun onItemsAdded(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsAdded(recyclerView, positionStart, itemCount)
        mHasDatasetUpdated = true
    }

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsRemoved(recyclerView, positionStart, itemCount)
        mHasDatasetUpdated = true
    }

    override fun onItemsMoved(recyclerView: RecyclerView, from: Int, to: Int, itemCount: Int) {
        super.onItemsMoved(recyclerView, from, to, itemCount)
        mHasDatasetUpdated = true
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        mRecyclerView = view
    }

    override fun onDetachedFromWindow(view: RecyclerView, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        mRecyclerView = null
    }

    /**
     * Add a given runnable action to the underlying CarouselView1.
     *
     * @param action
     * @return whether the given action is successfully queued
     * @see CarouselView1.post
     */
    protected fun post(action: Runnable): Boolean {
        if (mRecyclerView == null) return false

        mRecyclerView!!.post(action)
        return true
    }

    internal class SavedState : Parcelable {

        var scrollOffset: Int = 0

        constructor()

        private constructor(`in`: Parcel) {
            scrollOffset = `in`.readInt()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(scrollOffset)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> = object : Parcelable.Creator<SavedState?> {
                override fun createFromParcel(source: Parcel): SavedState? {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }

    }

    companion object {
        val DEFAULT_TRANSFORMER: CarouselView1.ViewTransformer = ImmutableTransformer(FlatMerryGoRoundTransformer())
        val DEFAULT_SCROLLER: CarouselView1.Scroller = NormalScroller()
        private val TAG = CarouselLayoutManager::class.java.simpleName

        private fun log(format: String, vararg args: Any) {
            if (CarouselView1.isDebug()) {
                if (args.size > 0) {
                    Log.d(TAG, String.format(format, *args))
                } else {
                    Log.d(TAG, format)
                }
            }
        }

        private fun logv(format: String, vararg args: Any) {
            if (CarouselView1.isDebug()) {
                if (args.size > 0) {
                    Log.v(TAG, String.format(format, *args))
                } else {
                    Log.v(TAG, format)
                }
            }
        }
    }
}
