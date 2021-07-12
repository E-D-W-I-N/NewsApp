package com.edwin.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edwin.data.entity.ArticleDTO
import com.edwin.domain.model.SortOrder

@Dao
interface ArticleDao {

    fun getArticles(query: String, sortOrder: SortOrder): PagingSource<Int, ArticleDTO> =
        when (sortOrder) {
            SortOrder.BY_PUBLISHING_DATE -> getArticlesByPublishingDate("%$query%")
            SortOrder.BY_AUTHOR -> getArticlesByAuthor("%$query%")
            SortOrder.BY_TITLE -> getArticlesByTitle("%$query%")
        }

    @Query("SELECT * FROM articles WHERE title LIKE :query OR content LIKE :query ORDER BY publishedAt")
    fun getArticlesByPublishingDate(query: String): PagingSource<Int, ArticleDTO>

    @Query("SELECT * FROM articles WHERE title LIKE :query OR content LIKE :query ORDER BY author")
    fun getArticlesByAuthor(query: String): PagingSource<Int, ArticleDTO>

    @Query("SELECT * FROM articles WHERE title LIKE :query OR content LIKE :query ORDER BY title")
    fun getArticlesByTitle(query: String): PagingSource<Int, ArticleDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<ArticleDTO>)

    @Query("DELETE FROM articles WHERE `query` LIKE :query")
    suspend fun delete(query: String)

}