package com.bove.martin.pexel.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.di.DispacherModule.MainDispatcher
import com.bove.martin.pexel.domain.GetFotosUseCase
import com.bove.martin.pexel.domain.GetPupularSearchesUseCase
import com.bove.martin.pexel.domain.GetSearchedFotosUseCase
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getFotosUseCase: GetFotosUseCase,
    private val getPopularSearchesUseCase: GetPupularSearchesUseCase,
    private val getSearchedFotosUseCase: GetSearchedFotosUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
    ) : ViewModel() {

    val fotos = MutableLiveData<List<Foto>>()
    private val _fotos: MutableList<Foto> = arrayListOf()

    val searches = MutableLiveData<List<Search>>()

    private var queryString:String? = null
    private var pageNumber = 0


    fun getFotos() {
        pageNumber++
        if(queryString.isNullOrEmpty())
            getCuratedFotos()
        else
            getSearchedFotos()
    }

    private fun getCuratedFotos() {
        viewModelScope.launch {
            val response = getFotosUseCase(pageNumber)
            if (response.operationResult) {
                withContext(mainDispatcher) {
                    val listOfFotos = (response.resultObject as List<*>).filterIsInstance<Foto>()
                    _fotos.addAll(listOfFotos)
                    fotos.postValue(_fotos)
                }
            } else {
                fotos.postValue(null)
            }
        }
    }

    private fun getSearchedFotos() {
        viewModelScope.launch {
            val response = getSearchedFotosUseCase(queryString, pageNumber)
            if (response.operationResult) {
                withContext(mainDispatcher) {
                    val listOfFotos = (response.resultObject as List<*>).filterIsInstance<Foto>()
                    _fotos.addAll(listOfFotos)
                    fotos.postValue(_fotos)
                }
            } else {
                fotos.postValue(null)
            }
        }
    }

    fun getSearchOptions() {
        viewModelScope.launch {
            val response = getPopularSearchesUseCase()

            if (response.isNotEmpty()) {
                withContext(mainDispatcher) {
                    searches.postValue(response)
                }
            }
        }
    }

    fun setQueryString(search: String?) {
       if(!search.isNullOrEmpty()) {
           queryString = search
           resetFotoList()
       }
    }

    fun getQueryString(): String? {
        return queryString
    }

    fun clearQueryString() {
        queryString = null
        resetFotoList()
    }

    private fun resetFotoList() {
        pageNumber = 0
        _fotos.clear()
    }
}