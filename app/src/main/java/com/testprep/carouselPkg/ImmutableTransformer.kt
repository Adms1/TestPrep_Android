package com.testprep.carouselPkg

import android.view.View

/**
 *
 * Immutable transformer.
 *
 * @author sunny-chung
 */

class ImmutableTransformer(private val mTransformer: CarouselView1.ViewTransformer) : CarouselView1.ViewTransformer {

    override fun onAttach(layoutManager: CarouselLayoutManager) {
        mTransformer.onAttach(layoutManager)
    }

    override fun transform(view: View, position: Float) {
        mTransformer.transform(view, position)
    }
}
