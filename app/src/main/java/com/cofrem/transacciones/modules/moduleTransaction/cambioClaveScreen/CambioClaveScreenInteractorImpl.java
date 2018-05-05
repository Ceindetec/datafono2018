package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Tarjeta;

class CambioClaveScreenInteractorImpl implements CambioClaveScreenInteractor {
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
    private CambioClaveScreenRepository genericScreenRepository;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public CambioClaveScreenInteractorImpl() {

        genericScreenRepository = new CambioClaveScreenRepositoryImpl();

    }
    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     *
     */
    @Override
    public void validateAccess(Context context) {
        //Valida el acceso a la app
        genericScreenRepository.validateAcces(context);
    }

    @Override
    public void validarPassActual(Context context, Tarjeta tarjeta) {
        genericScreenRepository.validarPassActual(context,tarjeta);
    }

    @Override
    public void validarPassNuevo(Context context, Tarjeta tarjeta, String nuevoPass) {
        genericScreenRepository.validarPassNuevo(context,tarjeta,nuevoPass);
    }
}
