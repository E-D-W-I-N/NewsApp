package com.edwin.newsapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.edwin.data.preferences.AppTheme
import com.edwin.data.preferences.PreferencesManager
import com.edwin.newsapp.di.AppModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class NewsApplication : Application() {

    private val preferencesManager: PreferencesManager by inject()

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NewsApplication)
            modules(
                AppModule.dataModule,
                AppModule.useCaseModule,
                AppModule.viewModelModule
            )
        }

        MainScope().launch {
            when (preferencesManager.appTheme.first()) {
                AppTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                AppTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}