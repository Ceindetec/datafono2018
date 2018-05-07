package com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.ui;

import com.cofrem.transacciones.models.Servicio;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionSaldo;

import java.util.ArrayList;

public interface SaldoScreenView {

    /**
     * Metodo para manejar la verificacion exitosa
     */
    void handleTransaccionSuccess(ArrayList<Servicio> listServicios);

    /**
     * Metodo para manejar la verificacion exitosa
     */
    void handleTransaccionWSRegisterError(String errorMessage);

    /**
     * Metodo para manejar la verificacion exitosa
     */
    void handleTransaccionWSConexionError();


    /**
     * Metodo para manejar la orden de imprimir recibo exitosa
     */
    void handleImprimirReciboSuccess();

    /**
     * Metodo para manejar la orden de imprimir recibo con Error
     */
    void handleImprimirReciboError(String errorMessage);

}
