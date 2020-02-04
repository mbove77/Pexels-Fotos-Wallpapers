package com.bove.martin.pexel.activities;

import android.content.Intent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
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

import com.bove.martin.pexel.R;
import com.bove.martin.pexel.adapter.FotoAdapter;
import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.util.Util;
import com.bove.martin.pexel.viewmodels.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FotoAdapter.OnItemClickListener, Observer<List<Foto>>, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FotoAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    private String queryString;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();

        initRecyclerView();

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

        // When reach the final of the list call for more items.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    getMorePhotos();
                }
            }
        });

        getPhotos(queryString, false);

        // Observe for changes in queryString
        mainActivityViewModel.getQueryString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                queryString = s;
                searchForPhotos();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFotos);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    // Observer for data changes in Fotos list.
    @Override
    public void onChanged(List<Foto> fotos) {
        if (adapter == null) {
            adapter = new FotoAdapter(fotos, R.layout.recycler_view_item, this);
            recyclerView.setAdapter(adapter);
            hideProgressBar();
        } else {
            adapter.notifyDataSetChanged();
            hideProgressBar();
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
        largeFotoIntent.putExtra(Util.PHOTO_URL, foto.getLarge());
        largeFotoIntent.putExtra(Util.LARGE_FOTO_URL, foto.getLarge2x());
        largeFotoIntent.putExtra(Util.PHOTOGRAPHER_NAME, foto.getPhotographer());
        largeFotoIntent.putExtra(Util.PHOTOGRAPHER_URL, foto.getUrl());
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

