package com.example.newsappt2.data.cache

import com.example.newsappt2.data.model.News
import com.pacoworks.rxpaper2.RxPaperBook
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class NewsCDS @Inject constructor() {

    companion object {
        private const val NEWS_LIST_KEY = "NEWS_LIST_KEY"
    }

    fun upsertNewsList(newsList: List<News>): Completable =
        RxPaperBook.with().write(NEWS_LIST_KEY, newsList)

    fun getNewsList(): Single<List<News>> =
        RxPaperBook.with().read(NEWS_LIST_KEY)

    fun getNews(newsId: Int): Single<News> =
        getNewsList()
            .map { newsList ->
                newsList.firstOrNull { news ->
                    news.id == newsId
                }
            }
}