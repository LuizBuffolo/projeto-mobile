package com.example.newsappt2.presentation.scenes.newsdetail

import android.net.Uri
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsappt2.R
import com.example.newsappt2.data.model.News
import com.example.newsappt2.data.repository.NewsRepository
import com.example.newsappt2.presentation.common.Event
import com.example.newsappt2.presentation.common.ScreenState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class NewsDetailViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private var newsId: Int = -1

    private val _screenState: MutableLiveData<ScreenState<News>> = MutableLiveData()
    val screenState: LiveData<ScreenState<News>>
        get() = _screenState

    private val _navigationEntireNews: MutableLiveData<Event<Uri>> = MutableLiveData()
    val navigationEntireNews: LiveData<Event<Uri>>
        get() = _navigationEntireNews

    private val _navigationShareNews: MutableLiveData<Event<String>> = MutableLiveData()
    val navigationShareNews: LiveData<Event<String>>
        get() = _navigationShareNews

    private val _message: MutableLiveData<Event<Int>> = MutableLiveData()
    val message: LiveData<Event<Int>>
        get() = _message


    fun onIdReceived(newsId: Int) {
        this.newsId = newsId
        getNewsDetail()
    }

    private fun getNewsDetail() {
        if (newsId < 0) throw IllegalArgumentException("newsId must be > 0")

        _screenState.value = ScreenState.Loading()

        repository.getNews(newsId)
            .subscribe(
                { _screenState.value = ScreenState.Success(it) },
                { _screenState.value = ScreenState.Error() }
            ).addTo(compositeDisposable)
    }

    fun onTryAgainClicked() {
        getNewsDetail()
    }

    fun onViewEntireNewsClicked(news: News) {
        if (URLUtil.isValidUrl(news.newsUrl)) {
            val webpage: Uri = Uri.parse(news.newsUrl)
            _navigationEntireNews.value = Event(webpage)
        } else {
            _message.value = Event(R.string.invalid_url)
        }
    }

    fun onShareNewsClicked(news: News) {
        _navigationShareNews.value = Event(news.newsUrl)
    }

    fun onShowNewsResolveActivityFail() {
        _message.value = Event(R.string.need_install_browser)
    }

    fun onShareNewsResolveActivityFail() {
        _message.value = Event(R.string.need_install_message_app)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}