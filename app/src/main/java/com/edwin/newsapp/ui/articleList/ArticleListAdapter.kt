package com.edwin.newsapp.ui.articleList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edwin.domain.model.Article
import com.edwin.newsapp.databinding.ArticleListItemBinding
import com.edwin.newsapp.extension.loadImage

class ArticleListAdapter(private val onClick: (Article) -> Unit) :
    ListAdapter<Article, ArticleListAdapter.ArticleListViewHolder>(ArticleDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder {
        val binding = ArticleListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }

    inner class ArticleListViewHolder(private val binding: ArticleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onClick(getItem(adapterPosition)) }
        }

        fun bind(article: Article) = with(binding) {
            articleTitle.text = article.title
            articleDescription.text = article.description
            articlePublishDate.text = article.publishedAt
            articleImage.loadImage(itemView.context, article.urlToImage)
        }

    }

    object ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.title == newItem.urlToImage

        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
    }
}