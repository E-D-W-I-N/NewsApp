package com.edwin.data.network

import com.edwin.data.BuildConfig
import com.edwin.data.entity.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiDataSource {

    @GET("everything")
    fun getArticles(
        @Query("q") query: String = "android",
        @Query("from") fromDate: String = "2019-04-00",
        @Query("sortBy") sort: String,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("page") page: String = "1",
    ): Call<ArticleResponse>
}