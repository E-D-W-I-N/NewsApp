package com.edwin.newsapp.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.edwin.data.database.DatabaseProvider
import com.edwin.data.network.ArticleRemoteMediator
import com.edwin.data.network.RetrofitProvider
import com.edwin.data.preferences.PreferencesManager
import com.edwin.data.repository.ArticleRepositoryImpl
import com.edwin.domain.model.Article
import com.edwin.domain.repository.ArticleRepository
import com.edwin.domain.usecase.UseCase
import com.edwin.domain.usecase.articleList.FetchArticlesUseCase
import com.edwin.newsapp.ui.articleList.ArticleListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {

    @ExperimentalPagingApi
    val dataModule = module {

        factory {
            ArticleRemoteMediator(
                query = "",
                articleDao = DatabaseProvider(androidApplication()).articleDao,
                RetrofitProvider.articleDataSource
            )
        }

        // ArticleRepository
        single<ArticleRepository> {
            ArticleRepositoryImpl(
                DatabaseProvider(androidApplication()).articleDao,
                get()
            )
        }

        // PreferencesManager
        single { PreferencesManager(androidContext()) }
    }

    val useCaseModule = module {
        single<UseCase<Flow<PagingData<Article>>, FetchArticlesUseCase.Params>> {
            FetchArticlesUseCase(get())
        }
    }

    @ExperimentalCoroutinesApi
    val viewModelModule = module {
        viewModel { ArticleListViewModel(get(), get()) }
    }
}