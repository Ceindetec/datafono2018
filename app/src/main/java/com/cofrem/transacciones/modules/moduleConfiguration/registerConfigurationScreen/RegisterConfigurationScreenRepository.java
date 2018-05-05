package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;

public interface RegisterConfigurationScreenRepository {
    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    void validarPasswordTecnicoLocal(Context context, String passAdmin);

    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    void validarPasswordTecnicoWeb(Context context, String passAdmin);

    /**
     * Metodo encargado de validar la existencia de una terminal
     *
     * @param context
     * @param configurations
     */
    void validarTerminal(Context context, Configurations configurations);


    /**
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param establecimiento
     */
    void setAsignaID(Context context, Establecimiento establecimiento);


    void registerConexionEstablecimineto(Establecimiento establecimiento);
}
