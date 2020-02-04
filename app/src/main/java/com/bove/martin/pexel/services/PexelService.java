package com.bove.martin.pexel.services;

import com.bove.martin.pexel.model.Foto;

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
    @Headers("Authorization: 563492ad6f91700001000001a96d7dd310414c72ae7b60d4a55f0181")
    @GET("curated")
    Call<List<Foto>> getCurated(@Query("per_page") int itemNum, @Query("page") int numPage);

    @Headers("Authorization: 563492ad6f91700001000001a96d7dd310414c72ae7b60d4a55f0181")
    @GET("search")
    Call<List<Foto>> getSearch(@Query("query") String query, @Query("per_page") int itemNum, @Query("page") int numPage);

    @Headers("Authorization: 563492ad6f91700001000001a96d7dd310414c72ae7b60d4a55f0181")
    @GET("photos")
    Call<List<Foto>> getPhoto(@Query("id") String id);
}
