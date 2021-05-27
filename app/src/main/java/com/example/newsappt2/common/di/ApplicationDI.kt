package com.example.newsappt2.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsappt2.data.remote.NewsRDS
import com.example.newsappt2.presentation.common.ViewModelFactory
import com.example.newsappt2.presentation.scenes.newsdetail.NewsDetailActivity
import com.example.newsappt2.presentation.scenes.newsdetail.NewsDetailViewModel
import com.example.newsappt2.presentation.scenes.newslist.NewsListActivity
import com.example.newsappt2.presentation.scenes.newslist.NewsListViewModel
import com.example.newsappt2.presentation.scenes.searchnews.SearchNewsActivity
import com.example.newsappt2.presentation.scenes.searchnews.SearchNewsViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(newsDetailActivity: NewsDetailActivity)
    fun inject(newsListActivity: NewsListActivity)
    fun inject(searchNewsActivity: SearchNewsActivity)
}


@Module
class ApplicationModule() {

    @Provides
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun converterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun adapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    fun retrofit(
        converterFactory: GsonConverterFactory,
        adapterFactory: RxJava2CallAdapterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(adapterFactory)
        .build()

    @Provides
    fun newsRDS(retrofit: Retrofit): NewsRDS = retrofit.create(NewsRDS::class.java)

    @Provides
    fun timer(): Timer = Timer()

    @Provides
    @RepositoryStrings
    fun repositoryStrings(): List<String> = listOf("repository1", "repository2", "repository3")

    @Provides
    @CacheStrings
    fun cacheStrings(): List<String> = listOf("cache1", "cache2", "cache3")

}


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailViewModel::class)
    internal abstract fun newsDetailViewModel(viewModel: NewsDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsListViewModel::class)
    internal abstract fun newsListViewModel(viewModel: NewsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchNewsViewModel::class)
    internal abstract fun searchNewsViewModel(viewModel: SearchNewsViewModel): ViewModel

    //Add more ViewModels here
}