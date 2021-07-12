package com.edwin.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.edwin.data.database.ArticleDao
import com.edwin.data.entity.ArticleDTO
import com.edwin.data.util.Constants.MAX_PAGE_SIZE
import retrofit2.HttpException


@ExperimentalPagingApi
class ArticleRemoteMediator(
    var query: String,
    val articleDao: ArticleDao,
    private val articleService: ArticleService
) : RemoteMediator<Int, ArticleDTO>() {

    //????
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleDTO>
    ): MediatorResult = when (loadType) {
        REFRESH -> refresh(state)
        PREPEND -> MediatorResult.Success(endOfPaginationReached = false)
        APPEND -> append(state)
    }

    private suspend fun refresh(state: PagingState<Int, ArticleDTO>): MediatorResult {
        val response = articleService.getArticles(
            query = query,
            page = INITIAL_PAGE_NUMBER,
            pageSize = pageSize(state)
        )

        return if (response.isSuccessful) {
            deleteCache()
            val articles = response.body()!!.articles
            saveCache(articles)
            MediatorResult.Success(endOfPaginationReached = articles.isEmpty())
        } else {
            MediatorResult.Error(HttpException(response))
        }
    }

    private suspend fun append(state: PagingState<Int, ArticleDTO>): MediatorResult {
        val page = state.pages.minOfOrNull { it.prevKey ?: Int.MAX_VALUE }
            ?: return MediatorResult.Success(endOfPaginationReached = true)

        val response = articleService.getArticles(
            query = query,
            page = page,
            pageSize = pageSize(state)
        )

        return if (response.isSuccessful) {
            val articles = response.body()!!.articles
            saveCache(articles)
            MediatorResult.Success(endOfPaginationReached = articles.isEmpty())
        } else {
            MediatorResult.Error(HttpException(response))
        }
    }

    private fun pageSize(state: PagingState<Int, ArticleDTO>): Int {
        return state.config.pageSize.coerceAtMost(MAX_PAGE_SIZE)
    }

    private suspend fun deleteCache() {
        articleDao.delete("%$query%")
    }

    private suspend fun saveCache(articles: List<ArticleDTO>) {
        articles.map { it.query = query }
        articleDao.insert(articles)
    }

    private companion object {
        private const val INITIAL_PAGE_NUMBER = 1
    }

}