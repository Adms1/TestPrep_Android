package com.testcraft.testcraft.retrofit

import android.util.Log
import retrofit2.Call
import retrofit2.Callback

abstract class CallbackWithRetry<T>(private val call: Call<T>) : Callback<T> {
    private var retryCount = 0
    fun onFailure(t: Throwable?) {
        Log.e(TAG, t!!.localizedMessage)
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(
                TAG,
                "Retrying... ($retryCount out of $TOTAL_RETRIES)"
            )
            retry()
        }
    }

    private fun retry() {
        call.clone().enqueue(this)
    }

    companion object {
        private const val TOTAL_RETRIES = 3
        private val TAG = CallbackWithRetry::class.java.simpleName
    }

}
