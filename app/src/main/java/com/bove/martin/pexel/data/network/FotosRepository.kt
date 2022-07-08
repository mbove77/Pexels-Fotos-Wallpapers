package com.bove.martin.pexel.data.network

import android.content.Context
import com.bove.martin.pexel.AppConstants.ITEM_NUMBER
import com.bove.martin.pexel.R
import com.bove.martin.pexel.data.network.retrofit.PexelService
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.OperationResult
import com.bove.martin.pexel.presentation.utils.UiText
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */

class FotosRepository @Inject constructor(private val fotosApi: PexelService, @ApplicationContext val context: Context) {

    suspend fun getCuratedFotos(pageNumber: Int): OperationResult {
        return if(pageNumber > 0) {
            val response: Response<List<Foto>> =
                fotosApi.getCurated(ITEM_NUMBER, pageNumber)

            if (response.isSuccessful) {
                if (!response.body().isNullOrEmpty()) {
                    OperationResult(true, null, response.body())
                } else {
                    notifyError(UiText.StringResource(R.string.load_image_error))
                }
            } else {
                notifyError(UiText.StringResource(R.string.connection_error))
            }
        } else {
            notifyError(UiText.StringResource(R.string.paging_error))
        }
    }

    suspend fun getSearchedFotos(queryString: String?, pageNumber: Int): OperationResult {
        return if(pageNumber > 0) {
            if (!queryString.isNullOrEmpty()) {
                val response: Response<List<Foto>> =
                    fotosApi.getSearch(queryString, ITEM_NUMBER, pageNumber)

                if (response.isSuccessful) {
                    if (!response.body().isNullOrEmpty()) {
                        OperationResult(true, null, response.body())
                    } else {
                        notifyError(UiText.StringResource(R.string.load_image_error))
                    }
                } else {
                    notifyError(UiText.StringResource(R.string.connection_error))
                }
            } else {
                notifyError(UiText.StringResource(R.string.query_string_error))
            }
        } else {
            notifyError(UiText.StringResource(R.string.paging_error))
        }
    }

    private fun notifyError(errorString: UiText): OperationResult {
        return OperationResult(false, errorString, null)
    }
}