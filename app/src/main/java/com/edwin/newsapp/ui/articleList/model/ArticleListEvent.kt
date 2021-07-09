package com.edwin.newsapp.ui.articleList.model

import com.edwin.data.preferences.AppTheme
import com.edwin.domain.model.SortOrder

sealed class ArticleListEvent {
    data class ChangeSortOrder(val sortOrder: SortOrder) : ArticleListEvent()
    data class ChangeSearchQuery(val searchQuery: String) : ArticleListEvent()
    data class SaveAppTheme(val appTheme: AppTheme) : ArticleListEvent()
}