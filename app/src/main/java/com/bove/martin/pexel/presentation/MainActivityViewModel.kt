package com.bove.martin.pexel.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.data.model.Foto
import com.bove.martin.pexel.data.model.Search
import com.bove.martin.pexel.data.repositories.FotosRepository
import com.bove.martin.pexel.data.repositories.PopularSearchesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */
class MainActivityViewModel : ViewModel() {
    private val _fotos = MutableLiveData<List<Foto>>()
    val fotos: LiveData<List<Foto>> get() = _fotos
    
    private val _queryString = MutableLiveData<String>()
    val queryString: LiveData<String> get() = _queryString
    
    private val _searches = MutableLiveData<List<Search>>()
    val searches: LiveData<List<Search>> get() = _searches

    private val mRepo = FotosRepository()
    private val mSearchRepo = PopularSearchesRepository()
    private var pageNumber = 1

    // todo unir esta funcion con getFotos
    fun getMoreFotos(resetList: Boolean) {
        pageNumber++
        getFotos(resetList)
    }

    fun getFotos(resetList: Boolean) {
        if (resetList) {pageNumber = 1}
        viewModelScope.launch(Dispatchers.IO) {
            val response = mRepo.getCuratedFotos(_queryString.value, pageNumber, resetList)

            if (response.operationResult) {
                withContext(Dispatchers.Main) {
                    _fotos.value = response.resultObject as List<Foto>
                }
            }
        }
    }

    fun getSearchOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = mSearchRepo.searches

            if (!response.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    _searches.value = response;
                }
            }
        }
    }

    fun setQueryString(value:String?) {
        _queryString.value = value
    }
}