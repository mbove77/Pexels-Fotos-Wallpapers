package com.bove.martin.pexel.data.retrofit

import com.bove.martin.pexel.data.model.Foto
import com.bove.martin.pexel.utils.ApiKey
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by Martín Bove on 28/05/2018.
 * E-mail: mbove77@gmail.com
 */
interface PexelService {
    @Headers("Authorization: " + ApiKey.PEXELS_TOKEN)
    @GET("curated")
    fun getCurated(@Query("per_page") itemNum: Int, @Query("page") numPage: Int): Call<List<Foto?>?>?

    @Headers("Authorization: " + ApiKey.PEXELS_TOKEN)
    @GET("search")
    fun getSearch(@Query("query") query: String?, @Query("per_page") itemNum: Int, @Query("page") numPage: Int): Call<List<Foto?>?>?

    @Headers("Authorization: " + ApiKey.PEXELS_TOKEN)
    @GET("photos")
    fun getPhoto(@Query("id") id: String?): Call<List<Foto?>?>?
}