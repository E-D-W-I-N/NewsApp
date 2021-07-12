package com.edwin.newsapp.ui.articleList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.edwin.data.preferences.AppTheme
import com.edwin.data.preferences.PreferencesManager
import com.edwin.domain.model.Article
import com.edwin.domain.usecase.UseCase
import com.edwin.domain.usecase.articleList.FetchArticlesUseCase
import com.edwin.newsapp.ui.articleList.model.ArticleListAction
import com.edwin.newsapp.ui.articleList.model.ArticleListEvent
import com.edwin.newsapp.ui.articleList.model.ArticleListViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ArticleListViewModel(
    private val getArticlesUseCase: UseCase<Flow<PagingData<Article>>, FetchArticlesUseCase.Params>,
    private val preferences: PreferencesManager
) : ViewModel() {

    private val _viewStates = MutableStateFlow(ArticleListViewState())
    val viewStates: StateFlow<ArticleListViewState> = _viewStates.asStateFlow()

    private val _viewActions = Channel<ArticleListAction>()
    val viewActions = _viewActions.receiveAsFlow()

    val searchQuery = MutableStateFlow("")
    lateinit var appTheme: MutableStateFlow<AppTheme>

    init {
        viewModelScope.launch {
            val stateFlowValue = preferences.appTheme.stateIn(viewModelScope).value
            appTheme = MutableStateFlow(stateFlowValue)
        }
        getArticles()
    }

    fun obtainEvent(viewEvent: ArticleListEvent) = viewModelScope.launch {
        when (viewEvent) {
            is ArticleListEvent.ChangeSortOrder -> preferences.updateSortOrder(viewEvent.sortOrder)
            is ArticleListEvent.ChangeSearchQuery -> searchQuery.value = viewEvent.searchQuery
            is ArticleListEvent.SaveAppTheme -> saveAppTheme(viewEvent.appTheme)
        }
    }

    private fun getArticles() = viewModelScope.launch {
        combine(searchQuery, preferences.sortOrder) { query, sortOrder ->
            Pair(query, sortOrder)
        }.flatMapLatest { (query, sortOrder) ->
            _viewStates.value = _viewStates.value.copy(isLoading = true)
            getArticlesUseCase.run(FetchArticlesUseCase.Params(query, sortOrder))
        }.collect {
            _viewStates.value = _viewStates.value.copy(isLoading = false, pagingData = it)
        }
    }

    private fun saveAppTheme(appTheme: AppTheme) = viewModelScope.launch {
        preferences.updateAppTheme(appTheme)
        this@ArticleListViewModel.appTheme.value = appTheme
    }
}