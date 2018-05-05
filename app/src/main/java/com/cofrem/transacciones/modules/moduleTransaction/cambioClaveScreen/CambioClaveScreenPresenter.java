package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Tarjeta;
import com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.events.CambioClaveScreenEvent;

public interface CambioClaveScreenPresenter {

    //Todo: crear metodos presentador

    /**
     * metodo presentador
     */
    void VerifySuccess(Context context);

    /**
     * Metodo encargado de validar la informacion de la tarjeta
     * @param context
     * @param tarjeta
     */
    void validarPassActual(Context context, Tarjeta tarjeta);

    /**
     * Metodo encargado de validar la informacion de la tarjeta
     * @param context
     * @param tarjeta
     * @param nuevoPass
     */
    void validarPassNuevo(Context context, Tarjeta tarjeta,String nuevoPass);


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
     * @param splashScreenEvent
     */
    void onEventMainThread(CambioClaveScreenEvent splashScreenEvent);

}
