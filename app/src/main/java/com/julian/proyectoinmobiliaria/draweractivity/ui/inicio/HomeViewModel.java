package com.julian.proyectoinmobiliaria.draweractivity.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

// aqui cree el viewmodel para la pantalla de inicio
public class HomeViewModel extends AndroidViewModel {

    // aqui uso un livedata para mostrar un texto en la vista
    private final MutableLiveData<String> mText;

    // aqui inicializo el viewmodel y el livedata con el texto por defecto
    public HomeViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("Inicio");
    }

    // aqui expongo el livedata para que la vista lo observe
    public LiveData<String> getText() {
        return mText;
    }

    // aqui implemento la logica para cargar el mapa y pasarle el callback
    public void cargarMapa(SupportMapFragment mapFragment) {
        if (mapFragment != null) {
            MapaActual mapaActual = new MapaActual();
            mapFragment.getMapAsync(mapaActual);
        }
    }

    // aqui defino el callback que se ejecuta cuando el mapa esta listo
    public class MapaActual implements OnMapReadyCallback {
        // aqui defino la ubicacion de la inmobiliaria
        LatLng INMOBILIARIA = new LatLng(-33.30102562782761, -66.33485401397176);


        // aqui configuro el mapa cuando esta listo
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.addMarker(new MarkerOptions().position(INMOBILIARIA).title("Inmobiliaria Rios"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(INMOBILIARIA)
                    .zoom(20)
                    .bearing(10)
                    .tilt(60)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.animateCamera(cameraUpdate);
        }
    }
}