package com.julian.proyectoinmobiliaria.login;

import com.google.gson.annotations.SerializedName;

// declaré la clase LoginRequest para representar la solicitud de login con los campos necesarios.
// usé anotaciones de Gson para mapear los nombres de los campos al formato esperado por la API.
public class LoginRequest {
    // declaré el campo usuario y lo anoté para que se serialice como "Usuario".
    @SerializedName("Usuario")
    private String usuario;
    //  declaré el campo clave y lo anoté para que se serialice como "Clave".
    @SerializedName("Clave")
    private String clave;

    // creé el constructor que recibe usuario y clave para inicializar la solicitud.
    public LoginRequest(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    // creé el getter para obtener el usuario.
    public String getUsuario() {
        return usuario;
    }

    // creé el getter para obtener la clave.
    public String getClave() {
        return clave;
    }
}
