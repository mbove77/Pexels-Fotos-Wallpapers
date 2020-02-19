package com.bove.martin.pexel.activities;

import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bove.martin.pexel.R;
import com.bove.martin.pexel.adapter.FotoAdapter;
import com.bove.martin.pexel.adapter.SearchAdapter;
import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.model.Search;
import com.bove.martin.pexel.utils.AppConstants;
import com.bove.martin.pexel.utils.EndlessRecyclerViewScrollListener;
import com.bove.martin.pexel.utils.MyRecyclerScroll;
import com.bove.martin.pexel.viewmodels.MainActivityViewModel;

import java.util.List;

//TODO smooth the load of new items
//TODO when search get the scroll to top
//TODO post new screen shots to google play
//TODO smooth animation on load new items
//TODO coordinator layout for hid show search items
//TODO horizontal layout
//TODO implement voice search

public class MainActivity extends AppCompatActivity implements FotoAdapter.OnItemClickListener, Observer<List<Foto>>, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layoutManager;
    private FotoAdapter adapter;
    private ViewFlipper viewFlipper;
    private SwipeRefreshLayout swipeContainer;

    private String queryString;
    private MainActivityViewModel mainActivityViewModel;

    private RecyclerView recyclerViewSeaches;
    private LinearLayoutManager searchesLayoutManager;
    private SearchAdapter searchAdapter;
    private LinearLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();

        initRecyclerView();
        initRecyclerViewSearch();

        viewFlipper = findViewById(R.id.mainViewFlipper);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

       /* // When reach the final of the list call for more items.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    getMorePhotos();
                }
            }
        });*/

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getMorePhotos();
            }
        });


        getPhotos(queryString, false);

        // Searches Observer
        mainActivityViewModel.getSearchs().observe(this, new Observer<List<Search>>() {
            @Override
            public void onChanged(List<Search> searches) {
                if(searchAdapter != null) {
                    recyclerViewSeaches.setAdapter(searchAdapter);
                } else {
                    searchAdapter = new SearchAdapter(searches, R.layout.recycler_view_search_item, new SearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Search search, int posicion) {
                            //Toast.makeText(MainActivity.this, "Click " + search.getSearchInSpanish(), Toast.LENGTH_SHORT).show();
                            queryString = search.getSearchInEnglish();
                            searchForPhotos();
                        }
                    });
                    recyclerViewSeaches.setAdapter(searchAdapter);
                }

            }
        });


       /* // Hide-show searches based on scroll
        searchLayout = findViewById(R.id.searchesLayout);
        recyclerView.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                searchLayout.setVisibility(View.VISIBLE);
                searchLayout.animate().translationY(0);
            }

            @Override
            public void hide() {
                searchLayout.animate().translationY(-searchLayout.getHeight()).withEndAction(() -> searchLayout.setVisibility(View.GONE));
            }
        });*/


        // Observe for changes in queryString
        mainActivityViewModel.getQueryString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                queryString = s;
                searchForPhotos();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Daiya", "ORIENTATION_LANDSCAPE");
            layoutManager.setSpanCount(3);
           // searchesLayoutManager.setOrientation(RecyclerView.VERTICAL);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setSpanCount(2);
            //searchesLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            Log.d("Daiya", "ORIENTATION_PORTRAIT");
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFotos);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//      gridLayoutManager = new GridLayoutManager(this, 2);
//      recyclerView.setLayoutManager(gridLayoutManager);

//        int resId = R.anim.grid_layout_animation_from_bottom;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
//        recyclerView.setLayoutAnimation(animation);

//      recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initRecyclerViewSearch() {
        recyclerViewSeaches = findViewById(R.id.recyclerViewSearchs);
        searchesLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerViewSeaches.setLayoutManager(searchesLayoutManager);
        recyclerViewSeaches.setItemAnimator(new DefaultItemAnimator());
    }

    /*
    * Observer for data changes in Fotos list.
    * DisplayChild 0 is the swipeLayout with recyclerView.
    * DisplayChild 1 is no result layout.
    */
    @Override
    public void onChanged(List<Foto> fotos) {
        if(fotos.size() > 0) {
            viewFlipper.setDisplayedChild(0);
            if (adapter == null) {
                adapter = new FotoAdapter(fotos, R.layout.recycler_view_item, this);
                recyclerView.setAdapter(adapter);
                hideProgressBar();
            } else {
                adapter.notifyDataSetChanged();
                hideProgressBar();
            }
        } else {
            viewFlipper.setDisplayedChild(1);
        }
    }

    // Search Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainActivityViewModel.setQueryString(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mainActivityViewModel.setQueryString(null);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onItemClick(Foto foto, int posicion) {
        Intent largeFotoIntent = new Intent(this, FullFotoActivity.class);
        largeFotoIntent.putExtra(AppConstants.PHOTO_URL, foto.getLarge());
        largeFotoIntent.putExtra(AppConstants.LARGE_FOTO_URL, foto.getLarge2x());
        largeFotoIntent.putExtra(AppConstants.PHOTOGRAPHER_NAME, foto.getPhotographer());
        largeFotoIntent.putExtra(AppConstants.PHOTOGRAPHER_URL, foto.getUrl());
        startActivity(largeFotoIntent);
    }

    // onRefresh SwipeContainer
    @Override
    public void onRefresh() {
        mainActivityViewModel.resetPage();

        getPhotos(queryString, true);
    }

    private void showProgressBar() {
        swipeContainer.setRefreshing(true);
    }

    private void hideProgressBar() {
        swipeContainer.setRefreshing(false);
    }

    public void getPhotos(String query, Boolean resetList) {
        showProgressBar();
        mainActivityViewModel.getFotos(query, resetList).observe(this, this);
    }

    public void getMorePhotos() {
       mainActivityViewModel.addPage();
       getPhotos(queryString, false);
    }

    public void searchForPhotos() {
        showProgressBar();
        mainActivityViewModel.getFotos(queryString, true).observe(this, this);
    }
}

