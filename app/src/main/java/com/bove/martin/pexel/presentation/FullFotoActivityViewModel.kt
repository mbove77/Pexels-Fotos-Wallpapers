package com.bove.martin.pexel.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Martín Bove on 07-Feb-20.
 * E-mail: mbove77@gmail.com
 */
class FullFotoActivityViewModel : ViewModel() {
    private val mHaveStorgePermission: MutableLiveData<Boolean>
    val storagePermission: LiveData<Boolean>
        get() = mHaveStorgePermission

    fun setSoragePermission(value: Boolean) {
        mHaveStorgePermission.value = value
    }

    init {
        mHaveStorgePermission = MutableLiveData()
    }
}