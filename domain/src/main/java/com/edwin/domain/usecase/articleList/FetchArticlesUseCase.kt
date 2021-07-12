package com.edwin.domain.usecase.articleList

import androidx.paging.PagingData
import com.edwin.domain.model.Article
import com.edwin.domain.model.SortOrder
import com.edwin.domain.repository.ArticleRepository
import com.edwin.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

class FetchArticlesUseCase(
    private val articleRepository: ArticleRepository
) : UseCase<Flow<PagingData<Article>>, FetchArticlesUseCase.Params> {

    override operator fun invoke(params: Params): Flow<PagingData<Article>> =
        articleRepository.getArticles(
            query = params.query,
            sortOrder = params.sortOrder
        )

    data class Params(
        val query: String,
        val sortOrder: SortOrder
    )
}