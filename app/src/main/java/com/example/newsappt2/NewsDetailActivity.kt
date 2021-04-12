package com.example.newsappt2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newsappt2.databinding.ActivityNewsDetailBinding

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val NEWS_DETAIL_KEY = "NEWS_DETAIL_KEY"
    }

    lateinit var binding: ActivityNewsDetailBinding
    private lateinit var viewModel: NewsDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedNews = intent.getParcelableExtra<News>(NEWS_DETAIL_KEY)
        val viewModelFactory = NewsDetailViewModelFactory(receivedNews!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsDetailViewModel::class.java)

        viewModel.newsDetail.observe(this) { news ->
            binding.newsTitle.text = news.title

            if (news.description != null) binding.newsDescription.text = news.description
            else binding.newsDescription.visibility = View.GONE

            if (news.content != null) binding.newsContent.text = news.content
            else binding.newsContent.visibility = View.GONE

            binding.newsSource.text = getString(
                R.string.news_source,
                news.author ?: getString(R.string.unknown),
                news.source.name
            )

            binding.newsLastUpdate.text =
                getString(R.string.news_last_updated, news.lastUpdated)

            Glide
                .with(this)
                .load(news.imageUrl)
                .placeholder(R.drawable.ic_no_image)
                .into(binding.newsImage)

            binding.btnViewEntireNews.setOnClickListener {
                viewModel.onViewEntireNewsClicked(news)
            }

            binding.btnShareNews.setOnClickListener {
                viewModel.onShareNewsClicked(news)
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

        viewModel.navigationShareNews.observe(this) { newsurlEvent ->
            newsurlEvent.handleEvent { newsUrl ->
                val shareNewsIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "See this news!\n${newsUrl}")
                }
                if (shareNewsIntent.resolveActivity(packageManager) != null) {
                    startActivity(shareNewsIntent)
                } else {
                    viewModel.onShareNewsResolveActivityFail()
                }
            }
        }

        viewModel.message.observe(this) { messageEvent ->
            messageEvent.handleEvent { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

}