package com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.events;

import com.cofrem.transacciones.models.Transaccion;

import java.util.ArrayList;

public class ReimpresionScreenEvent {

    public final static int onVerifySuccess = 0;
    public final static int onVerifyExistenceUltimoReciboSuccess = 1;
    public final static int onVerifyExistenceUltimoReciboError = 2;
    public final static int onImprimirUltimoReciboSuccess = 3;
    public final static int onImprimirUltimoReciboError = 4;
    public final static int onVerifyExistenceReciboPorNumCargoSuccess = 5;
    public final static int onVerifyExistenceReciboPorNumCargoError = 6;
    public final static int onImprimirReciboPorNumCargoSuccess = 7;
    public final static int onImprimirReciboPorNumCargoError = 8;
    public final static int onVerifyExistenceReporteDetalleSuccess = 9;
    public final static int onVerifyExistenceReporteDetalleError = 10;
    public final static int onImprimirReporteDetalleSuccess = 11;
    public final static int onImprimirReporteDetalleError = 12;
    public final static int onVerifyExistenceReporteGeneralSuccess = 13;
    public final static int onVerifyExistenceReporteGeneralError = 14;
    public final static int onImprimirReporteGeneralSuccess = 15;
    public final static int onImprimirReporteGeneralError = 16;
    public final static int onVerifyClaveAdministradorSuccess = 17;
    public final static int onVerifyClaveAdministradorError = 18;

    public final static int onCierreLoteSuccess = 19;
    public final static int onCierreLoteError = 20;
    public final static int onTransaccionWSConexionError = 21;


    // Variable que maneja los tipos de eventos
    private int eventType;

    // Variable que maneja los mensajes de error de los eventos
    private String errorMessage;

    // Variable que maneja los resultados de las transacciones
    private Transaccion modelTransaccion;

    // Variable que maneja los resultados de las transacciones
    private ArrayList<Transaccion> listaTransacciones;

    //Getters y Setters de la clase

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Transaccion getModelTransaccion() {
        return modelTransaccion;
    }

    public void setModelTransaccion(Transaccion modelTransaccion) {
        this.modelTransaccion = modelTransaccion;
    }

    public ArrayList<Transaccion> getListaTransacciones() {
        return listaTransacciones;
    }

    public void setListaTransacciones(ArrayList<Transaccion> listaTransacciones) {
        this.listaTransacciones = listaTransacciones;
    }
}
