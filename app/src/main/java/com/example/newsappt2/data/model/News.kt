package com.example.newsappt2.data.model

import com.google.gson.annotations.SerializedName

data class News(
    val title: String,
    val description: String?,
    val content: String?,
    val author: String?,
    val source: Source,
    @SerializedName("publishedAt")
    val lastUpdated: String,
    @SerializedName("urlToImage")
    val imageUrl: String?,
    @SerializedName("url")
    val newsUrl: String,
    val id: Int? = null
) {

    data class Source(
        val id: String?,
        val name: String
    )

}