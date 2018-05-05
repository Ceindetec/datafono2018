package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.events;

import com.cofrem.transacciones.models.Servicio;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;

import java.util.ArrayList;

public class CreditoScreenEvent {

    public final static int onTransaccionSuccess = 0;
    public final static int onTransaccionWSConexionError = 1;
    public final static int onTransaccionWSRegisterError = 2;
    public final static int onTransaccionDBRegisterError = 3;
    public final static int onImprecionReciboSuccess = 4;
    public final static int onImprecionReciboError = 5;


    public final static int onConsultarServiciosSuccess = 6;
    public final static int onDocumentoIncorrecto = 7;
    public final static int onTarjetaInactiva = 8;
    public final static int onTarjetaNoValida = 9;
    public final static int onCambioDeClaveObligatorio = 10;

    public final static int onCosumirServiciosSuccess = 11;
    public final static int onCosumirServiciosError = 12;
    public final static int onTerminalInactiva = 13;
    public final static int onConsultarServiciosError = 14;

    public final static int onTransaccionConError = 15;



    // Variable que maneja los tipos de eventos
    private int eventType;

    // Variable que maneja los mensajes de error de los eventos
    private String errorMessage;

    private InformacionTransaccion informacionTransaccion;

    private ArrayList<Servicio> listServicios;
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

    public ArrayList<Servicio> getListServicios() {
        return listServicios;
    }

    public void setListServicios(ArrayList<Servicio> listServicios) {
        this.listServicios = listServicios;
    }

    public InformacionTransaccion getInformacionTransaccion() {
        return informacionTransaccion;
    }

    public void setInformacionTransaccion(InformacionTransaccion informacionTransaccion) {
        this.informacionTransaccion = informacionTransaccion;
    }
}
