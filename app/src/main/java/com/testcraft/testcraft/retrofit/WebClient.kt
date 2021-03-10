package com.testcraft.testcraft.retrofit

import com.testcraft.testcraft.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WebClient {

    private var interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()

    var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor)
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS).build()

    fun buildService(): WebInterface {

        interceptor.level = HttpLoggingInterceptor.Level.BODY

//            if (retrofit == null) {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(WebInterface::class.java)
//            }

    }

//        fun buildService(): WebInterface {
//            return retrofit!!
//        }
}
