package com.bove.martin.pexel.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bove.martin.pexel.data.model.Foto
import com.bove.martin.pexel.data.model.Search
import com.bove.martin.pexel.data.repositories.FotosRepository
import com.bove.martin.pexel.data.repositories.PopularSearchesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val photoRepository: FotosRepository, private val popularSearchesRepository: PopularSearchesRepository) : ViewModel() {
    val fotos = MutableLiveData<List<Foto>>()
    val queryString = MutableLiveData<String?>()
    val searches = MutableLiveData<List<Search>>()

    private var pageNumber = 1

    // todo unir esta funcion con getFotos
    fun getMoreFotos(resetList: Boolean) {
        pageNumber++
        getFotos(resetList)
    }

    fun getFotos(resetList: Boolean) {
        if (resetList) {pageNumber = 1}
        viewModelScope.launch(Dispatchers.IO) {
            val response = photoRepository.getCuratedFotos(queryString.value, pageNumber, resetList)

            if (response.operationResult) {
                withContext(Dispatchers.Main) {
                    fotos.postValue(response.resultObject as List<Foto>)
                }
            }
        }
    }

    fun getSearchOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = popularSearchesRepository.searches

            if (!response.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    searches.postValue(response)
                }
            }
        }
    }

    fun setQueryString(value:String?) {
        queryString.postValue(value)
    }
}