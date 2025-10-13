package com.julian.proyectoinmobiliaria.draweractivity.ui.pagos;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Pagos;
import com.julian.proyectoinmobiliaria.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {
    // aqui guardo la lista de pagos que obtengo del backend
    private MutableLiveData<List<Pagos>> pagosLiveData = new MutableLiveData<>();
    // esta variable indica si estoy cargando datos
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    // aqui guardo los mensajes de error si ocurre alguno
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    // referencia al servicio de la api para hacer las llamadas
    private ApiService.ServiceInterface apiService;
    // preferencias para guardar y recuperar el token
    private SharedPreferences prefs;

    // en el constructor inicializo las preferencias y el servicio de la api
    public PagosViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("token_prefs", android.content.Context.MODE_PRIVATE);
        // configuro el cliente okhttp para agregar el token en la cabecera de cada solicitud
        okhttp3.OkHttpClient client = ApiService.getDefaultClient();
        apiService = ApiService.getApiService(client);
    }

    // este metodo devuelve la lista observable de pagos
    public LiveData<List<Pagos>> getPagosLiveData() {
        return pagosLiveData;
    }

    // este metodo devuelve el estado de carga observable
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    // este metodo devuelve el mensaje de error observable
    public LiveData<String> getError() {
        return error;
    }

    // este metodo obtiene los pagos de un contrato usando la api, validando el idContrato
    public void obtenerPagosPorContrato(int idContrato) {
        // aqui valido si el idContrato es valido antes de hacer la llamada
        if (idContrato == -1) {
            error.setValue("ID de contrato invalido"); // si es invalido, informo el error
            loading.setValue(false);
            return;
        }
        loading.setValue(true); // indico que estoy cargando
        String token = prefs.getString("token", null); // obtengo el token guardado
        if (token == null) {
            loading.setValue(false); // si no hay token, dejo de cargar
            error.setValue("Token no disponible"); // muestro error
            return;
        }
        // hago la llamada a la api para obtener los pagos
        Call<List<Pagos>> call = apiService.obtenerPagosPorContrato("Bearer " + token, idContrato);
        call.enqueue(new retrofit2.Callback<List<Pagos>>() {
            @Override
            public void onResponse(Call<List<Pagos>> call, Response<List<Pagos>> response) {
                loading.setValue(false); // dejo de cargar
                if (response.isSuccessful() && response.body() != null) {
                    pagosLiveData.setValue(response.body()); // si todo sale bien, actualizo la lista de pagos
                } else {
                    error.setValue("Error al obtener pagos"); // si hay error en la respuesta, lo informo
                }
            }

            @Override
            public void onFailure(Call<List<Pagos>> call, Throwable t) {
                loading.setValue(false); // dejo de cargar
                error.setValue(t.getMessage()); // muestro el mensaje de error
            }
        });
    }
}
