package com.example.newsappt2

import android.net.Uri
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsDetailViewModel(news: News) : ViewModel() {

    private val _newsDetail: MutableLiveData<News> = MutableLiveData()
    val newsDetail: LiveData<News>
        get() = _newsDetail

    private val _navigationEntireNews: MutableLiveData<Event<Uri>> = MutableLiveData()
    val navigationEntireNews: LiveData<Event<Uri>>
        get() = _navigationEntireNews

    private val _navigationShareNews: MutableLiveData<Event<String>> = MutableLiveData()
    val navigationShareNews: LiveData<Event<String>>
        get() = _navigationShareNews

    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>>
        get() = _message

    init {
        _newsDetail.value = news
    }

    fun onViewEntireNewsClicked(news: News) {
        if (URLUtil.isValidUrl(news.newsUrl)) {
            val webpage: Uri = Uri.parse(news.newsUrl)
            _navigationEntireNews.value = Event(webpage)
        } else {
            _message.value = Event("Invalid URL")
        }
    }

    fun onShareNewsClicked(news: News) {
        _navigationShareNews.value = Event(news.newsUrl)
    }

    fun onShowNewsResolveActivityFail() {
        _message.value = Event("You need to install a browser")
    }

    fun onShareNewsResolveActivityFail() {
        _message.value = Event("You need to install a message app")
    }

}