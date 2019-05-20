package com.testprep.retrofit

import com.testprep.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebClient {

    companion object {

        private var retrofit: Retrofit? = null

        private var interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()

        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


        fun getClient(): Retrofit {

            interceptor.level = HttpLoggingInterceptor.Level.BODY

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}
