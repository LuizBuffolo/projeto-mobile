package com.example.newsappt2.presentation.scenes.newslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsappt2.data.model.News
import com.example.newsappt2.data.repository.NewsRepository
import com.example.newsappt2.presentation.common.Event
import com.example.newsappt2.presentation.common.ScreenState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class NewsListViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _screenState: MutableLiveData<ScreenState<List<News>>> = MutableLiveData()
    val screenState: LiveData<ScreenState<List<News>>>
        get() = _screenState

    private val _navigationDetail: MutableLiveData<Event<Int>> = MutableLiveData()
    val navigationDetail: LiveData<Event<Int>>
        get() = _navigationDetail

    private val _navigationSearchNews: MutableLiveData<Event<Unit>> = MutableLiveData()
    val navigationSearchNews: LiveData<Event<Unit>>
        get() = _navigationSearchNews

    init {
        getNewsList()
    }

    fun onTryAgainClicked() {
        getNewsList()
    }

    fun onSearchNewsClicked() {
        _navigationSearchNews.value = Event(Unit)
    }

    fun onItemClicked(news: News) {
        _navigationDetail.value = Event(news.id!!)
    }

    private fun getNewsList() {
        _screenState.value = ScreenState.Loading()

        repository.getNewsList()
            .subscribe(
                { _screenState.value = ScreenState.Success(it) },
                { _screenState.value = ScreenState.Error() }
            ).addTo(compositeDisposable)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}