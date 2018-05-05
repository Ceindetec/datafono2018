package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;

class CreditoScreenInteractorImpl implements CreditoScreenInteractor {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    private CreditoScreenRepository creditoScreenRepository;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public CreditoScreenInteractorImpl() {

        creditoScreenRepository = new CreditoScreenRepositoryImpl();

    }
    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    @Override
    public void consultarServicios(Context context, Transaccion transaccion) {
        creditoScreenRepository.consultarServicios(context, transaccion);
    }

    @Override
    public void consumir(Context context, Transaccion transaccion) {
        creditoScreenRepository.consumir(context, transaccion);
    }

    @Override
    public void cosumirSuccess(Context context, InformacionTransaccion informacionTransaccion) {
        creditoScreenRepository.cosumirSuccess(context,informacionTransaccion);
    }

    /**
     * Metodo para obtener el numero de tarjeta desde el dispositivo
     *
     * @param context
     * @param transaccion
     */
    @Override
    public void registrarTransaccion(Context context, Transaccion transaccion) {
        //Valida el acceso a la app
        creditoScreenRepository.registrarTransaccion(context, transaccion);
    }

    @Override
    public void imprimirRecibo(Context context, String stringCopia) {
        //Imprime recibo
        creditoScreenRepository.imprimirRecibo(context,stringCopia);
    }
}
