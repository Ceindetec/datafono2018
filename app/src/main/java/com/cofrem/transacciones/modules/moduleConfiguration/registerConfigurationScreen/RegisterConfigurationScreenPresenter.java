package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.events.RegisterConfigurationScreenEvent;
import com.cofrem.transacciones.models.Configurations;

public interface RegisterConfigurationScreenPresenter {

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
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param establecimiento
     */
    void setAsignaID(Context context, Establecimiento establecimiento);

    /**
     * Metodo encargado de validar la existencia de una terminal
     *
     * @param context
     * @param configurations
     */
    void validarTerminal(Context context, Configurations configurations);

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
    void onEventMainThread(RegisterConfigurationScreenEvent splashScreenEvent);

}
