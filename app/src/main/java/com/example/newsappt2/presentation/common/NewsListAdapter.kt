package com.example.newsappt2.presentation.common

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.example.newsappt2.data.model.News
import com.example.newsappt2.R
import com.example.newsappt2.databinding.NewsItemBinding
import com.example.newsappt2.databinding.TextItemBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem

class NewsListAdapter(private val context: Context) : GroupAdapter<GroupieViewHolder>() {

    fun setItems(newsList: List<News>, clickListener: (News) -> Unit) {
        clear()
        newsList.forEach { news ->
            add(NewsItem(news, clickListener))
        }
    }

    fun addText(text: String, clickListener: (String) -> Unit) {
        add(TextItem(text, clickListener))
    }

    inner class NewsItem(private val news: News, val clickListener: (News) -> Unit) : BindableItem<NewsItemBinding>() {

        override fun bind(viewBinding: NewsItemBinding, position: Int) {
            viewBinding.newsItemTitle.text = news.title

            Glide
                .with(context)
                .load(news.imageUrl)
                .placeholder(R.drawable.ic_no_image)
                .centerCrop()
                .into(viewBinding.newsItemImage)

            viewBinding.root.setOnClickListener {
                clickListener(news)
            }

        }

        override fun getLayout(): Int = R.layout.news_item

        override fun initializeViewBinding(view: View): NewsItemBinding = NewsItemBinding.bind(view)

    }

    class TextItem(private val text: String, private val clickListener: (String) -> Unit) : BindableItem<TextItemBinding>() {

        override fun bind(viewBinding: TextItemBinding, position: Int) {
            viewBinding.txtTextItem.text = text

            viewBinding.root.setOnClickListener {
                clickListener(text)
            }
        }

        override fun getLayout(): Int = R.layout.text_item

        override fun initializeViewBinding(view: View): TextItemBinding = TextItemBinding.bind(view)

    }

}