package com.edwin.data.network

import com.edwin.data.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val BASE_URL = BuildConfig.API_URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val articleDataSource: ArticleApiDataSource by lazy {
        retrofit.create(ArticleApiDataSource::class.java)
    }
}