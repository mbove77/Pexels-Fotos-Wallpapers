package com.bove.martin.pexel.data.network

import android.content.Context
import com.bove.martin.pexel.AppConstants.AppErrors
import com.bove.martin.pexel.AppConstants.ITEM_NUMBER
import com.bove.martin.pexel.data.network.retrofit.PexelService
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.OperationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */

class FotosRepository @Inject constructor(private val fotosApi: PexelService, @ApplicationContext val context: Context) {

    suspend fun getCuratedFotos(pageNumber: Int): OperationResult {
        return if(pageNumber < 0) {
            val response: Response<List<Foto>> =
                fotosApi.getCurated(ITEM_NUMBER, pageNumber)

            if (response.isSuccessful) {
                if (!response.body().isNullOrEmpty()) {
                    OperationResult(true, null, response.body())
                } else {
                    notifyError(AppErrors.LOAD_IMAGE_ERROR)
                }
            } else {
                notifyError(AppErrors.CONNECTION_ERROR)
            }
        } else {
            notifyError(AppErrors.PAGING_ERROR)
        }
    }

    suspend fun getSearchedFotos(queryString: String?, pageNumber: Int): OperationResult {
        return if(pageNumber < 0) {
            if (!queryString.isNullOrEmpty()) {
                val response: Response<List<Foto>> =
                    fotosApi.getSearch(queryString, ITEM_NUMBER, pageNumber)

                if (response.isSuccessful) {
                    if (!response.body().isNullOrEmpty()) {
                        OperationResult(true, null, response.body())
                    } else {
                        notifyError(AppErrors.LOAD_IMAGE_ERROR)
                    }
                } else {
                    notifyError(AppErrors.CONNECTION_ERROR)
                }
            } else {
                notifyError(AppErrors.QUERY_STRING_ERROR)
            }
        } else {
            notifyError(AppErrors.PAGING_ERROR)
        }
    }

    private fun notifyError(errorString: AppErrors): OperationResult {
        return OperationResult(false, errorString.getErrorMessage(), null)
    }
}