package com.julian.proyectoinmobiliaria.model;

public class Pagos {
    public int idPago;
    public String fechaPago;
    public double monto;
    public String detalle;
    public boolean estado;
    public int idContrato;
    public Contrato contrato;

    // constructor que recibe todos los campos
    public Pagos(int idPago, String fechaPago, double monto, String detalle, boolean estado, int idContrato, Contrato contrato) {
        this.idPago = idPago;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.detalle = detalle;
        this.estado = estado;
        this.idContrato = idContrato;
        this.contrato = contrato;
    }

    // agrego constructor vacio para que gson pueda deserializar correctamente
    public Pagos() {}
}
