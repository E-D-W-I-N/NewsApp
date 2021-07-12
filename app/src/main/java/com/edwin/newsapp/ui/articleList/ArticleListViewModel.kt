package com.edwin.newsapp.ui.articleList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.edwin.data.preferences.AppTheme
import com.edwin.data.preferences.PreferencesManager
import com.edwin.domain.model.Article
import com.edwin.domain.usecase.UseCase
import com.edwin.domain.usecase.articleList.FetchArticlesUseCase.Params
import com.edwin.newsapp.ui.articleList.model.ArticleListAction
import com.edwin.newsapp.ui.articleList.model.ArticleListEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ArticleListViewModel(
    private val getArticlesUseCase: UseCase<Flow<PagingData<Article>>, Params>,
    private val preferences: PreferencesManager
) : ViewModel() {

    private val _viewActions = Channel<ArticleListAction>()
    val viewActions = _viewActions.receiveAsFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _appTheme = MutableStateFlow(AppTheme.SYSTEM)
    val appTheme: StateFlow<AppTheme> = _appTheme.asStateFlow()

    init {
        viewModelScope.launch {
            val stateFlowValue = preferences.appTheme.stateIn(viewModelScope).value
            _appTheme.tryEmit(stateFlowValue)
        }
    }

    fun obtainEvent(viewEvent: ArticleListEvent) = viewModelScope.launch {
        when (viewEvent) {
            is ArticleListEvent.ChangeSortOrder -> preferences.updateSortOrder(viewEvent.sortOrder)
            is ArticleListEvent.ChangeSearchQuery -> _query.tryEmit(viewEvent.searchQuery)
            is ArticleListEvent.SaveAppTheme -> saveAppTheme(viewEvent.appTheme)
        }
    }

    val articles = combine(query, preferences.sortOrder) { query, sortOrder ->
        Pair(query, sortOrder)
    }.flatMapLatest { (query, sortOrder) ->
        getArticlesUseCase(Params(query, sortOrder))
    }.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty()).cachedIn(viewModelScope)

    private fun saveAppTheme(appTheme: AppTheme) = viewModelScope.launch {
        preferences.updateAppTheme(appTheme)
        _appTheme.tryEmit(appTheme)
    }
}