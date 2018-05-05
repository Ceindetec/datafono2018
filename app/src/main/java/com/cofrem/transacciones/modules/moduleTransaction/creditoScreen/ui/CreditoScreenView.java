package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.ui;

import com.cofrem.transacciones.models.Servicio;

import java.util.ArrayList;

public interface CreditoScreenView {

    /**
     * Metodo para manejar la transaccion del Web Service Correcta
     */
    void handleTransaccionSuccess();

    /**
     * Metodo para manejar la conexion del Web Service Erronea
     */
    void handleTransaccionWSConexionError();

    /**
     * Metodo para manejar la transaccion erronea desde el Web Service
     *
     * @param errorMessage
     */
    void handleTransaccionWSRegisterError(String errorMessage);

    /**
     * Metodo para manejar la transaccion erronea desde la base de datos
     */
    void handleTransaccionDBRegisterError();

    /**
     * Metodo para manejar la orden de imprimir recibo exitosa
     */
    void handleImprimirReciboSuccess();

    /**
     * Metodo para manejar la orden de imprimir recibo con Error
     */
    void handleImprimirReciboError(String errorMessage);

    void handleConsultarServiciosSuccess(ArrayList<Servicio> lista);

    void handleTransaccionConError(String error);

    void handleMostrarErrorEnVista (String error);

    void handleCosumirServiciosError(String errorMessage);

    void handleCosumirServiciosSuccess();
}
