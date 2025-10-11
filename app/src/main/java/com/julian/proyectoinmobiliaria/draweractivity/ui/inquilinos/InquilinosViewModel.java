package com.julian.proyectoinmobiliaria.draweractivity.ui.inquilinos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Inquilino;
import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.service.ApiService;
import com.julian.proyectoinmobiliaria.model.Contrato;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inquilino>> inquilinos = new MutableLiveData<>();
    private ApiService.ServiceInterface apiService;

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        OkHttpClient client = ApiService.getDefaultClient();
        apiService = ApiService.getApiService(client);
    }

    public LiveData<List<Inquilino>> getInquilinos() {
        return inquilinos;
    }

    // obtengo las preferencias compartidas para acceder al token
    public void cargarInquilinos() {
        SharedPreferences sp = getApplication().getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        // obtengo el token guardado y lo preparo para la autenticacion
        String token = sp.getString("token", "");
        String bearerToken = "Bearer " + token;
        // hago la llamada a la api para obtener los inmuebles con contrato vigente
        apiService.contratoVigente(bearerToken).enqueue(new retrofit2.Callback<List<com.julian.proyectoinmobiliaria.model.Inmueble>>() {
            @Override
            // proceso la respuesta de la api
            public void onResponse(retrofit2.Call<List<com.julian.proyectoinmobiliaria.model.Inmueble>> call, retrofit2.Response<List<com.julian.proyectoinmobiliaria.model.Inmueble>> response) {
                // si la respuesta es exitosa y tiene datos
                if (response.isSuccessful() && response.body() != null) {
                    // obtengo la lista de inmuebles
                    List<com.julian.proyectoinmobiliaria.model.Inmueble> lista = response.body();
                    // si la lista esta vacia, actualizo el livedata y salgo
                    if (lista.isEmpty()) {
                        inquilinos.postValue(new ArrayList<>());
                        return;
                    }
                    // lista para guardar los inquilinos
                    List<com.julian.proyectoinmobiliaria.model.Inquilino> listaInquilinos = new ArrayList<>();
                    // guardo el total de inmuebles y preparo un contador para saber cuando termine
                    final int total = lista.size();
                    final int[] count = {0};
                    // recorro cada inmueble para obtener su contrato y el inquilino
                    for (com.julian.proyectoinmobiliaria.model.Inmueble i : lista) {
                        // hago la llamada a la api para obtener el contrato por inmueble
                        apiService.obtenerContratoPorInmueble(bearerToken, i.getIdInmueble()).enqueue(new retrofit2.Callback<com.julian.proyectoinmobiliaria.model.Contrato>() {
                            @Override
                            // proceso la respuesta del contrato
                            public void onResponse(retrofit2.Call<com.julian.proyectoinmobiliaria.model.Contrato> call, retrofit2.Response<com.julian.proyectoinmobiliaria.model.Contrato> response) {
                                // si la respuesta es exitosa y tiene inquilino
                                if (response.isSuccessful() && response.body() != null && response.body().inquilino != null) {
                                    // obtengo el inquilino del contrato
                                    com.julian.proyectoinmobiliaria.model.Inquilino inq = response.body().inquilino;
                                    // si el contrato tiene inmueble, guardo la direccion en el inquilino
                                    if (response.body().inmueble != null) {
                                        inq.direccionInmueble = response.body().inmueble.getDireccion();
                                    }
                                    listaInquilinos.add(inq);
                                }
                                count[0]++;
                                if (count[0] == total) {
                                    inquilinos.postValue(listaInquilinos);
                                }
                            }
                            @Override
                            public void onFailure(retrofit2.Call<com.julian.proyectoinmobiliaria.model.Contrato> call, Throwable t) {
                                count[0]++;
                                if (count[0] == total) {
                                    inquilinos.postValue(listaInquilinos);
                                }
                            }
                        });
                    }
                } else {
                    inquilinos.postValue(new ArrayList<>());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<com.julian.proyectoinmobiliaria.model.Inmueble>> call, Throwable t) {
                inquilinos.postValue(new ArrayList<>());
            }
        });
    }
}