package com.example.newsappt2

import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("articles")
    val items: List<News>
)