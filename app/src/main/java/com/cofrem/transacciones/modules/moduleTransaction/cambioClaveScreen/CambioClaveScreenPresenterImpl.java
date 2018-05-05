package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen;

import android.content.Context;

import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.Tarjeta;
import com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.events.CambioClaveScreenEvent;
import com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.ui.CambioClaveScreenView;

public class CambioClaveScreenPresenterImpl implements CambioClaveScreenPresenter {


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
    //Instanciamiento de la interface cambioClaveScreenView
    private CambioClaveScreenView cambioClaveScreenView;

    //Instanciamiento de la interface CambioClaveScreenInteractor
    private CambioClaveScreenInteractor genericScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param cambioClaveScreenView
     */
    public CambioClaveScreenPresenterImpl(CambioClaveScreenView cambioClaveScreenView) {
        this.cambioClaveScreenView = cambioClaveScreenView;
        this.genericScreenInteractor = new CambioClaveScreenInteractorImpl();
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
        cambioClaveScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * Metodo para la verificacion de los datos
     */
    @Override
    public void VerifySuccess(Context context) {
        if (cambioClaveScreenView != null) {
            genericScreenInteractor.validateAccess(context);
        }
    }

    @Override
    public void validarPassActual(Context context, Tarjeta tarjeta) {
        if (cambioClaveScreenView != null) {
            genericScreenInteractor.validarPassActual(context,tarjeta);
        }
    }

    @Override
    public void validarPassNuevo(Context context, Tarjeta tarjeta, String nuevoPass) {
        if (cambioClaveScreenView != null) {
            genericScreenInteractor.validarPassNuevo(context,tarjeta,nuevoPass);
        }
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param cambioClaveScreenEvent
     */
    @Override
    public void onEventMainThread(CambioClaveScreenEvent cambioClaveScreenEvent) {
        switch (cambioClaveScreenEvent.getEventType()) {

            case CambioClaveScreenEvent.onVerifySuccess:
                onVerifySuccess();
                break;
            case CambioClaveScreenEvent.onValidarPassActualSuccess:
                onValidarPassActualSuccess();
                break;
            case CambioClaveScreenEvent.onValidarPassActualError:
                onValidarPassActualError(cambioClaveScreenEvent.getErrorMessage());
                break;
            case CambioClaveScreenEvent.onPasswordDedeSerNum:
                onPasswordDedeSerNum(cambioClaveScreenEvent.getErrorMessage());
                break;
            case CambioClaveScreenEvent.onValidarPassActualizarSuccess:
                onValidarPassActualizarSuccess();
                break;
            case CambioClaveScreenEvent.onValidarPassActualizarError:
                onValidarPassActualizarError(cambioClaveScreenEvent.getErrorMessage());
                break;
            case CambioClaveScreenEvent.onDocumentoIncorrecto:
                onDocumentoIncorrecto(cambioClaveScreenEvent.getErrorMessage());
                break;
            case CambioClaveScreenEvent.onTarjetaInactiva:
                onTarjetaInactiva(cambioClaveScreenEvent.getErrorMessage());
                break;
            case CambioClaveScreenEvent.onTarjetaNoValida:
                onTarjetaNoValida(cambioClaveScreenEvent.getErrorMessage());
                break;
            case CambioClaveScreenEvent.onTransaccionConError:
                onTransaccionConError(cambioClaveScreenEvent.getErrorMessage());
                break;


        }
    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onVerifySuccess() {
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleVerifySuccess();
        }
    }

    private void onValidarPassActualSuccess(){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleValidarPassActualSuccess();
        }
    }

    private void onValidarPassActualError(String Error){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleValidarPassActualError(Error);
        }
    }
    private void onValidarPassActualizarSuccess(){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleValidarPassActualizarSuccess();
        }
    }

    private void onValidarPassActualizarError(String error){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleValidarPassActualizarError(error);
        }
    }

    private void onPasswordDedeSerNum(String errorMessage) {
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handlePasswordDedeSerNum(errorMessage);
        }
    }
    private void onDocumentoIncorrecto(String Error){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleDocumentoIncorrecto(Error);
        }
    }

    private void onTarjetaInactiva(String Error){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleTarjetaInactiva(Error);
        }
    }

    private void onTarjetaNoValida(String Error){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleTarjetaNoValida(Error);
        }
    }

    private void onTransaccionConError(String Error){
        if (cambioClaveScreenView != null) {
            cambioClaveScreenView.handleTransaccionConError(Error);
        }
    }


}
