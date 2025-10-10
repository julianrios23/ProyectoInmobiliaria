package com.julian.proyectoinmobiliaria.model;

public class Inmueble {
    private int idInmueble;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private double superficie;
    private double latitud;
    private double valor;
    private String imagen;
    private Boolean disponible;
    private double longitud;
    private int idPropietario;
    private Propietario duenio;
    private boolean tieneContratoVigente;

    // Getters y setters
    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getAmbientes() { return ambientes; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }
    public double getSuperficie() { return superficie; }
    public void setSuperficie(double superficie) { this.superficie = superficie; }
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }
    public Propietario getDuenio() { return duenio; }
    public void setDuenio(Propietario duenio) { this.duenio = duenio; }
    public boolean isTieneContratoVigente() { return tieneContratoVigente; }
    public void setTieneContratoVigente(boolean tieneContratoVigente) { this.tieneContratoVigente = tieneContratoVigente; }
    public boolean isDisponible() {
        return disponible != null && disponible;
    }
}
