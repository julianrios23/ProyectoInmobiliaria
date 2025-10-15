package com.julian.proyectoinmobiliaria.draweractivity.ui.inmuebles;

// este es el paquete donde se encuentra mi viewmodel de inmuebles

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.service.ApiService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>(); // esta es la data viva que contiene la lista de inmuebles
    private final ApiService.ServiceInterface apiService; // esta es la interfaz de mi servicio de api
    private final SharedPreferences prefs; // aqui guardo las preferencias compartidas para obtener el token

    // este es mi singleliveevent para manejar la navegacion y mostrar el dialog de nuevo inmueble
    private final SingleLiveEvent<Void> _showNuevoInmuebleDialog = new SingleLiveEvent<>();
    // esta es la forma de obtener el singleliveevent desde el fragmento o actividad
    public LiveData<Void> getShowNuevoInmuebleDialog() {
        return _showNuevoInmuebleDialog;
    }

    // este es el constructor de mi viewmodel
    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        // aqui inicializo las preferencias compartidas para el token
        prefs = application.getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        // obtengo el cliente okhttp por defecto
        OkHttpClient client = ApiService.getDefaultClient();
        // inicializo el servicio de api con el cliente okhttp
        apiService = ApiService.getApiService(client);
    }

    // este metodo me devuelve el livedata con la lista de inmuebles
    public LiveData<List<Inmueble>> getInmueblesLiveData() {
        return inmueblesLiveData;
    }

    // este metodo es para cargar la lista de inmuebles desde la api
    public void cargarInmuebles() {
        // obtengo el token de las preferencias compartidas
        String token = prefs.getString("token", "");
        // hago la llamada a la api para obtener los inmuebles
        apiService.obtenerInmuebles("Bearer " + token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            // este metodo se ejecuta cuando recibo una respuesta de la api
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                // si la respuesta es exitosa y tiene datos, actualizo el livedata de inmuebles
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> lista = response.body();
                    // ordeno la lista de inmuebles por id en orden descendente
                    Collections.sort(lista, (a, b) -> Integer.compare(b.getIdInmueble(), a.getIdInmueble()));
                    inmueblesLiveData.postValue(lista);
                } else {
                    // si la respuesta no es exitosa o no tiene datos, publico null al livedata
                    inmueblesLiveData.postValue(null);
                }
            }

            @Override
            // este metodo se ejecuta si la llamada a la api falla
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                // si la llamada falla, publico null al livedata
                inmueblesLiveData.postValue(null);
            }
        });
    }

    // este es el nuevo metodo para manejar el clic en el boton flotante de agregar inmueble
    public void onFabAgregarInmuebleClick() {
        // cuando se hace click, llamo al singleliveevent para notificar que se debe mostrar el dialog
        _showNuevoInmuebleDialog.call();
    }

    // esta es mi implementacion de singleliveevent, una clase para eventos de una sola vez
    public static class SingleLiveEvent<T> extends MutableLiveData<T> {

        private final AtomicBoolean mPending = new AtomicBoolean(false); // esta bandera asegura que el evento se consuma una sola vez

        @Override
        // este metodo observa los cambios en el livedata
        public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {

            // observo el livedata interno
            super.observe(owner, t -> {
                // si la bandera esta en true, significa que hay un evento pendiente y lo consumo
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            });
        }

        @Override
        // este metodo establece un nuevo valor para el livedata
        public void setValue(T value) {
            mPending.set(true); // establezco la bandera en true para indicar que hay un evento pendiente
            super.setValue(value);
        }

        /**
         * este metodo es para casos donde el tipo es void, para hacer las llamadas mas limpias.
         */
        // este metodo es un atajo para publicar un evento vacio
        public void call() {
            setValue(null);
        }
    }
}