package com.julian.proyectoinmobiliaria.service;

import com.julian.proyectoinmobiliaria.model.Propietario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/* En esta interfaz defino los metodos para realizar el login usando diferentes formas de envio de datos a la API
 Importo las anotaciones necesarias de Retrofit para definir los endpoints y el tipo de peticion
 Defino el metodo login que envia un objeto LoginRequest como cuerpo de la peticion
 Defino el metodo loginManual que permite enviar un RequestBody personalizado como cuerpo
Defino el metodo loginForm que envia los campos usuario y clave como formulario codificado
 Cada metodo retorna un Call<String> que representa la respuesta de la API*/
public interface LoginApi {

    @FormUrlEncoded
    @POST("api/Propietarios/login")
    Call<String> loginForm(@Field("Usuario") String usuario, @Field("Clave") String clave);

    @GET("api/Propietarios")
    Call<Propietario> getPropietarios();
}
