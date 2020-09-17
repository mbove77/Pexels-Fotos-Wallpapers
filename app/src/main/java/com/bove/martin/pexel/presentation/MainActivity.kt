package com.bove.martin.pexel.presentation

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bove.martin.pexel.R
import com.bove.martin.pexel.data.model.Foto
import com.bove.martin.pexel.data.model.Search
import com.bove.martin.pexel.presentation.adapters.FotoAdapter
import com.bove.martin.pexel.presentation.adapters.SearchAdapter
import com.bove.martin.pexel.presentation.adapters.SearchAdapter.OnSearchItemClickListener
import com.bove.martin.pexel.utils.AppConstants
import com.bove.martin.pexel.utils.EndlessRecyclerViewScrollListener
import com.bove.martin.pexel.utils.MyRecyclerScroll

//TODO implement voice search
//TODO translate searches
//TODO implement connection monitor
//TODO implementar navigation graph y transiciones
//TODO migrar a Kotlin + MVVM + Koin
class MainActivity : AppCompatActivity(), FotoAdapter.OnItemClickListener, Observer<List<Foto?>?>, OnRefreshListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private var adapter: FotoAdapter? = null
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var recyclerViewSeaches: RecyclerView
    private lateinit var searchesLayoutManager: LinearLayoutManager
    private var searchAdapter: SearchAdapter? = null
    private lateinit var searchLayout: LinearLayout
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.AppTheme)
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        initRecyclerView()
        initRecyclerViewSearch()
        viewFlipper = findViewById(R.id.mainViewFlipper)
        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener(this)

        // load more items whew reach near the end of the list.
        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                morePhotos
            }
        })

        // initial load of the photos
        getPhotos(false)

        // searches observer
        mainActivityViewModel.searchs.observe(this, Observer<List<Search>> { searches: List<Search> ->
            if (searchAdapter != null) {
                recyclerViewSeaches.adapter = searchAdapter
            } else {
                searchAdapter = SearchAdapter(searches, R.layout.recycler_view_search_item, object : OnSearchItemClickListener {
                    override fun onSearchSuggestItemClick(search: Search, posicion: Int) {
                        mainActivityViewModel.setQueryString(search.searchInEnglish)
                        searchView.isIconified = true
                        searchView.onActionViewCollapsed()
                    }
                })
                recyclerViewSeaches.adapter = searchAdapter
            }
        })

        // queryString observer
        mainActivityViewModel.queryString?.observe(this, Observer { s: String? -> searchForPhotos() })

        // hide & show searches recycler based on scroll.
        searchLayout = findViewById(R.id.searchesLayout)
        recyclerView.addOnScrollListener(object : MyRecyclerScroll() {
            override fun show() {
                searchLayout.setVisibility(View.VISIBLE)
            }

            override fun hide() {
                searchLayout.setVisibility(View.GONE)
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager!!.spanCount = 3
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager!!.spanCount = 2
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFotos)
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(layoutManager)
    }

    private fun initRecyclerViewSearch() {
        recyclerViewSeaches = findViewById(R.id.recyclerViewSearchs)
        searchesLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerViewSeaches.setLayoutManager(searchesLayoutManager)
        recyclerViewSeaches.setItemAnimator(DefaultItemAnimator())
    }

    /*
    * Observer for data changes in Fotos list.
    * DisplayChild 0 is the swipeLayout with recyclerView.
    * DisplayChild 1 is no result layout.
    */
    /*
    fun onChanged(fotos: List<Foto>) {
        if (fotos != null && fotos.size > 0) {
            viewFlipper!!.displayedChild = 0
            if (adapter == null) {
                adapter = FotoAdapter(fotos, R.layout.recycler_view_item, this)
                recyclerView!!.adapter = adapter
                hideProgressBar()
            } else {
                if (fotos.size > AppConstants.ITEM_NUMBER) {
                    adapter!!.notifyItemRangeInserted(fotos.size - AppConstants.ITEM_NUMBER, AppConstants.ITEM_NUMBER)
                } else {
                    adapter!!.notifyDataSetChanged()
                    recyclerView!!.scrollToPosition(0)
                }
                hideProgressBar()
            }
        } else {
            viewFlipper!!.displayedChild = 1
        }
    }*/

    // Search Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != null && query !== mainActivityViewModel!!.queryString!!.value) {
                    mainActivityViewModel!!.setQueryString(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView!!.setOnCloseListener {
            if (viewFlipper!!.displayedChild == 1) {
                mainActivityViewModel!!.setQueryString(null)
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
        mainActivityViewModel!!.setQueryString(null)
    }

    private fun showProgressBar() {
        swipeContainer!!.isRefreshing = true
    }

    private fun hideProgressBar() {
        swipeContainer!!.isRefreshing = false
    }

    fun getPhotos(resetList: Boolean?) {
        showProgressBar()
        mainActivityViewModel!!.getFotos(resetList!!).observe(this, this)
    }

    val morePhotos: Unit
        get() {
            mainActivityViewModel!!.addPage()
            getPhotos(false)
        }

    fun searchForPhotos() {
        showProgressBar()
        mainActivityViewModel!!.getFotos(true).observe(this, this)
    }

    override fun onChanged(fotos: List<Foto?>?) {
        if (fotos != null && fotos.size > 0) {
            viewFlipper!!.displayedChild = 0
            if (adapter == null) {
                adapter = FotoAdapter(fotos as List<Foto>, R.layout.recycler_view_item, this)
                recyclerView!!.adapter = adapter
                hideProgressBar()
            } else {
                if (fotos.size > AppConstants.ITEM_NUMBER) {
                    adapter!!.notifyItemRangeInserted(fotos.size - AppConstants.ITEM_NUMBER, AppConstants.ITEM_NUMBER)
                } else {
                    adapter!!.notifyDataSetChanged()
                    recyclerView!!.scrollToPosition(0)
                }
                hideProgressBar()
            }
        } else {
            viewFlipper!!.displayedChild = 1
        }
    }
}