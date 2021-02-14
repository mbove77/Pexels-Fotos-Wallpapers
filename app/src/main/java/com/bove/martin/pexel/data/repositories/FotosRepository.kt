package com.bove.martin.pexel.data.repositories

import android.content.res.Resources
import com.bove.martin.pexel.R
import com.bove.martin.pexel.data.model.Foto
import com.bove.martin.pexel.data.model.OperationResult
import com.bove.martin.pexel.data.retrofit.PexelService
import com.bove.martin.pexel.data.retrofit.RetrofitService
import com.bove.martin.pexel.utils.AppConstants
import retrofit2.Response

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */
// todo recibir el servicio por el constructor para usarlo con di
class FotosRepository(private val fotosApi: PexelService) {
    //private val fotosApi: PexelService = RetrofitService().createService(PexelService::class.java)
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