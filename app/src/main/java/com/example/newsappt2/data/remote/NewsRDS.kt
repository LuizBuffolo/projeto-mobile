package com.example.newsappt2.data.remote

import com.example.newsappt2.data.model.NewsList
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsRDS {

    companion object {
        private const val API_KEY: String = "apiKey=05e83b7e3eff4975b25193c2c9d020fa"
    }

    @GET("top-headlines?$API_KEY")
    fun getTopHeadlines(
        @Query("country") country: String
    ): Single<NewsList>

    @GET("everything?$API_KEY")
    fun getEverything(
        @Query("q") query: String
    ): Single<NewsList>

}
