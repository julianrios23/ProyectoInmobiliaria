package com.julian.proyectoinmobiliaria.model;

public class Contrato {
    public int idContrato;
    public String fechaInicio;
    public String fechaFinalizacion;
    public double montoAlquiler;
    public boolean estado;
    public int idInquilino;
    public int idInmueble;
    public Inquilino inquilino;
    public Inmueble inmueble;

    // agrego constructor vacio para que gson pueda deserializar correctamente
    public Contrato() {}
}
