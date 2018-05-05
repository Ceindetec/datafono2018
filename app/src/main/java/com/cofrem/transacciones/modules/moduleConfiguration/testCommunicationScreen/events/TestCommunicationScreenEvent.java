package com.cofrem.transacciones.modules.moduleConfiguration.testCommunicationScreen.events;

public class TestCommunicationScreenEvent {

    public final static int onVerifySuccess = 0;
    public final static int onTestComunicationSuccess = 1;
    public final static int onTestComunicationError = 2;
    public final static int onTransaccionWSConexionError = 3;

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
