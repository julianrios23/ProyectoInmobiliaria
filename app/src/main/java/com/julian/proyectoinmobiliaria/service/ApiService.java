package com.julian.proyectoinmobiliaria.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.julian.proyectoinmobiliaria.model.Inmueble;
import com.julian.proyectoinmobiliaria.model.Propietario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class ApiService {
    static String BASE_URL = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";


public static ServiceInterface getApiService() {
    Gson gson = new GsonBuilder().setLenient().create();
    Retrofit rt = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create(gson))
            .build();
    return rt.create(ServiceInterface.class);
}



public interface ServiceInterface {
    @FormUrlEncoded
    @POST("api/Propietarios/login")
    Call<String> loginForm(@Field("Usuario") String usuario, @Field("Clave") String clave);

    @GET("api/Propietarios")
    Call<Propietario> getPropietarios(@Header("Authorization") String token);

    @GET("api/Inmuebles")
    Call<List<Inmueble>> obtenerInmuebles(@Header("Authorization") String token);

    @PUT("api/Propietarios/actualizar")
    Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);





}

}
