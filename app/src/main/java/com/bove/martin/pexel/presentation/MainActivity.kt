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
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.Search
import com.bove.martin.pexel.databinding.ActivityMainBinding
import com.bove.martin.pexel.presentation.adapters.FotoAdapter
import com.bove.martin.pexel.presentation.adapters.SearchAdapter
import com.bove.martin.pexel.presentation.adapters.SearchAdapter.OnSearchItemClickListener
import com.bove.martin.pexel.utils.AppConstants
import com.bove.martin.pexel.utils.EndlessRecyclerViewScrollListener
import com.bove.martin.pexel.utils.MyRecyclerScroll
import dagger.hilt.android.AndroidEntryPoint

//TODO implement voice search
//TODO translate searches
//TODO implement connection monitor
//TODO implementar navigation graph y transiciones
//TODO cambiar los adapters para que acepeten la conelleccion despues de creados.
//TODO para hacer el scroll horizontal una vez seleccionada una foto, debemos pasar todos las pantallas a fragments asi usamos siempre la misma conexión de datos.

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FotoAdapter.OnItemClickListener,  OnRefreshListener {
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding

    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var searchesLayoutManager: LinearLayoutManager
    private var fotoAdapter: FotoAdapter? = null
    private var searchAdapter: SearchAdapter? = null

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.AppTheme)
        initRecyclerView()
        initRecyclerViewSearch()
        binding.swipeContainer.setOnRefreshListener(this)

        // load more items whew reach near the end of the list.
        binding.recyclerViewFotos.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.getMoreFotos(false)
            }
        })

        // initial load of the photos and searches
        getPhotos(false)
        viewModel.getSearchOptions()

        // searches observer
        viewModel.searches.observe(this) {
            if (searchAdapter != null) {
                binding.recyclerViewSearchs.adapter = searchAdapter
            } else {
                searchAdapter = SearchAdapter(it, R.layout.recycler_view_search_item,
                    object : OnSearchItemClickListener {
                        override fun onSearchSuggestItemClick(search: Search, posicion: Int) {
                            viewModel.setQueryString(search.searchInEnglish)
                            searchView.isIconified = true
                            searchView.onActionViewCollapsed()
                        }
                    })
                binding.recyclerViewSearchs.adapter = searchAdapter
            }
        }


        // photos observer
        viewModel.fotos.observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.mainViewFlipper.displayedChild = 0
                if (fotoAdapter == null) {
                    fotoAdapter = FotoAdapter(it, R.layout.recycler_view_item, this)
                    binding.recyclerViewFotos.adapter = fotoAdapter
                    hideProgressBar()
                } else {
                    if (it.size > AppConstants.ITEM_NUMBER) {
                        fotoAdapter!!.notifyItemRangeInserted(
                            it.size - AppConstants.ITEM_NUMBER,
                            AppConstants.ITEM_NUMBER
                        )
                    } else {
                        fotoAdapter!!.notifyDataSetChanged()
                        binding.recyclerViewFotos.scrollToPosition(0)
                    }
                    hideProgressBar()
                }
            } else {
                binding.mainViewFlipper.displayedChild = 1
            }
        }

        // queryString observer
        viewModel.queryString.observe(this) { searchForPhotos() }

        // hide & show searches recycler based on scroll.
        binding.recyclerViewFotos.addOnScrollListener(object : MyRecyclerScroll() {
            override fun show() {
                binding.searchesLayout.visibility = View.VISIBLE
            }

            override fun hide() {
                binding.searchesLayout.visibility = View.GONE
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.spanCount = 3
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.spanCount = 2
        }
    }

    private fun initRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewFotos.layoutManager = layoutManager
    }

    private fun initRecyclerViewSearch() {
        searchesLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerViewSearchs.layoutManager = searchesLayoutManager
        binding.recyclerViewSearchs.itemAnimator = DefaultItemAnimator()
    }

    // Search Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query !== viewModel.queryString.value) {
                    viewModel.setQueryString(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener {
            if (binding.mainViewFlipper.displayedChild == 1) {
                viewModel.setQueryString(null)
            }
            false
        }
        return true
    }

    override fun onItemClick(foto: Foto?, posicion: Int) {
        val largeFotoIntent = Intent(this, FullFotoActivity::class.java)
        largeFotoIntent.putExtra(AppConstants.PHOTO_URL, foto!!.large)
        largeFotoIntent.putExtra(AppConstants.LARGE_FOTO_URL, foto.large2x)
        largeFotoIntent.putExtra(AppConstants.PHOTOGRAPHER_NAME, foto.photographer)
        largeFotoIntent.putExtra(AppConstants.PHOTOGRAPHER_URL, foto.url)
        startActivity(largeFotoIntent)
    }

    // onRefresh SwipeContainer
    override fun onRefresh() {
        viewModel.setQueryString(null)
    }

    private fun showProgressBar() {
        binding.swipeContainer.isRefreshing = true
    }

    private fun hideProgressBar() {
        binding.swipeContainer.isRefreshing = false
    }

    private fun getPhotos(resetList: Boolean) {
        showProgressBar()
        viewModel.getFotos(resetList)
    }

    private fun searchForPhotos() {
        showProgressBar()
        viewModel.getFotos(true)
    }
}