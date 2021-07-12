package com.edwin.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.edwin.data.database.ArticleDao
import com.edwin.data.entity.mapper.toDomain
import com.edwin.data.network.ArticleRemoteMediator
import com.edwin.data.util.Constants.DEFAULT_PAGE_SIZE
import com.edwin.domain.model.SortOrder
import com.edwin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.map

@ExperimentalPagingApi
class ArticleRepositoryImpl(
    private val articleDatabase: ArticleDao,
    private val mediator: ArticleRemoteMediator
) : ArticleRepository {

    override fun getArticles(query: String, sortOrder: SortOrder) = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            prefetchDistance = 5
        ),
        remoteMediator = mediator
    ) {
        articleDatabase.getArticles(query, sortOrder)
    }.flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }
}