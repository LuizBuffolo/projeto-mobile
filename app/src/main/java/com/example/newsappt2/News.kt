package com.example.newsappt2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val newsUrl: String
) : Parcelable {

    @Parcelize
    data class Source(
        val id: String?,
        val name: String
    ): Parcelable

}