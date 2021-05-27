package com.example.newsappt2.presentation.scenes.searchnews

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
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class SearchNewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val timer: Timer,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private var timerTask: TimerTask? = null

    private val _screenState: MutableLiveData<ScreenState<List<News>>> = MutableLiveData()
    val screenState: LiveData<ScreenState<List<News>>>
        get() = _screenState

    private val _navigationEntireNews: MutableLiveData<Event<Uri>> = MutableLiveData()
    val navigationEntireNews: LiveData<Event<Uri>>
        get() = _navigationEntireNews

    private val _message: MutableLiveData<Event<Int>> = MutableLiveData()
    val message: LiveData<Event<Int>>
        get() = _message

    fun onSearchEditTextChanged(text: CharSequence?) {
        timerTask?.cancel()
        timerTask = timer.schedule(1000) {
            searchNews(text.toString())
        }
    }

    fun onNewsItemClicked(clickedNews: News) {
        if (URLUtil.isValidUrl(clickedNews.newsUrl)) {
            val webpage: Uri = Uri.parse(clickedNews.newsUrl)
            _navigationEntireNews.value = Event(webpage)
        } else {
            _message.value = Event(R.string.invalid_url)
        }
    }

    fun onShowNewsResolveActivityFail() {
        _message.value = Event(R.string.need_install_browser)
    }

    private fun searchNews(searchText: String) {
        _screenState.postValue(ScreenState.Loading())

        repository.searchNews(searchText).subscribe(
            { _screenState.postValue(ScreenState.Success(it)) },
            { _screenState.postValue(ScreenState.Error()) }
        ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}