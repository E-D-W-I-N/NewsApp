package com.edwin.domain.repository

import com.edwin.domain.model.Article
import com.edwin.domain.model.SortOrder

interface ArticleRepository {

    fun getArticles(query: String, sortOrder: SortOrder): List<Article>?
}