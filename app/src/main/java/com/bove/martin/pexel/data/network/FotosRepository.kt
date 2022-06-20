package com.bove.martin.pexel.data.network

import android.content.Context
import android.content.res.Resources
import com.bove.martin.pexel.AppConstants
import com.bove.martin.pexel.R
import com.bove.martin.pexel.data.network.retrofit.PexelService
import com.bove.martin.pexel.domain.model.Foto
import com.bove.martin.pexel.domain.model.OperationResult
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */

class FotosRepository @Inject constructor(private val fotosApi: PexelService, @ApplicationContext val context: Context) {

    suspend fun getCuratedFotos(pageNumber: Int): OperationResult {

        val response: Response<List<Foto>> =
            fotosApi.getCurated(AppConstants.ITEM_NUMBER, pageNumber)

        return if (response.isSuccessful) {
            if (response.body().isNullOrEmpty()) {
                OperationResult(false, Resources.getSystem().getString(R.string.loadImageError), null)
            } else {
                OperationResult(true, null, response.body())
            }
        } else {
            OperationResult(false, Resources.getSystem().getString(R.string.connectionError), null)
        }
    }

    suspend fun getSearchedFotos(queryString: String?, pageNumber: Int): OperationResult {

        if(!queryString.isNullOrEmpty()) {
            val response: Response<List<Foto>> =
                fotosApi.getSearch(queryString, AppConstants.ITEM_NUMBER, pageNumber)

            return if (response.isSuccessful) {
                if (response.body().isNullOrEmpty()) {
                    Logger.w(context.getString(R.string.loadImageError))

                    OperationResult(
                        false,
                        context.getString(R.string.loadImageError),
                        null
                    )
                } else {
                    OperationResult(true, null, response.body())
                }
            } else {
                OperationResult(
                    false,
                    context.getString(R.string.connectionError),
                    null
                )
            }
        } else {
            return OperationResult(
                false,
                context.getString(R.string.queryStringError),
                null)
        }
    }
}