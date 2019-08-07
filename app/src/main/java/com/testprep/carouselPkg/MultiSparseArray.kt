package com.testprep.carouselPkg

import android.util.SparseArray
import java.util.*

/**
 * Multi-dimension FIFO SparseArray.
 *
 * @see SparseArray
 *
 *
 * @author  sunny-chung
 */
class MultiSparseArray<E> {

    internal var mArray: SparseArray<ArrayList<E>>
    internal var mSize = 0

    constructor() {
        mArray = SparseArray()
    }

    constructor(initialCapacity: Int) {
        mArray = SparseArray(initialCapacity)
    }

    fun put(key: Int, value: E) {
        var values: ArrayList<E>? = mArray.get(key)
        if (values == null) values = ArrayList()
        values.add(value)
        ++mSize
        mArray.put(key, values)
    }

    operator fun get(key: Int): E? {
        val values = mArray.get(key)
        return if (values != null && values.size > 0) values[0] else null
    }

    fun pop(key: Int): E? {
        val values = mArray.get(key)
        if (values != null && values.size > 0) {
            val value = values[0]
            values.removeAt(0)
            --mSize
            return value
        }
        return null
    }

    fun remove(key: Int) {
        val values = mArray.get(key)
        if (values != null && values.size > 0) {
            values.removeAt(0)
            --mSize
            // let the empty array to stay in memory
        }
    }

    fun size(): Int {
        //        return mSize;
        return mArray.size()
    }

    fun keyAt(index: Int): Int {
        return mArray.keyAt(index)
    }

    fun valuesAt(index: Int): ArrayList<E> {
        return mArray.valueAt(index)
    }
}
