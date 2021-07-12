package com.edwin.newsapp.ui.articleList.model

import androidx.paging.PagingData
import com.edwin.domain.model.Article
import com.edwin.domain.model.SortOrder

data class ArticleListViewState(
    val isLoading: Boolean = false,
    val pagingData: PagingData<Article> = PagingData.empty(),
    val sortOrder: SortOrder? = null
)