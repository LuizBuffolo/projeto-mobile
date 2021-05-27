package com.example.newsappt2.data.model

import com.example.newsappt2.data.model.News
import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("articles")
    val items: List<News>
)