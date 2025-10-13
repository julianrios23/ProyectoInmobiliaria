package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.service.ApiService;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NuevoInmuebleViewModel extends AndroidViewModel {


    private final ApiService.ServiceInterface apiService;

    private final SharedPreferences prefs;

    private final MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>();
    // livedata para el resultado de la operacion de alta
    private final MutableLiveData<ResultInmueble> resultadoLiveData = new MutableLiveData<>();

    // inicializo el viewmodel, configuro api y preferencias
    public NuevoInmuebleViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        OkHttpClient client = ApiService.getDefaultClient();
        apiService = ApiService.getApiService(client);
    }

    // devuelvo el livedata de la lista de inmuebles
    public MutableLiveData<List<Inmueble>> getInmueblesLiveData() {
        return inmueblesLiveData;
    }

    // actualizo la lista de inmuebles
    public void setInmuebles(List<Inmueble> lista) {
        inmueblesLiveData.setValue(lista);
    }

    // metodos para actualizar campos individuales de un inmueble en la lista
    public void updateDireccion(int position, String direccion) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setDireccion(direccion);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateUso(int position, String uso) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setUso(uso);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateTipo(int position, String tipo) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setTipo(tipo);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateAmbientes(int position, int ambientes) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setAmbientes(ambientes);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateSuperficie(int position, int superficie) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setSuperficie(superficie);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateLatitud(int position, double latitud) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setLatitud(latitud);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateLongitud(int position, double longitud) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setLongitud(longitud);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateValor(int position, double valor) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setValor(valor);
            inmueblesLiveData.setValue(lista);
        }
    }
    public void updateDisponible(int position, boolean disponible) {
        List<Inmueble> lista = inmueblesLiveData.getValue();
        if (lista != null && position >= 0 && position < lista.size()) {
            lista.get(position).setDisponible(disponible);
            inmueblesLiveData.setValue(lista);
        }
    }

    // metodo para cargar inmueble con imagen usando multipart
    public void cargarInmuebleConImagen(Inmueble inmueble, java.io.File imagenFile, Callback<Inmueble> callback) {
        String token = "Bearer " + prefs.getString("token", "");
        // Convertir inmueble a JSON
        String inmuebleJson = new Gson().toJson(inmueble);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json"), inmuebleJson);
        // Crear parte de imagen
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imagenFile);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", imagenFile.getName(), requestFile);
        // Llamada Retrofit
        Call<Inmueble> call = apiService.cargarInmueble(token, imagenPart, inmuebleBody);
        call.enqueue(callback);
    }

    // obtengo el id del propietario desde preferencias
    public int getIdPropietario() {
        return prefs.getInt("idPropietario", 0);
    }

    // obtengo el token desde preferencias
    public String getToken() {
        return prefs.getString("token", "");
    }

    // devuelvo el livedata del resultado
    public MutableLiveData<ResultInmueble> getResultadoLiveData() {
        return resultadoLiveData;
    }

    //logica para dar de alta un inmueble con la foto
    public void procesarNuevoInmueble(String direccion, String uso, String tipo,
                                      String ambientes, String superficie, String latitud, String longitud, String valor,
                                      boolean disponible, boolean contratoVigente, android.net.Uri imagenUri, android.content.Context context, Runnable onSuccess) {
        // valido que todos los campos obligatorios esten completos
        if (direccion.trim().isEmpty() || uso.trim().isEmpty() || tipo.trim().isEmpty() ||
            ambientes.trim().isEmpty() || superficie.trim().isEmpty() || latitud.trim().isEmpty() ||
            longitud.trim().isEmpty() || valor.trim().isEmpty()) {
            resultadoLiveData.postValue(ResultInmueble.error("todos los campos deben estar completos"));
            return;
        }
        Inmueble inmueble = new Inmueble();
        try {
            inmueble.setDireccion(direccion);
            inmueble.setUso(uso);
            inmueble.setTipo(tipo);
            inmueble.setAmbientes(Integer.parseInt(ambientes));
            inmueble.setSuperficie(Integer.parseInt(superficie));
            inmueble.setLatitud(Double.parseDouble(latitud));
            inmueble.setLongitud(Double.parseDouble(longitud));
            inmueble.setValor(Double.parseDouble(valor));
        } catch (Exception e) {
            resultadoLiveData.postValue(ResultInmueble.error("error en los datos ingresados. verifica los valores numericos."));
            return;
        }
        inmueble.setDisponible(disponible);
        inmueble.setTieneContratoVigente(contratoVigente);
        int idPropietario = getIdPropietario();
        if (idPropietario == 0) {
            resultadoLiveData.postValue(ResultInmueble.error("error: el id del propietario no es valido."));
            return;
        }
        inmueble.setIdPropietario(idPropietario);
        inmueble.setImagen("");
        inmueble.setDuenio(null);
        inmueble.setContrato(null);
        java.io.File imagenFile = null;
        if (imagenUri != null) {
            String path = com.julian.proyectoinmobiliaria.util.ManejoImagenes.getPathFromUri(context, imagenUri);
            if (path != null) {
                imagenFile = new java.io.File(path);
            }
        }
        String token = getToken();
        if (token == null || token.trim().isEmpty()) {
            resultadoLiveData.postValue(ResultInmueble.error("token de autenticacion no disponible."));
            return;
        }
        // callback para manejar la respuesta de la api
        Callback<Inmueble> callback = new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    resultadoLiveData.postValue(ResultInmueble.exito("inmueble guardado "));
                    if (onSuccess != null) onSuccess.run();
                } else {
                    String errorMsg = "error al guardar inmueble: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += "\n" + response.errorBody().string();
                        }
                    } catch (Exception e) { }
                    resultadoLiveData.postValue(ResultInmueble.error(errorMsg));
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                resultadoLiveData.postValue(ResultInmueble.error("error de red: " + t.getMessage()));
            }
        };
        if (imagenFile != null) {
            cargarInmuebleConImagen(inmueble, imagenFile, callback);
        } else {
            String tokenBearer = "Bearer " + token;
            Call<Inmueble> call = apiService.nuevoInmueble(tokenBearer, inmueble);
            call.enqueue(callback);
        }
    }

    // clase para decirlñe el resultado al fragment
    public static class ResultInmueble {
        public final boolean exito;
        public final String mensaje;
        private ResultInmueble(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }
        public static ResultInmueble exito(String mensaje) { return new ResultInmueble(true, mensaje); }
        public static ResultInmueble error(String mensaje) { return new ResultInmueble(false, mensaje); }
    }
}