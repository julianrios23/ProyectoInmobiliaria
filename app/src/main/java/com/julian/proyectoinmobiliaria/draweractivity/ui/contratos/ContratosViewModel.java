package com.julian.proyectoinmobiliaria.draweractivity.ui.contratos;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.model.Contrato;
import com.julian.proyectoinmobiliaria.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> inmueblesVigentes = new MutableLiveData<>();
    private MutableLiveData<List<Contrato>> contratosVigentes = new MutableLiveData<>();
    private final ApiService.ServiceInterface apiService;
    private final SharedPreferences prefs;

    // inicializo el viewmodel y obtengo las preferencias compartidas para el token
    public ContratosViewModel(Application application) {
        super(application);
        prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        apiService = ApiService.getApiService(ApiService.getDefaultClient());
    }

    // devuelvo el livedata que contiene la lista de inmuebles vigentes
    public LiveData<List<Inmueble>> getInmueblesVigentes() {
        return inmueblesVigentes;
    }

    // devuelvo el livedata que contiene la lista de contratos vigentes
    public LiveData<List<Contrato>> getContratosVigentes() {
        return contratosVigentes;
    }

    // defino el enum para manejar el estado de la interfaz
    public enum EstadoUI {
        MOSTRAR_LISTA,
        MOSTRAR_VACIO
    }
    private final MutableLiveData<EstadoUI> estadoUI = new MutableLiveData<>();
    // devuelvo el livedata que contiene el estado de la interfaz
    public LiveData<EstadoUI> getEstadoUI() {
        return estadoUI;
    }

    // Clase para representar el estado de visibilidad de la UI
    public static class UIState {
        public final int rvVisibility;
        public final int tvEmptyVisibility;
        public UIState(int rvVisibility, int tvEmptyVisibility) {
            this.rvVisibility = rvVisibility;
            this.tvEmptyVisibility = tvEmptyVisibility;
        }
    }
    private final MutableLiveData<UIState> uiState = new MutableLiveData<>();
    public LiveData<UIState> getUIState() {
        return uiState;
    }

    // Actualiza el estado de la UI según el estado actual
    private void actualizarUIState(EstadoUI estado) {
        if (estado == EstadoUI.MOSTRAR_LISTA) {
            uiState.postValue(new UIState(android.view.View.VISIBLE, android.view.View.GONE));
        } else {
            uiState.postValue(new UIState(android.view.View.GONE, android.view.View.VISIBLE));
        }
    }

    // defino el metodo para cargar los inmuebles vigentes desde la api
    public void cargarInmueblesVigentes() {
        String token = prefs.getString("token", "");
        String bearerToken = "Bearer " + token;

        apiService.contratoVigente(bearerToken).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                // si la respuesta es exitosa y contiene datos, actualizo el livedata y el estado de la interfaz
                android.util.Log.d("CONTRATOS", "onResponse ejecutado");
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> lista = response.body();
                    android.util.Log.d("CONTRATOS", "Cantidad de inmuebles recibidos: " + lista.size());
                    for (Inmueble i : lista) {
                        android.util.Log.d("CONTRATOS", "ID: " + i.getIdInmueble() + ", Direccion: " + i.getDireccion() + ", Contrato vigente: " + i.isTieneContratoVigente());
                        // agrego log para mostrar el valor de la imagen
                        android.util.Log.d("CONTRATOS", "Imagen: " + i.getImagen());
                    }
                    inmueblesVigentes.postValue(lista);
                    estadoUI.postValue(lista != null && !lista.isEmpty() ? EstadoUI.MOSTRAR_LISTA : EstadoUI.MOSTRAR_VACIO);
                    actualizarUIState(lista != null && !lista.isEmpty() ? EstadoUI.MOSTRAR_LISTA : EstadoUI.MOSTRAR_VACIO);
                } else {
                    // si no, actualizo el livedata con null y el estado de la interfaz como vacio
                    android.util.Log.d("CONTRATOS", "Respuesta no exitosa o body nulo");
                    inmueblesVigentes.postValue(null);
                    estadoUI.postValue(EstadoUI.MOSTRAR_VACIO);
                    actualizarUIState(EstadoUI.MOSTRAR_VACIO);
                }
            }
            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                // si ocurre un error, actualizo el livedata con null y el estado de la interfaz como vacio
                android.util.Log.e("CONTRATOS", "Error al obtener inmuebles: " + t.getMessage(), t);
                inmueblesVigentes.postValue(null);
                estadoUI.postValue(EstadoUI.MOSTRAR_VACIO);
                actualizarUIState(EstadoUI.MOSTRAR_VACIO);
            }
        });
    }

    // defino el metodo para cargar los contratos vigentes desde la api
    public void cargarContratosVigentes() {
        String token = prefs.getString("token", "");
        String bearerToken = "Bearer " + token;
        apiService.contratoVigente(bearerToken).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> lista = response.body();
                    android.util.Log.d("CONTRATOS", "Cantidad de inmuebles recibidos: " + lista.size());
                    if (lista.isEmpty()) {
                        contratosVigentes.postValue(new ArrayList<>());
                        estadoUI.postValue(EstadoUI.MOSTRAR_VACIO);
                        actualizarUIState(EstadoUI.MOSTRAR_VACIO);
                        return;
                    }
                    List<Contrato> contratos = new ArrayList<>();
                    final int total = lista.size();
                    final int[] count = {0};
                    for (Inmueble i : lista) {
                        apiService.obtenerContratoPorInmueble(bearerToken, i.getIdInmueble()).enqueue(new Callback<Contrato>() {
                            @Override
                            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    contratos.add(response.body());
                                }
                                count[0]++;
                                if (count[0] == total) {
                                    contratosVigentes.postValue(contratos);
                                    estadoUI.postValue(contratos.isEmpty() ? EstadoUI.MOSTRAR_VACIO : EstadoUI.MOSTRAR_LISTA);
                                    actualizarUIState(contratos.isEmpty() ? EstadoUI.MOSTRAR_VACIO : EstadoUI.MOSTRAR_LISTA);
                                }
                            }
                            @Override
                            public void onFailure(Call<Contrato> call, Throwable t) {
                                count[0]++;
                                if (count[0] == total) {
                                    contratosVigentes.postValue(contratos);
                                    estadoUI.postValue(contratos.isEmpty() ? EstadoUI.MOSTRAR_VACIO : EstadoUI.MOSTRAR_LISTA);
                                    actualizarUIState(contratos.isEmpty() ? EstadoUI.MOSTRAR_VACIO : EstadoUI.MOSTRAR_LISTA);
                                }
                            }
                        });
                    }
                } else {
                    contratosVigentes.postValue(new ArrayList<>());
                    estadoUI.postValue(EstadoUI.MOSTRAR_VACIO);
                    actualizarUIState(EstadoUI.MOSTRAR_VACIO);
                }
            }
            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                contratosVigentes.postValue(new ArrayList<>());
                estadoUI.postValue(EstadoUI.MOSTRAR_VACIO);
                actualizarUIState(EstadoUI.MOSTRAR_VACIO);
            }
        });
    }
}