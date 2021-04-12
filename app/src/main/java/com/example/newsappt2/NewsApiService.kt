package com.example.newsappt2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    companion object {
        private const val API_KEY: String = "apiKey=b03532cd4be5416fb606da49973ab836"
    }

    @GET("top-headlines?$API_KEY")
    fun getTopHeadlines(
        @Query("country") country: String
    ): Call<NewsList>

}
