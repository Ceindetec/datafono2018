package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;

class RegisterConfigurationScreenInteractorImpl implements RegisterConfigurationScreenInteractor {
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
    private RegisterConfigurationScreenRepository registerConfigurationScreenRepository;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public RegisterConfigurationScreenInteractorImpl() {

        registerConfigurationScreenRepository = new RegisterConfigurationScreenRepositoryImpl();

    }
    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordTecnicoLocal(Context context, String passAdmin) {

        //Valida el acceso a la configuracion del dispositivo
        registerConfigurationScreenRepository.validarPasswordTecnicoLocal(context, passAdmin);

    }

    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordTecnicoWeb(Context context, String passAdmin) {

        //Valida el acceso a la configuracion del dispositivo
        registerConfigurationScreenRepository.validarPasswordTecnicoWeb(context, passAdmin);

    }


    /**
     * Metodo encargado de validar la existencia de una terminal
     *
     * @param context
     * @param configurations
     */
    @Override
    public void validarTerminal(Context context, Configurations configurations) {
        registerConfigurationScreenRepository.validarTerminal(context,configurations);
    }

    /**
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param establecimiento
     */
    @Override
    public void setAsignaID(Context context, Establecimiento establecimiento) {
        registerConfigurationScreenRepository.setAsignaID(context, establecimiento);
    }

    @Override
    public void registerConexionEstablecimineto(Establecimiento establecimiento) {
        registerConfigurationScreenRepository.registerConexionEstablecimineto(establecimiento);
    }
}
