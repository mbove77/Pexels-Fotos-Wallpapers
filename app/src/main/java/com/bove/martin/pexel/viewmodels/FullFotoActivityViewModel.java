package com.bove.martin.pexel.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * Created by Martín Bove on 07-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class FullFotoActivityViewModel extends ViewModel {
    private MutableLiveData<Boolean> mHaveStorgePermission;

    public FullFotoActivityViewModel() {
        mHaveStorgePermission = new MutableLiveData<Boolean>();
    }

    public LiveData<Boolean> getStoragePermission() {
        return mHaveStorgePermission;
    }

    public void setSoragePermission(Boolean value) {
        mHaveStorgePermission.setValue(value);
    }

}
