package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.events;

import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.InformacionEstablecimiento;

public class RegisterConfigurationScreenEvent {

    public final static int onClaveTecnicaValida = 0;
    public final static int onClaveTecnicaNoValida = 1;
    public final static int onClaveTecnicaError = 2;

    public final static int onRegistroConfigConexionSuccess = 3;
    public final static int onRegistroConfigConexionError = 4;

    public final static int onInformacionDispositivoSuccess = 5;
    public final static int onInformacionDispositivoErrorConexion = 6;
    public final static int onInformacionDispositivoErrorInformacion = 7;

    public final static int onProccessInformacionEstablecimientoSuccess = 8;
    public final static int onProccessInformacionEstablecimientoError = 9;


    public final static int onVerifyTerminalSuccess = 10;
    public final static int onVerifyTerminalError = 11;

    public final static int onAsignaIDSuccess = 12;
    public final static int onAsignaIDError = 13;


    public final static int onVerifyConsumoWSError = 14;




    // Variable que maneja los tipos de eventos
    private int eventType;

    // Variable que maneja los mensajes de error de los eventos
    private String errorMessage;

    private Establecimiento establecimiento;

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

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }
}
