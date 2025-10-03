package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Inmueble;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InmueblesViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>();
    private final InmueblesApi inmueblesApi;
    private final SharedPreferences prefs;

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    String token = prefs.getString("token", "");
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        inmueblesApi = retrofit.create(InmueblesApi.class);
    }

    public LiveData<List<Inmueble>> getInmueblesLiveData() {
        return inmueblesLiveData;
    }

    public void cargarInmuebles() {
        String token = prefs.getString("token", "");
        inmueblesApi.obtenerInmuebles("Bearer " + token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmueblesLiveData.postValue(response.body());
                } else {
                    inmueblesLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                inmueblesLiveData.postValue(null);
            }
        });
    }
}