package com.edwin.domain.model

data class Article(
    val author: String? = "",
    val title: String? = "",
    val description: String? = "",
    val content: String? = "",
    val publishedAt: String? = "",
    val urlToImage: String? = "",
    val url: String? = ""
)