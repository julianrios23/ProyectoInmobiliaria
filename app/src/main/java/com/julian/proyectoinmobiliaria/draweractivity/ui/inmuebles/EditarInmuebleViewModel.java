package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.service.ApiService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditarInmuebleViewModel extends AndroidViewModel {
    private ApiService.ServiceInterface apiService;
    private SharedPreferences prefs;
    private MutableLiveData<Inmueble> inmuebleActualizado = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public EditarInmuebleViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        OkHttpClient client = ApiService.getDefaultClient();
        apiService = ApiService.getApiService(client);
    }

    public MutableLiveData<Inmueble> getInmuebleActualizado() {
        return inmuebleActualizado;
    }
    public MutableLiveData<String> getError() {
        return error;
    }

    public void actualizarInmueble(Inmueble inmueble) {
        String token = prefs.getString("token", null);
        if (token == null) {
            error.postValue("Token no encontrado");
            return;
        }
        apiService.actualizarInmueble("Bearer " + token, inmueble).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmuebleActualizado.postValue(response.body());
                } else {
                    error.postValue("Error al actualizar inmueble");
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                error.postValue("Error de red: " + t.getMessage());
            }
        });
    }

    public void guardarCambios(Inmueble inmueble, String direccion, String uso, String tipo, int ambientes, int superficie, double valor) {
        inmueble.setDireccion(direccion);
        inmueble.setUso(uso);
        inmueble.setTipo(tipo);
        inmueble.setAmbientes(ambientes);
        inmueble.setSuperficie(superficie);
        inmueble.setValor(valor);
        actualizarInmueble(inmueble);
    }
}