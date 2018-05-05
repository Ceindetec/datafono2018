package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.events;

public class CambioClaveScreenEvent {

    public final static int onVerifySuccess = 0;

    public final static int onValidarPassActualSuccess = 1;
    public final static int onValidarPassActualError = 2;
    public final static int onValidarPassActualizarSuccess = 3;
    public final static int onValidarPassActualizarError = 4;
    public final static int onPasswordDedeSerNum = 5;

    public final static int onDocumentoIncorrecto = 6;
    public final static int onTarjetaInactiva = 7;
    public final static int onTarjetaNoValida = 8;

    public final static int onTransaccionConError = 9;



    // Variable que maneja los tipos de eventos
    private int eventType;

    // Variable que maneja los mensajes de error de los eventos
    private String errorMessage;

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
}
