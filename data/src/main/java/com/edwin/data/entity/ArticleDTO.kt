package com.edwin.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String? = "",
    val description: String? = "",
    val publishedAt: String? = "",
    val urlToImage: String? = "",
    val url: String? = ""
)

data class ArticleResponse(
    val articles: List<ArticleDTO>
)