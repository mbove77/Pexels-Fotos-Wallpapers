package com.bove.martin.pexel.services;
import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Martín Bove on 28/05/2018.
 * E-mail: mbove77@gmail.com
 */
public interface PexelService {
    @Headers("Authorization: " + AppConstants.PEXELS_TOKEN)
    @GET("curated")
    Call<List<Foto>> getCurated(@Query("per_page") int itemNum, @Query("page") int numPage);

    @Headers("Authorization: " + AppConstants.PEXELS_TOKEN)
    @GET("search")
    Call<List<Foto>> getSearch(@Query("query") String query, @Query("per_page") int itemNum, @Query("page") int numPage);

    @Headers("Authorization: " + AppConstants.PEXELS_TOKEN)
    @GET("photos")
    Call<List<Foto>> getPhoto(@Query("id") String id);
}
