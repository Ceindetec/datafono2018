package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;

public interface AnulacionScreenRepository {

    /**
     * @param context
     * @param passAdmin
     */
    void validarPasswordAdministrador(Context context, String passAdmin);

    /**
     * @param context
     * @param numeroCargo
     */
    void obtenerValorTransaccion(Context context, String numeroCargo);

    /**
     * @param context
     * @param transaccion
     */
    void registrarTransaccion(Context context, Transaccion transaccion);

    /**
     *
     * @param context
     * @param informacionTransaccion
     */
    void anularSuccess(Context context, InformacionTransaccion informacionTransaccion);

    /**
     * Metodo para imprimir la copia para el cliente
     */
    void imprimirRecibo(Context context, String stringCopia);
}
