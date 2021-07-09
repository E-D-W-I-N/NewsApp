package com.edwin.newsapp.ui.articleList.model

import com.edwin.domain.model.Article
import com.edwin.domain.model.SortOrder

data class ArticleListViewState(
    val isLoading: Boolean = false,
    val contacts: List<Article> = emptyList(),
    val sortOrder: SortOrder? = null
)