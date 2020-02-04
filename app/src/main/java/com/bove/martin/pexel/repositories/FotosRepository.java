package com.bove.martin.pexel.repositories;

import androidx.lifecycle.MutableLiveData;

import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.services.PexelService;
import com.bove.martin.pexel.util.GsonDeserializador;
import com.bove.martin.pexel.util.Util;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class FotosRepository {
    private static FotosRepository instance;
    private Retrofit retrofit;
    private PexelService service;
    private ArrayList<Foto> tempFotos = new ArrayList<>();

    public static FotosRepository getInstance() {
        if(instance == null) {
            instance = new FotosRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Foto>> getFotos(String queryString, int pageNumber, boolean resetList) {
        initRetroFitAndGson();

        MutableLiveData<List<Foto>> fotoData = new MutableLiveData<>();
        service = retrofit.create(PexelService.class);
        Call<List<Foto>> callFotos;

        if(queryString != null) {
            callFotos = service.getSearch(queryString, Util.ITEM_NUMBER, pageNumber);
        } else {
            callFotos = service.getCurated(Util.ITEM_NUMBER, pageNumber);
        }

        callFotos.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                if (response.isSuccessful()){
                    if(resetList) {
                        tempFotos.clear();
                    }
                    tempFotos.addAll(response.body());
                    fotoData.setValue(tempFotos);
                }
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {
                fotoData.setValue(null);
            }
        });
        return fotoData;
    }

    private void initRetroFitAndGson() {
        GsonBuilder builder = new GsonBuilder();
        Type assetsType = new TypeToken<List<Foto>>() {}.getType();
        builder.registerTypeAdapter(assetsType, new GsonDeserializador());

        retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
    }
}
