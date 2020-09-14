package com.bove.martin.pexel.data.retrofit;

import com.bove.martin.pexel.data.model.Foto;
import com.bove.martin.pexel.utils.AppConstants;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Martín Bove on 05-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class RetrofitService {

    private  Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(new TypeToken<List<Foto>>() {
            }.getType(), new GsonDeserializador()).create()))
            .build();


    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}

