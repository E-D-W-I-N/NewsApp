package com.edwin.data.network

import androidx.annotation.IntRange
import com.edwin.data.BuildConfig
import com.edwin.data.entity.ArticleResponse
import com.edwin.data.util.Constants.DEFAULT_PAGE_SIZE
import com.edwin.data.util.Constants.MAX_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {

    @GET("/v2/everything")
    suspend fun getArticles(
        @Query("q") query: String? = null,
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("pageSize") @IntRange(
            from = 1,
            to = MAX_PAGE_SIZE.toLong()
        ) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query("from") fromDate: String = "2019-04-00",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<ArticleResponse>
}