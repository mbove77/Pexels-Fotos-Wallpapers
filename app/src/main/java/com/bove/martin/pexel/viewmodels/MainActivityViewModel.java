package com.bove.martin.pexel.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.model.Search;
import com.bove.martin.pexel.repositories.FotosRepository;
import com.bove.martin.pexel.repositories.PopularSearchsRepository;

import java.util.List;

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Foto>> mFotos;
    private MutableLiveData<List<Search>> mSearchs;
    private MutableLiveData<String> mQueryString;

    private FotosRepository mRepo;
    private PopularSearchsRepository mPopularSearchRepo;
    private int pageNumber = 1;

    public MainActivityViewModel(){
        mRepo = FotosRepository.getInstance();
        mPopularSearchRepo = PopularSearchsRepository.getInstance();
        mFotos = new MutableLiveData<List<Foto>>();
        mQueryString = new MutableLiveData<String>();
    }

    public void addPage() {
        pageNumber ++;
    }

    private void resetList() { pageNumber = 1; }

    public LiveData<List<Foto>> getFotos(Boolean resetList) {
        if(resetList) { resetList(); }
        return mFotos = mRepo.getFotos(mQueryString.getValue(), pageNumber, resetList);
    }

    public LiveData<List<Search>> getSearchs() {
        return mSearchs = mPopularSearchRepo.getSearchs();
    }

    public void setQueryString(String query) {
        mQueryString.postValue(query);
    }

    public LiveData<String> getQueryString() {
        return mQueryString;
    }

}
