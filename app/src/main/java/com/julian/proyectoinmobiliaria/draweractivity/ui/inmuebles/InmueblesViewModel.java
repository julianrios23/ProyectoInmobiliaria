package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.service.ApiService;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>();
    private final ApiService.ServiceInterface apiService;
    private final SharedPreferences prefs;

    // en este bloque inicializo el viewmodel y obtengo las preferencias compartidas para el token
    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        // configuro el cliente okhttp para agregar el token en la cabecera de cada solicitud
        // Uso el cliente por defecto centralizado en ApiService
        OkHttpClient client = ApiService.getDefaultClient();
        apiService = ApiService.getApiService(client);
    }

    // en este bloque devuelvo el livedata que contiene la lista de inmuebles
    public LiveData<List<Inmueble>> getInmueblesLiveData() {
        return inmueblesLiveData;
    }

    // en este bloque defino el metodo para cargar los inmuebles desde la api
    public void cargarInmuebles() {
        String token = prefs.getString("token", "");
        apiService.obtenerInmuebles("Bearer " + token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                // si la respuesta es exitosa y contiene datos, actualizo el livedata
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> lista = response.body();
                    // muestra el ultimo id primeero
                    Collections.sort(lista, (a, b) -> Integer.compare(b.getIdInmueble(), a.getIdInmueble()));
                    inmueblesLiveData.postValue(lista);
                } else {
                    // si no, lo actualizo con nulla
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