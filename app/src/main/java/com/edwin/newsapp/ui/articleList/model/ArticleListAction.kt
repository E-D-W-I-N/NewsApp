package com.edwin.newsapp.ui.articleList.model

sealed class ArticleListAction {
    data class ShowError(val throwable: Throwable) : ArticleListAction()
}