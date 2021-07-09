package com.edwin.domain.usecase.articleList

import com.edwin.domain.model.Article
import com.edwin.domain.model.SortOrder
import com.edwin.domain.repository.ArticleRepository
import com.edwin.domain.usecase.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FetchArticlesUseCase(
    private val articleRepository: ArticleRepository
) : UseCase<Flow<Result<List<Article>>>, FetchArticlesUseCase.Params> {

    override suspend fun run(params: Params): Flow<Result<List<Article>>> = flow {
        val result = try {
            Result.success(
                articleRepository.getArticles(params.query, params.sortOrder) ?: emptyList()
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    data class Params(
        val query: String,
        val sortOrder: SortOrder
    )
}