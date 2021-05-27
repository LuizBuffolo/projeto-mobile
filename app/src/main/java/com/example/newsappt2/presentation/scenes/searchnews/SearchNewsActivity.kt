package com.example.newsappt2.presentation.scenes.searchnews

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappt2.*
import com.example.newsappt2.common.NewsAppApplication
import com.example.newsappt2.databinding.ActivitySearchNewsBinding
import com.example.newsappt2.presentation.common.NewsListAdapter
import com.example.newsappt2.presentation.common.ScreenState
import javax.inject.Inject

class SearchNewsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchNewsBinding
    lateinit var viewModel: SearchNewsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as NewsAppApplication)
            .applicationComponent
            .inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchNewsViewModel::class.java)

        val adapter = NewsListAdapter(this)
        binding.searchNewsList.adapter = adapter
        binding.searchNewsList.layoutManager = LinearLayoutManager(this)

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchEditTextChanged(text)
        }

        viewModel.screenState.observe(this) { lastScreenState ->
            when (lastScreenState) {
                is ScreenState.Success -> {
                    adapter.setItems(lastScreenState.data) { clickedNews ->
                        viewModel.onNewsItemClicked(clickedNews)
                    }
                    binding.emptyStateIndicator.hide()
                    binding.progressIndicator.hide()
                    binding.searchNewsList.show()
                }
                is ScreenState.Error -> {
                    binding.progressIndicator.hide()
                    binding.searchNewsList.hide()
                    binding.emptyStateIndicator.show()
                }
                is ScreenState.Loading -> {
                    binding.searchNewsList.hide()
                    binding.emptyStateIndicator.hide()
                    binding.progressIndicator.show()
                }
            }
        }

        viewModel.navigationEntireNews.observe(this) { webpageEvent ->
            webpageEvent.handleEvent { webpage ->
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    viewModel.onShowNewsResolveActivityFail()
                }
            }
        }

        viewModel.message.observe(this) { messageEvent ->
            messageEvent.handleEvent { messageId ->
                Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()
            }
        }
    }
}