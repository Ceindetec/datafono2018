package com.cofrem.transacciones.models.modelsWS.modelEstablecimiento;

import android.content.Context;

import com.cofrem.transacciones.models.modelsWS.MessageWS;

public class Establecimiento {


    //Modelo usado en la respuesta del WS para la respuesta terminalResult

    private InformacionEstablecimiento informacionEstablecimiento;
    private ConexionEstablecimiento conexionEstablecimiento;
    private String Mensaje;
    private MessageWS messageWS;
    private Context context;

    public Establecimiento(MessageWS messageWS) {
        this.messageWS = messageWS;
    }

    public Establecimiento(InformacionEstablecimiento informacionEstablecimiento,
                           ConexionEstablecimiento conexionEstablecimiento,
                           MessageWS messageWS) {
        this.informacionEstablecimiento = informacionEstablecimiento;
        this.conexionEstablecimiento = conexionEstablecimiento;
        this.messageWS = messageWS;
    }

    public Establecimiento(String mensaje) {
        Mensaje = mensaje;
    }

    public Establecimiento(InformacionEstablecimiento informacionEstablecimiento, ConexionEstablecimiento conexionEstablecimiento, String mensaje) {
        this.informacionEstablecimiento = informacionEstablecimiento;
        this.conexionEstablecimiento = conexionEstablecimiento;
        Mensaje = mensaje;
    }

    public InformacionEstablecimiento getInformacionEstablecimiento() {
        return informacionEstablecimiento;
    }

    public void setInformacionEstablecimiento(InformacionEstablecimiento informacionEstablecimiento) {
        this.informacionEstablecimiento = informacionEstablecimiento;
    }

    public ConexionEstablecimiento getConexionEstablecimiento() {
        return conexionEstablecimiento;
    }

    public void setConexionEstablecimiento(ConexionEstablecimiento conexionEstablecimiento) {
        this.conexionEstablecimiento = conexionEstablecimiento;
    }

    public MessageWS getMessageWS() {
        return messageWS;
    }

    public void setMessageWS(MessageWS messageWS) {
        this.messageWS = messageWS;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}