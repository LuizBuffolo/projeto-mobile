package com.example.newsappt2.presentation.scenes.newsdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.newsappt2.R
import com.example.newsappt2.common.NewsAppApplication
import com.example.newsappt2.data.model.News
import com.example.newsappt2.databinding.ActivityNewsDetailBinding
import com.example.newsappt2.hide
import com.example.newsappt2.presentation.common.ScreenState
import com.example.newsappt2.presentation.scenes.login.LoginActivity
import com.example.newsappt2.show
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val NEWS_DETAIL_KEY = "NEWS_DETAIL_KEY"
    }

    lateinit var binding: ActivityNewsDetailBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: NewsDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as NewsAppApplication)
            .applicationComponent
            .inject(this)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.username.text = firebaseAuth.currentUser!!.email

        binding.buttonSignOut.setOnClickListener{
            firebaseAuth.signOut()

            val signOutIntent = Intent(this@NewsDetailActivity, LoginActivity::class.java)
            signOutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(signOutIntent)
            finish()
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsDetailViewModel::class.java)

        val receivedNewsId: Int = intent.getIntExtra(NEWS_DETAIL_KEY, -1)
        viewModel.onIdReceived(receivedNewsId)


        binding.emptyState.btnTryAgain.setOnClickListener {
            viewModel.onTryAgainClicked()
        }

        viewModel.screenState.observe(this) { screenState ->
            when (screenState) {
                is ScreenState.Success -> {
                    displayData(screenState.data)
                    binding.progressIndicator.hide()
                    binding.emptyState.emptyStateIndicator.hide()
                    binding.successState.show()
                }
                is ScreenState.Error -> {
                    binding.progressIndicator.hide()
                    binding.successState.hide()
                    binding.emptyState.emptyStateIndicator.show()

                }
                is ScreenState.Loading -> {
                    binding.successState.hide()
                    binding.emptyState.emptyStateIndicator.hide()
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
            messageEvent.handleEvent { messageId ->
                Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayData(news: News) {
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

}