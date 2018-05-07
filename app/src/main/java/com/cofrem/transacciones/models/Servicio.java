package com.cofrem.transacciones.models;

import android.graphics.drawable.Drawable;

/**
 * Created by luisp on 17/10/2017.
 */

public class Servicio {

    private String codigo;
    private String descripcion;
    private String valor;
    private Drawable imagen;

    public Servicio() {
        super();
    }

    public Servicio(String codigo, String descripcion,String valor, Drawable imagen) {
        super();
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.valor = valor;
        this.imagen = imagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }
}
