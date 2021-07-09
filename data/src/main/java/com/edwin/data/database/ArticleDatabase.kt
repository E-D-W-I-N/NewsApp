package com.edwin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edwin.data.entity.ArticleDTO

@Database(entities = [ArticleDTO::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}