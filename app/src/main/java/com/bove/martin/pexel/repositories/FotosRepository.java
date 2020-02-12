package com.bove.martin.pexel.repositories;

import androidx.lifecycle.MutableLiveData;

import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.services.PexelService;
import com.bove.martin.pexel.services.RetrofitService;
import com.bove.martin.pexel.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martín Bove on 01-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class FotosRepository {
    private static FotosRepository instance;
    private ArrayList<Foto> tempFotos = new ArrayList<>();
    private PexelService fotosApi;

    public static FotosRepository getInstance() {
        if(instance == null) {
            instance = new FotosRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Foto>> getFotos(String queryString, int pageNumber, boolean resetList) {
        fotosApi = new RetrofitService().createService(PexelService.class);
        MutableLiveData<List<Foto>> fotoData = new MutableLiveData<>();
        Call<List<Foto>> callFotos;

        if(queryString != null) {
            callFotos = fotosApi.getSearch(queryString, AppConstants.ITEM_NUMBER, pageNumber);
        } else {
            callFotos = fotosApi.getCurated(AppConstants.ITEM_NUMBER, pageNumber);
        }

        callFotos.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                if (response.isSuccessful()) {
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
}
