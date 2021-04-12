package com.example.newsappt2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsListViewModel() : ViewModel() {

    private val _newsList: MutableLiveData<List<News>> = MutableLiveData()
    val newsList: LiveData<List<News>>
        get() = _newsList

    private val _screenState: MutableLiveData<ScreenState> = MutableLiveData()
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _navigationDetail: MutableLiveData<Event<News>> = MutableLiveData()
    val navigationDetail: LiveData<Event<News>>
        get() = _navigationDetail


    private val service = RetrofitInitializer.getNewsApiService()

    init {
        getDataFromService()
    }

    fun onTryAgainClicked() {
        getDataFromService()
    }

    fun onItemClicked(news: News) {
        _navigationDetail.value = Event(news)
    }

    private fun getDataFromService() {
        //exibir loading
        _screenState.value = ScreenState.LOADING

        service.getTopHeadlines("br").enqueue(object : Callback<NewsList> {
            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                // validar se o retorno obteve sucesso
                if (response.isSuccessful && response.body() != null) {
                    // exibir dados para o usuário
                    _newsList.value = response.body()!!.items as ArrayList<News>
                    _screenState.value = ScreenState.SUCCESS
                } else {
                    _screenState.value = ScreenState.ERROR
                }
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                _screenState.value = ScreenState.ERROR
            }

        })
    }
}