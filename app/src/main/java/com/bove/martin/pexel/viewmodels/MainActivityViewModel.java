package com.bove.martin.pexel.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.repositories.FotosRepository;

import java.util.List;

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Foto>> mFotos;
    private MutableLiveData<String> mQueryString;

    private FotosRepository mRepo;
    private int pageNumber = 1;

    public void init() {
        if(mFotos != null) {
            return;
        }
        mRepo = FotosRepository.getInstance();
    }

    public void addPage() {
        pageNumber ++;
    }

    public void resetPage() {
        pageNumber = 1;
    }

    public LiveData<List<Foto>> getFotos(String query, Boolean resetList) {
        if(resetList) {
          resetPage();
        }
        if(query == null) {
            mFotos = mRepo.getFotos(null, pageNumber, resetList);
        } else {
            mFotos = mRepo.getFotos(query, pageNumber, resetList);
        }
        return mFotos;
    }

    public LiveData<String> getQueryString() {
        if(mQueryString == null ) {
            mQueryString = new MutableLiveData<String>();
        }
        return mQueryString;
    }

    public void setQueryString(String query) {
        mQueryString.setValue(query);
    }



}
