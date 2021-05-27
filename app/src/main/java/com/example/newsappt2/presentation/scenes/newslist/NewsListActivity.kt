package com.example.newsappt2.presentation.scenes.newslist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappt2.*
import com.example.newsappt2.common.NewsAppApplication
import com.example.newsappt2.databinding.ActivityNewsListBinding
import com.example.newsappt2.presentation.common.NewsListAdapter
import com.example.newsappt2.presentation.common.ScreenState
import com.example.newsappt2.presentation.scenes.newsdetail.NewsDetailActivity
import com.example.newsappt2.presentation.scenes.searchnews.SearchNewsActivity
import javax.inject.Inject

class NewsListActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsListViewModel
    private lateinit var binding: ActivityNewsListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as NewsAppApplication)
            .applicationComponent
            .inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsListViewModel::class.java)

        val adapter = NewsListAdapter(this)
        binding.recyclerViewNews.adapter = adapter
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(this)

        binding.emptyState.btnTryAgain.setOnClickListener {
            viewModel.onTryAgainClicked()
        }

        binding.btnSearchNews.setOnClickListener {
            viewModel.onSearchNewsClicked()
        }

        viewModel.screenState.observe(this) { screenState ->
            Log.i("LiveDataEvent", "Passou screen state")
            when (screenState) {
                is ScreenState.Success -> {
                    adapter.setItems(screenState.data) { news ->
                        viewModel.onItemClicked(news)
                    }
                    binding.progressIndicator.hide()
                    binding.emptyState.emptyStateIndicator.hide()
                    binding.recyclerViewNews.show()
                }
                is ScreenState.Error -> {
                    binding.progressIndicator.hide()
                    binding.recyclerViewNews.hide()
                    binding.emptyState.emptyStateIndicator.show()
                }
                is ScreenState.Loading -> {
                    binding.recyclerViewNews.hide()
                    binding.emptyState.emptyStateIndicator.hide()
                    binding.progressIndicator.show()
                }
                else -> throw IllegalArgumentException("Unknown ScreenState")
            }
        }

        viewModel.navigationDetail.observe(this) { newsIdEvent ->
            Log.i("LiveDataEvent", "Caiu no navigationDetail")

            newsIdEvent.handleEvent { newsId ->
                Log.i("LiveDataEvent", "Lidou com o evento")
                val navigateToDetailsIntent =
                    Intent(this, NewsDetailActivity::class.java).apply {
                        putExtra(NewsDetailActivity.NEWS_DETAIL_KEY, newsId)
                    }
                startActivity(navigateToDetailsIntent)
            }
        }

        viewModel.navigationSearchNews.observe(this) { event ->
            event.handleEvent {
                val navigateToSearchNews = Intent(this, SearchNewsActivity::class.java)
                startActivity(navigateToSearchNews)
            }
        }
    }
}