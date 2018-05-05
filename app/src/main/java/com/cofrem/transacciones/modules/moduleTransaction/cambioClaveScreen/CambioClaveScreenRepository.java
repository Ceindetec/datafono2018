package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Tarjeta;

public interface CambioClaveScreenRepository {

    void validateAcces(Context context);

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
}
