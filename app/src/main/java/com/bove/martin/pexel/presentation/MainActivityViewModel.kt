package com.bove.martin.pexel.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bove.martin.pexel.data.model.Foto
import com.bove.martin.pexel.data.model.Search
import com.bove.martin.pexel.data.repositories.FotosRepository
import com.bove.martin.pexel.data.repositories.PopularSearchsRepository

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */
class MainActivityViewModel : ViewModel() {
    private var mFotos: MutableLiveData<List<Foto>>
    private var mSearchs: MutableLiveData<List<Search>>? = null
    private val mQueryString: MutableLiveData<String>?
    private val mRepo: FotosRepository = FotosRepository.getInstance()
    private val mPopularSearchRepo: PopularSearchsRepository
    private var pageNumber = 1

    fun addPage() {
        pageNumber++
    }

    private fun resetList() {
        pageNumber = 1
    }

    fun getFotos(resetList: Boolean): LiveData<List<Foto>> {
        if (resetList) {
            resetList()
        }
        return mRepo.getFotos(mQueryString?.value, pageNumber, resetList).also { mFotos = it }
    }

    val searchs: LiveData<List<Search>>
        get() = mPopularSearchRepo.searchs.also { mSearchs = it }

    fun setQueryString(query: String?) {
        mQueryString?.postValue(query)
    }

    val queryString: LiveData<String>?
        get() = mQueryString

    init {
        mPopularSearchRepo = PopularSearchsRepository.getInstance()
        mFotos = MutableLiveData()
        mQueryString = MutableLiveData()
    }
}