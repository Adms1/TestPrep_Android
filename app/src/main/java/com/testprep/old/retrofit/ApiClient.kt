package com.testprep.old.retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


class ApiClient {

    companion object {

        val BASE_URL = "http://content.testcraft.co.in/mobileservice.asmx/"
        private var retrofit: Retrofit? = null


        fun getClient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}
