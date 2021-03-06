package com.edwin.data.entity.mapper

import com.edwin.data.entity.ArticleDTO
import com.edwin.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Article.toDomain() = ArticleDTO(
    id = 0,
    title = title,
    description = description,
    publishedAt = publishedAt,
    urlToImage = urlToImage,
    url = url
)

fun ArticleDTO.toDomain(): Article = Article(
    title = title,
    description = description,
    publishedAt = publishedAt,
    urlToImage = urlToImage,
    url = url
)

fun List<ArticleDTO>.toDomainList(): List<Article> = map { it.toDomain() }

fun Flow<List<ArticleDTO>>.toDomainFlowList(): Flow<List<Article>> = map { it.toDomainList() }