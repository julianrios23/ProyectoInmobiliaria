package com.julian.proyectoinmobiliaria.service;

import com.julian.proyectoinmobiliaria.model.Inmueble;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface InmueblesApi {
    @GET("api/Inmuebles")
    Call<List<Inmueble>> obtenerInmuebles(@Header("Authorization") String token);
}

