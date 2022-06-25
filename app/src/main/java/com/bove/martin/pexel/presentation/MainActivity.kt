package com.bove.martin.pexel.presentation

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bove.martin.pexel.AppConstants
import com.bove.martin.pexel.R
import com.bove.martin.pexel.databinding.ActivityMainBinding
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.Search
import com.bove.martin.pexel.presentation.adapters.FotoAdapter
import com.bove.martin.pexel.presentation.adapters.SearchAdapter
import com.bove.martin.pexel.presentation.adapters.SearchAdapter.OnSearchItemClickListener
import com.bove.martin.pexel.presentation.utils.EndlessRecyclerViewScrollListener
import com.bove.martin.pexel.presentation.utils.MyRecyclerScroll
import com.bove.martin.pexel.presentation.utils.NpaStaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

//TODO implement voice search
//TODO translate searches
//TODO implement connection monitor
//TODO implement favorite view
//TODO add visual cue when search option is selected

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FotoAdapter.OnFotoClickListener,  OnRefreshListener {
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.AppTheme)

        initUi()
        initObservables()
        initScrollListeners()
        getPhotos()
    }

    private fun initScrollListeners() {
        // Load more items whew reach near the end of the list.
        with(binding) {
            recyclerViewFotos.addOnScrollListener(object : EndlessRecyclerViewScrollListener(
                recyclerViewFotos.layoutManager as StaggeredGridLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.getFotos()
                }
            })
        }

        // Hide or show searches recycler based on scroll.
        binding.recyclerViewFotos.addOnScrollListener(object : MyRecyclerScroll() {
            override fun show() {
                binding.searchesLayout.visibility = View.VISIBLE
            }

            override fun hide() {
                binding.searchesLayout.visibility = View.GONE
            }
        })
    }

    private fun initObservables() {
        viewModel.searches.observe(this) {
            if (binding.recyclerViewSearchs.adapter == null) {
                val searchAdapter = SearchAdapter(it, R.layout.recycler_view_search_item,
                    object : OnSearchItemClickListener {
                        override fun onSearchSuggestItemClick(search: Search, posicion: Int) {
                            viewModel.setQueryString(search.searchInEnglish)
                            searchView.isIconified = true
                            searchView.onActionViewCollapsed()
                            searchForPhotos()
                        }
                    })
                binding.recyclerViewSearchs.adapter = searchAdapter
            }
        }

        viewModel.fotos.observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.mainViewFlipper.displayedChild = 0
                if (binding.recyclerViewFotos.adapter == null) {
                    binding.recyclerViewFotos.adapter = FotoAdapter(it, R.layout.recycler_view_item, this)
                    showHideProgressBar(false)
                } else {
                    if (it.size > AppConstants.ITEM_NUMBER) {
                        binding.recyclerViewFotos.adapter!!.notifyItemRangeInserted(
                            it.size - AppConstants.ITEM_NUMBER,
                            AppConstants.ITEM_NUMBER
                        )
                    } else {
                        binding.recyclerViewFotos.adapter!!.notifyDataSetChanged()
                        binding.recyclerViewFotos.scrollToPosition(0)
                    }
                    showHideProgressBar(false)
                }
            } else {
                binding.mainViewFlipper.displayedChild = 1
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val layoutManager = binding.recyclerViewFotos.layoutManager as NpaStaggeredGridLayoutManager
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.spanCount = 3
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.spanCount = 2
        }
    }

    private fun initUi() {
        val layoutManager = NpaStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewFotos.layoutManager = layoutManager
        binding.swipeContainer.setOnRefreshListener(this)

        val searchesLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerViewSearchs.layoutManager = searchesLayoutManager
        binding.recyclerViewSearchs.itemAnimator = DefaultItemAnimator()
        viewModel.getSearchOptions()
    }

    // Search Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query !== viewModel.getQueryString()) {
                    viewModel.setQueryString(query)
                    searchForPhotos()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener {
            if (binding.mainViewFlipper.displayedChild == 1) {
                viewModel.clearQueryString()
                getPhotos()
            }
            false
        }
        return true
    }

    override fun onFotoClick(foto: Foto, position: Int) {
        val largeFotoIntent = Intent(this, FullFotoActivity::class.java)
        largeFotoIntent.putExtra(AppConstants.PHOTO_URL, foto.large)
        largeFotoIntent.putExtra(AppConstants.LARGE_FOTO_URL, foto.large2x)
        largeFotoIntent.putExtra(AppConstants.PHOTOGRAPHER_NAME, foto.photographer)
        largeFotoIntent.putExtra(AppConstants.PHOTOGRAPHER_URL, foto.url)
        startActivity(largeFotoIntent)
    }

    override fun onRefresh() {
        viewModel.clearQueryString()
        viewModel.getFotos()
    }

    private fun showHideProgressBar(isVisible: Boolean) {
        binding.swipeContainer.isRefreshing = isVisible
    }

    private fun getPhotos() {
        showHideProgressBar(true)
        viewModel.getFotos()
    }

    private fun searchForPhotos() {
        showHideProgressBar(true)
        viewModel.getFotos()
    }
}