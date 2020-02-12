package com.bove.martin.pexel.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * Created by Martín Bove on 07-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class FullFotoActivityViewModel extends ViewModel {
    private MutableLiveData<Boolean> mHavePermission;
    private MutableLiveData<Boolean> mIsLoading;

    public FullFotoActivityViewModel() {
        mHavePermission = new MutableLiveData<Boolean>();
        mIsLoading = new MutableLiveData<Boolean>();
        mHavePermission.setValue(true);
    }
    public LiveData<Boolean> getHavePermission() {
        return mHavePermission;
    }

    public void setHavePermission(Boolean value) {
        mHavePermission.setValue(value);
    }

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void setIsLoading(Boolean value) {
        mIsLoading.setValue(value);
    }



}
