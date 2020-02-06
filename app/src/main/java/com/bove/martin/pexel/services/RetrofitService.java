package com.bove.martin.pexel.services;

import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.util.GsonDeserializador;
import com.bove.martin.pexel.util.Util;
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
            .baseUrl(Util.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(new TypeToken<List<Foto>>() {
            }.getType(), new GsonDeserializador()).create()))
            .build();


    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}

