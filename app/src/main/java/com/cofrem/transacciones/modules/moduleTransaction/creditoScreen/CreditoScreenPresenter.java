package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen;

import android.content.Context;

import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.events.CreditoScreenEvent;
import com.cofrem.transacciones.models.Transaccion;

public interface CreditoScreenPresenter {

    /**
     * Metodo encargado de consultar los servicios activos asociados a una tarjeta
     * @param context
     * @param transaccion
     */
    void consultarServicios(Context context, Transaccion transaccion);

    /**
     * Metodo encargado de hacer la transaccion
     *
     */
    void consumir(Context context, Transaccion transaccion);

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

    /**
     * Metodo para la creacion del presentador
     */
    void onCreate();

    /**
     * Metodo para la destruccion del presentador
     */
    void onDestroy();

    /**
     * Metodo para recibir los eventos generados
     *
     * @param creditoScreenEvent
     */
    void onEventMainThread(CreditoScreenEvent creditoScreenEvent);

}
