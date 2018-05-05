package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.events.RegisterConfigurationScreenEvent;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.ui.RegisterConfigurationScreenView;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.Configurations;

public class RegisterConfigurationScreenPresenterImpl implements RegisterConfigurationScreenPresenter {

    private Establecimiento establecimiento;

    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    //Declaracion del bus de eventos
    EventBus eventBus;

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface registerConfigurationScreenView
    private RegisterConfigurationScreenView registerConfigurationScreenView;

    //Instanciamiento de la interface RegisterConfigurationScreenInteractor
    private RegisterConfigurationScreenInteractor registerConfigurationScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param registerConfigurationScreenView
     */
    public RegisterConfigurationScreenPresenterImpl(RegisterConfigurationScreenView registerConfigurationScreenView) {
        this.registerConfigurationScreenView = registerConfigurationScreenView;
        this.registerConfigurationScreenInteractor = new RegisterConfigurationScreenInteractorImpl();
        this.eventBus = GreenRobotEventBus.getInstance();
    }

    /**
     * Sobrecarga del metodo onCreate de la interface SaldoScreenPresenter "crear" el registro al bus de eventos
     */
    @Override
    public void onCreate() {

        eventBus.register(this);

    }

    /**
     * Sobrecarga del metodo onDestroy de la interface SaldoScreenPresenter para "eliminar"  el registro al bus de eventos
     */
    @Override
    public void onDestroy() {
        registerConfigurationScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordTecnicoLocal(Context context, String passAdmin) {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenInteractor.validarPasswordTecnicoLocal(context, passAdmin);
        }
    }
    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordTecnicoWeb(Context context, String passAdmin) {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenInteractor.validarPasswordTecnicoWeb(context, passAdmin);
        }
    }

    /**
     * Metodo encargado de validar la existencia de una terminal
     *
     * @param context
     * @param configurations
     */
    @Override
    public void validarTerminal(Context context, Configurations configurations) {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenInteractor.validarTerminal(context, configurations);
        }
    }

    /**
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param establecimiento
     */
    @Override
    public void setAsignaID(Context context, Establecimiento establecimiento) {
        if (registerConfigurationScreenView != null) {
            this.establecimiento = establecimiento;
            this.establecimiento.setContext(context);
            registerConfigurationScreenInteractor.setAsignaID(context, establecimiento);
        }
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param registerConfigurationScreenEvent
     */
    @Override
    public void onEventMainThread(RegisterConfigurationScreenEvent registerConfigurationScreenEvent) {
        switch (registerConfigurationScreenEvent.getEventType()) {

            case RegisterConfigurationScreenEvent.onClaveTecnicaValida:
                onClaveTecnicaValida();
                break;

            case RegisterConfigurationScreenEvent.onClaveTecnicaNoValida:
                onClaveTecnicaNoValida();
                break;

            case RegisterConfigurationScreenEvent.onClaveTecnicaError:
                onClaveTecnicaError();
                break;

            case RegisterConfigurationScreenEvent.onRegistroConfigConexionSuccess:
                onRegistroConfigConexionSuccess();
                break;

            case RegisterConfigurationScreenEvent.onRegistroConfigConexionError:
                onRegistroConfigConexionError();
                break;

            case RegisterConfigurationScreenEvent.onInformacionDispositivoSuccess:
                onInformacionDispositivoSuccess();
                break;

            case RegisterConfigurationScreenEvent.onInformacionDispositivoErrorConexion:
                onInformacionDispositivoErrorConexion();
                break;

            case RegisterConfigurationScreenEvent.onInformacionDispositivoErrorInformacion:
                onInformacionDispositivoErrorInformacion();
                break;

            case RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoSuccess:
                onProccessInformacionEstablecimientoSuccess();
                break;

            case RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoError:
                onProccessInformacionEstablecimientoError();
                break;

            case RegisterConfigurationScreenEvent.onVerifyTerminalSuccess:
                onVerifyTerminalSuccess(registerConfigurationScreenEvent.getEstablecimiento());
                break;

            case RegisterConfigurationScreenEvent.onVerifyTerminalError:
                onVerifyTerminalError(registerConfigurationScreenEvent.getErrorMessage());
                break;

            case RegisterConfigurationScreenEvent.onAsignaIDSuccess:
                registerConexionEstablecimineto();
                break;

            case RegisterConfigurationScreenEvent.onAsignaIDError:
                onAsignaIDError(registerConfigurationScreenEvent.getErrorMessage());
                break;

            case RegisterConfigurationScreenEvent.onVerifyConsumoWSError:
                onInformacionDispositivoErrorConexion();
                break;

        }
    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */


    private void registerConexionEstablecimineto(){
        registerConfigurationScreenInteractor.registerConexionEstablecimineto(establecimiento);
    }

    /**
     * Metodo para manejar el password tecnico valido
     */
    private void onClaveTecnicaValida() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleClaveTecnicaValida();
        }
    }

    /**
     * Metodo para manejar el password tecnico NO valido
     */
    private void onClaveTecnicaNoValida() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleClaveTecnicaNoValida();
        }
    }

    /**
     * Metodo para manejar el error en el password tecnico
     */
    private void onClaveTecnicaError() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleClaveTecnicaError();
        }
    }

    /**
     * Metodo para manejar el registro de la configuracion exitoso
     */
    private void onRegistroConfigConexionSuccess() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleRegistroConfigConexionSuccess();
        }
    }

    /**
     * Metodo para manejar el registro de la configuracion erroneo
     */
    private void onRegistroConfigConexionError() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleRegistroConfigConexionError();
        }
    }

    /**
     * Metodo para manejar la consulta de la informacion del dispositivo exitosa
     */
    private void onInformacionDispositivoSuccess() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleInformacionDispositivoSuccess();
        }
    }

    /**
     * Metodo para manejar la consulta de la informacion del dispositivo erronea
     */
    private void onInformacionDispositivoErrorConexion() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleInformacionDispositivoErrorConexion();
        }
    }

    /**
     * Metodo para manejar la consulta de la informacion del dispositivo erronea
     */
    private void onInformacionDispositivoErrorInformacion() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleInformacionDispositivoErrorInformacion();
        }
    }

    /**
     * Metodo para manejar el registro de la informacion del dispositivo exitoso
     */
    private void onProccessInformacionEstablecimientoSuccess() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleProccessInformacionEstablecimientoSuccess();
        }
    }

    /**
     * Metodo para manejar el registro de la informacion del dispositivo erronea
     */
    private void onProccessInformacionEstablecimientoError() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleProccessInformacionEstablecimientoError();
        }
    }

    /**
     * Metodo para manejar el registro de la informacion del dispositivo erronea
     */
    private void onVerifyTerminalSuccess(Establecimiento establecimiento) {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleVerifyTerminalSuccess(establecimiento);
        }
    }

    private void onVerifyTerminalError(String error){
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleVerifyTerminalError(error);
        }
    }
    private void onAsignaIDError(String error){
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleVerifyTerminalError(error);
        }
    }

}
