package com.example.newsappt2.data.repository

import com.example.newsappt2.data.cache.NewsCDS
import com.example.newsappt2.data.model.News
import com.example.newsappt2.data.remote.NewsRDS
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsRDS: NewsRDS,
    private val newsCDS: NewsCDS,
) {

    fun getNewsList(): Single<List<News>> =
        newsRDS.getTopHeadlines("br")
            .flatMap {
                if (it.items.isEmpty())
                    Single.error(IllegalArgumentException("Remote data can't be null"))
                else
                    Single.just(it)
            }
            .map { it.items.mapIndexed { index, news -> news.copy(id = index) } }
            .flatMap { newsCDS.upsertNewsList(it).toSingleDefault(it) }
            .onErrorResumeNext { newsCDS.getNewsList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun searchNews(searchText: String): Single<List<News>> =
        newsRDS.getEverything(searchText)
            .flatMap {
                if (it.items.isEmpty())
                    Single.error(IllegalArgumentException("Remote data can't be null"))
                else
                    Single.just(it)
            }
            .map { it.items }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getNews(newsId: Int): Single<News> =
        newsCDS.getNews(newsId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}