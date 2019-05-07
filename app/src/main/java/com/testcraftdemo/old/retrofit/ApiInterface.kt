package com.testcraftdemo.old.retrofit

import com.testcraftdemo.old.models.QuestionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("GetRandomQuestions")
    fun getTopRatedMovies(@Query("TokenId") tokenid: String): Call<QuestionResponse>

}
