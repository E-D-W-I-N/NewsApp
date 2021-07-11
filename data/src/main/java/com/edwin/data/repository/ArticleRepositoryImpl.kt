package com.edwin.data.repository

import com.edwin.data.database.ArticleDao
import com.edwin.data.entity.mapper.toDomainList
import com.edwin.data.network.ArticleApiDataSource
import com.edwin.domain.model.Article
import com.edwin.domain.model.SortOrder
import com.edwin.domain.repository.ArticleRepository
import java.net.UnknownHostException

class ArticleRepositoryImpl(
    val articleDatabase: ArticleDao,
    private val articleApi: ArticleApiDataSource,
) : ArticleRepository {

    override fun getArticles(query: String, sortOrder: SortOrder): List<Article>? = try {
        val articleResponse = articleApi.getArticles(
            query = if (query.isBlank()) "android" else query,
            sort = sortOrder.toParamName()
        ).execute().body()
        articleResponse?.articles?.toDomainList()
    } catch (e: UnknownHostException) {
        //TODO: Make normal exception blyat
        throw UnknownHostException()
    }
}