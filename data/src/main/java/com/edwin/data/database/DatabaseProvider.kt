package com.edwin.data.database

import android.app.Application
import androidx.room.Room

class DatabaseProvider(application: Application) {

    val articleDao: ArticleDao by lazy {
        Room.databaseBuilder(application, ArticleDatabase::class.java, "ArticleDatabase")
            .build().articleDao()
    }
}