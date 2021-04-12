package com.example.newsappt2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappt2.databinding.ActivityNewsListBinding

class NewsListActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsListViewModel

    private lateinit var binding: ActivityNewsListBinding
    private val adapter = NewsListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)

        binding.recyclerViewNews.adapter = adapter
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(this)

        binding.emptyState.btnTryAgain.setOnClickListener {
            viewModel.onTryAgainClicked()
        }

        viewModel.newsList.observe(this) { newsList ->
            Log.i("LiveDataEvent", "Passou pelo newsList")
            adapter.setItems(newsList) { news ->
                viewModel.onItemClicked(news)
            }
        }

        viewModel.screenState.observe(this) { screenState ->
            Log.i("LiveDataEvent", "Passou screen state")
            when (screenState) {
                ScreenState.SUCCESS -> {
                    binding.progressIndicator.visibility = View.GONE
                    binding.emptyState.emptyStateIndicator.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.VISIBLE
                }
                ScreenState.ERROR -> {
                    binding.progressIndicator.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.GONE
                    binding.emptyState.emptyStateIndicator.visibility = View.VISIBLE
                }
                ScreenState.LOADING -> {
                    binding.recyclerViewNews.visibility = View.GONE
                    binding.emptyState.emptyStateIndicator.visibility = View.GONE
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                else -> throw IllegalArgumentException("Unknown ScreenState")
            }
        }

        viewModel.navigationDetail.observe(this) { newsEvent ->
            Log.i("LiveDataEvent", "Caiu no navigationDetail")

            newsEvent.handleEvent { news ->
                Log.i("LiveDataEvent", "Lidou com o evento")
                val navigateToDetailsIntent =
                    Intent(this, NewsDetailActivity::class.java).apply {
                        putExtra(NewsDetailActivity.NEWS_DETAIL_KEY, news)
                    }
                startActivity(navigateToDetailsIntent)
            }
        }
    }
}