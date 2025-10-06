package com.julian.proyectoinmobiliaria.draweractivity.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.julian.proyectoinmobiliaria.model.Propietario;
import com.julian.proyectoinmobiliaria.service.ApiService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// aqui defino el viewmodel gestiona los datos del perfil y la logica de edicion
public class PerfilViewModel extends AndroidViewModel {
    // aqui declaro los livedata para los campos del propietario y el estado de la vista
    private MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    private final MutableLiveData<String> nombre = new MutableLiveData<>("");
    private final MutableLiveData<String> apellido = new MutableLiveData<>("");
    private final MutableLiveData<String> dni = new MutableLiveData<>("");
    private final MutableLiveData<String> telefono = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> modoEdicion = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mostrarToast = new MutableLiveData<>(false);

    private ApiService.ServiceInterface apiService;

    // inicializo el viewmodel
    public PerfilViewModel(@NonNull Application application) {
        super(application);
        // uso apiservice.getapiservice() para centralizar la instancia de retrofit y su interface. no creo la instancia manualmente aqui.
        SharedPreferences prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        // Uso el cliente por defecto centralizado en ApiService
        OkHttpClient client = ApiService.getDefaultClient();
        apiService = ApiService.getApiService(client);
    }

    // aqui  los livedata para que el fragment observe
    public LiveData<Propietario> getPropietario() { return propietario; }
    public LiveData<String> getNombre() { return nombre; }
    public LiveData<String> getApellido() { return apellido; }
    public LiveData<String> getDni() { return dni; }
    public LiveData<String> getTelefono() { return telefono; }
    public LiveData<String> getEmail() { return email; }
    public LiveData<Boolean> getModoEdicion() { return modoEdicion; }
    public LiveData<Boolean> getMostrarToast() { return mostrarToast; }

    // cambio el modo edicion econ booleano
    public void toggleModoEdicion() {
        Boolean actual = modoEdicion.getValue();
        if (actual == null) actual = false;
        modoEdicion.setValue(!actual);
    }

    // inicio la carga de los datos del perfil desde la api
    public void cargarPerfil() {
        SharedPreferences sp = getApplication().getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Call<Propietario> call = apiService.getPropietarios("Bearer " + token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Propietario p = response.body();
                    propietario.postValue(p);
                    nombre.postValue(p.getNombre());
                    apellido.postValue(p.getApellido());
                    dni.postValue(p.getDni());
                    telefono.postValue(p.getTelefono());
                    email.postValue(p.getEmail());
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // guardo los datos editados del perfil si el modo edicion esta activo
    public void guardarPerfil() {
        if (modoEdicion.getValue() != null && modoEdicion.getValue()) {
            SharedPreferences sp = getApplication().getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
            String token = sp.getString("token", "");
            Propietario p = new Propietario();
            if (propietario.getValue() != null) {
                p.setIdPropietario(propietario.getValue().getIdPropietario());
                // siempre null al enviar a la api NO CAMBIAR LA CLAVE
                p.setClave(null);
            } else {
                p.setClave(null);
            }
            p.setNombre(nombre.getValue());
            p.setApellido(apellido.getValue());
            p.setDni(dni.getValue());
            p.setTelefono(telefono.getValue());
            p.setEmail(email.getValue());
            Call<Propietario> call = apiService.actualizarPropietario("Bearer " + token, p);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Propietario actualizado = response.body();
                        propietario.postValue(actualizado);
                        nombre.postValue(actualizado.getNombre());
                        apellido.postValue(actualizado.getApellido());
                        dni.postValue(actualizado.getDni());
                        telefono.postValue(actualizado.getTelefono());
                        email.postValue(actualizado.getEmail());
                        modoEdicion.postValue(false);
                        mostrarToast.postValue(true);
                    }
                }
                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    // defino los metodos para actualizar los livedata desde el fragmento
    public void setNombre(String value) { nombre.setValue(value); }
    public void setApellido(String value) { apellido.setValue(value); }
    public void setDni(String value) { dni.setValue(value); }
    public void setTelefono(String value) { telefono.setValue(value); }
    public void setEmail(String value) { email.setValue(value); }
    public void toastMostrado() { mostrarToast.setValue(false); }

    // gestiono la logica del boton editar/guardar segun el modo edicion
    public void onBotonEditarGuardar(String nombre, String apellido, String dni, String telefono) {
        if (modoEdicion.getValue() != null && modoEdicion.getValue()) {
            setNombre(nombre);
            setApellido(apellido);
            setDni(dni);
            setTelefono(telefono);
            guardarPerfil();
        } else {
            toggleModoEdicion();
        }
    }
}