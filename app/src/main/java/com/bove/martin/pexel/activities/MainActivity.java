package com.bove.martin.pexel.activities;

import android.content.Intent;

import androidx.annotation.NonNull;
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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.bove.martin.pexel.R;
import com.bove.martin.pexel.adapter.FotoAdapter;
import com.bove.martin.pexel.adapter.SearchAdapter;
import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.utils.AppConstants;
import com.bove.martin.pexel.utils.EndlessRecyclerViewScrollListener;
import com.bove.martin.pexel.utils.MyRecyclerScroll;
import com.bove.martin.pexel.viewmodels.MainActivityViewModel;

import java.util.List;

//TODO implement voice search
//TODO implement connection monitor
//TODO ver que hacer cuando esta escrita una busqueda y se apreta en una categoria.

public class MainActivity extends AppCompatActivity implements FotoAdapter.OnItemClickListener, Observer<List<Foto>>, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layoutManager;
    private FotoAdapter adapter;
    private ViewFlipper viewFlipper;
    private SwipeRefreshLayout swipeContainer;

    private MainActivityViewModel mainActivityViewModel;

    private RecyclerView recyclerViewSeaches;
    private LinearLayoutManager searchesLayoutManager;
    private SearchAdapter searchAdapter;
    private LinearLayout searchLayout;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        initRecyclerView();
        initRecyclerViewSearch();

        viewFlipper = findViewById(R.id.mainViewFlipper);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        // load more items whew reach near the end of the list.
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getMorePhotos();
            }
        });

        // initial load of the photos
        getPhotos(false);

        // searches observer
        mainActivityViewModel.getSearchs().observe(this, searches -> {
            if(searchAdapter != null) {
                recyclerViewSeaches.setAdapter(searchAdapter);
            } else {
                searchAdapter = new SearchAdapter(searches, R.layout.recycler_view_search_item, (search, posicion) -> {
                   mainActivityViewModel.setQueryString(search.getSearchInEnglish());
                });
                recyclerViewSeaches.setAdapter(searchAdapter);
            }
        });

        // queryString observer
        mainActivityViewModel.getQueryString().observe(this, s -> {
            searchForPhotos();
        });

        // hide & show searches recycler based on scroll.
        searchLayout = findViewById(R.id.searchesLayout);
        recyclerView.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                searchLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void hide() {
                searchLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setSpanCount(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setSpanCount(2);
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFotos);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
                if(fotos.size() > AppConstants.ITEM_NUMBER) {
                    adapter.notifyItemRangeInserted(fotos.size() - AppConstants.ITEM_NUMBER, AppConstants.ITEM_NUMBER);
                } else {
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(0);
                }
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
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null && query != mainActivityViewModel.getQueryString().getValue()) {
                    mainActivityViewModel.setQueryString(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        mainActivityViewModel.setQueryString(null);
    }

    private void showProgressBar() {
        swipeContainer.setRefreshing(true);
    }

    private void hideProgressBar() {
        swipeContainer.setRefreshing(false);
    }

    public void getPhotos(Boolean resetList) {
        showProgressBar();
        mainActivityViewModel.getFotos(resetList).observe(this, this);
    }

    public void getMorePhotos() {
       mainActivityViewModel.addPage();
       getPhotos(false);
    }

    public void searchForPhotos() {
        showProgressBar();
        mainActivityViewModel.getFotos(true).observe(this, this);
    }
}

