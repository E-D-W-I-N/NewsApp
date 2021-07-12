package com.edwin.newsapp.ui.articleList

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.edwin.data.preferences.AppTheme
import com.edwin.domain.model.SortOrder
import com.edwin.newsapp.R
import com.edwin.newsapp.databinding.FragmentArticleListBinding
import com.edwin.newsapp.extension.onQueryTextChanged
import com.edwin.newsapp.extension.showDialog
import com.edwin.newsapp.extension.showSnackbar
import com.edwin.newsapp.ui.HostActivity
import com.edwin.newsapp.ui.articleList.model.ArticleListAction
import com.edwin.newsapp.ui.articleList.model.ArticleListEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class ArticleListFragment : Fragment(R.layout.fragment_article_list) {

    private val viewModel: ArticleListViewModel by viewModel()
    private val binding by viewBinding(FragmentArticleListBinding::bind)
    private lateinit var searchView: SearchView
    private lateinit var toggle: ActionBarDrawerToggle

    private val articleListAdapter = ArticleListAdapter { article ->
        //TODO: Navigate to WebView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.articles.flowWithLifecycle(lifecycle).collectLatest {
                articleListAdapter.submitData(it)
            }
        }

        val viewActions = viewModel.viewActions.flowWithLifecycle(lifecycle)
        viewActions.onEach { bindViewAction(it) }.launchIn(lifecycleScope)

        val stateLoader = ArticleLoaderStateAdapter()
        recyclerViewArticles.adapter = articleListAdapter.withLoadStateHeaderAndFooter(
            header = stateLoader,
            footer = stateLoader
        )

        articleListAdapter.addLoadStateListener { state ->
            binding.apply {
                recyclerViewArticles.isVisible = state.refresh != LoadState.Loading
                progressBar.isVisible = state.refresh == LoadState.Loading
            }
        }

        setupNavToggle()
        setHasOptionsMenu(true)
    }

    private fun setupNavToggle() = with(binding) {
        toggle = ActionBarDrawerToggle(activity, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        (activity as HostActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.theme_switch) {
                showThemeDialog()
            }
            true
        }
    }

    private fun bindViewAction(action: ArticleListAction) {
        when (action) {
            is ArticleListAction.ShowError -> showSnackbar(getString(R.string.error))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_article_list, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.query.value
        if (pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }
        searchView.onQueryTextChanged {
            viewModel.obtainEvent(ArticleListEvent.ChangeSearchQuery(it))
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            toggle.onOptionsItemSelected(item) -> true
            item.itemId == R.id.sort_by_publishing_date -> {
                viewModel.obtainEvent(ArticleListEvent.ChangeSortOrder(SortOrder.BY_PUBLISHING_DATE))
                true
            }
            item.itemId == R.id.sort_by_author -> {
                viewModel.obtainEvent(ArticleListEvent.ChangeSortOrder(SortOrder.BY_AUTHOR))
                true
            }
            item.itemId == R.id.sort_by_title -> {
                viewModel.obtainEvent(ArticleListEvent.ChangeSortOrder(SortOrder.BY_TITLE))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showThemeDialog() {
        var checkedTheme = viewModel.appTheme.value.ordinal
        showDialog(
            title = R.string.choose_app_theme,
            positiveCallback = {
                val appTheme = AppTheme.values()[checkedTheme]
                viewModel.obtainEvent(ArticleListEvent.SaveAppTheme(appTheme))
                when (appTheme) {
                    AppTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    AppTheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    AppTheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        ).setSingleChoiceItems(AppTheme.valuesAsString(), checkedTheme) { _, which ->
            checkedTheme = which
        }.show()
    }
}