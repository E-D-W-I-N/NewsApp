package com.edwin.domain.repository

import androidx.paging.PagingData
import com.edwin.domain.model.Article
import com.edwin.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun getArticles(query: String, sortOrder: SortOrder): Flow<PagingData<Article>>
}