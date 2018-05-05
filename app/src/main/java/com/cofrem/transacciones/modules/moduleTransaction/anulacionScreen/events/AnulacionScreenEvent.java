package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.events;

public class AnulacionScreenEvent {

    public final static int VALOR_TRANSACCION_NO_VALIDO = -1;

    public final static int onClaveAdministracionValida = 0;
    public final static int onClaveAdministracionNoValida = 1;
    public final static int onClaveAdministracionError = 2;
    public final static int onNumeroCargoRelacionado = 3;
    public final static int onNumeroCargoNoRelacionado = 4;
    public final static int onTransaccionSuccess = 5;
    public final static int onTransaccionWSConexionError = 6;
    public final static int onTransaccionWSRegisterError = 7;
    public final static int onTransaccionDBRegisterError = 8;
    public final static int onImprecionReciboSuccess = 9;
    public final static int onImprecionReciboError = 10;


    // Variable que maneja los tipos de eventos
    private int eventType;

    // Variable que maneja los mensajes de error de los eventos
    private String errorMessage;

    // Variable que maneja un valor entero a enviar
    private int valorInt;

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

    public int getValorInt() {
        return valorInt;
    }

    public void setValorInt(int valorInt) {
        this.valorInt = valorInt;
    }
}
