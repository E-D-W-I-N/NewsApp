package com.edwin.domain.model

enum class SortOrder {
    BY_PUBLISHING_DATE,
    BY_POPULARITY;

    fun toParamName(): String = when (this) {
        BY_PUBLISHING_DATE -> "publishedAt"
        BY_POPULARITY -> "popularity"
    }
}