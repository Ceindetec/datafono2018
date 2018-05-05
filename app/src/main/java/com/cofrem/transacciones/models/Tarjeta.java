package com.cofrem.transacciones.models;

/**
 * Created by luisp on 11/10/2017.
 */

public class Tarjeta {

    private String documento;
    private String numTarjeta;
    private String actual;

    public Tarjeta() {
    }

    public Tarjeta(String documento, String numTarjeta, String actual) {
        this.documento = documento;
        this.numTarjeta = numTarjeta;
        this.actual = actual;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }
}
