package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;

interface CreditoScreenInteractor {

    /**
     * Metodo encargado de consultar los servicios activos asociados a una tarjeta
     * @param context
     * @param transaccion
     */
    void consultarServicios(Context context, Transaccion transaccion);

    /**
     * Metodo encargado de hacer la transaccion
     * @param context
     * @param transaccion
     */
    void consumir(Context context, Transaccion transaccion );

    /**
     * Metodo encargado de manejar el exito de la transaccion
     * @param context
     * @param informacionTransaccion
     */
    void cosumirSuccess(Context context, InformacionTransaccion informacionTransaccion);
    /**
     * Metodo para obtener el numero de tarjeta desde el dispositivo
     *
     * @param context
     * @param transaccion
     */
    void registrarTransaccion(Context context, Transaccion transaccion);

    /**
     * Metodo que imprime el recibo de la transaccion
     *
     * @param context
     */
    void imprimirRecibo(Context context, String stringCopia);


}
