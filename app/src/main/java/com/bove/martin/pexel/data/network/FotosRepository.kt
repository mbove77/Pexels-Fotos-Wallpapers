package com.bove.martin.pexel.data.network

import android.content.res.Resources
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.data.network.retrofit.PexelService
import com.bove.martin.pexel.utils.AppConstants
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */

class FotosRepository @Inject constructor(private val fotosApi: PexelService) {
    private val listFotos: MutableList<Foto> = arrayListOf()

    suspend fun getCuratedFotos(queryString: String?, pageNumber: Int, resetLit:Boolean): OperationResult {

        if (resetLit) {
            listFotos.let { listFotos.clear() }
        }

        val response: Response<List<Foto>> = if (queryString != null) {
            fotosApi.getSearch(queryString, AppConstants.ITEM_NUMBER, pageNumber)
        } else {
            fotosApi.getCurated(AppConstants.ITEM_NUMBER, pageNumber)
        }

        return if (response.isSuccessful) {
            if (response.body().isNullOrEmpty()) {
                OperationResult(false, Resources.getSystem().getString(R.string.loadImageError), null)
            } else {
                response.body()?.let {
                    listFotos.addAll(it)
                }
                OperationResult(true, null, listFotos)
            }
        } else {
            OperationResult(false, Resources.getSystem().getString(R.string.connectionError), null)
        }
    }

}