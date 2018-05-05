package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Servicio;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;
import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.events.CreditoScreenEvent;
import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.ui.CreditoScreenView;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.Transaccion;

import java.util.ArrayList;

public class CreditoScreenPresenterImpl implements CreditoScreenPresenter {


    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    //Declaracion del bus de eventos
    EventBus eventBus;

    Context context; Transaccion transaccion;
    int intentos;

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface creditoScreenView
    private CreditoScreenView creditoScreenView;

    //Instanciamiento de la interface CreditoScreenInteractor
    private CreditoScreenInteractor creditoScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param creditoScreenView
     */
    public CreditoScreenPresenterImpl(CreditoScreenView creditoScreenView) {
        this.creditoScreenView = creditoScreenView;
        this.creditoScreenInteractor = new CreditoScreenInteractorImpl();
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
        creditoScreenView = null;
        eventBus.unregister(this);
    }

    @Override
    public void consultarServicios(Context context, Transaccion transaccion) {
        if (creditoScreenView != null) {
            intentos = 1;
            this.context = context;
            this.transaccion = transaccion;
            creditoScreenInteractor.consultarServicios(context, transaccion);
        }
    }


    @Override
    public void consumir(Context context, Transaccion transaccion) {
        if (creditoScreenView != null) {
            this.context = context;
            this.transaccion = transaccion;
            creditoScreenInteractor.consumir(context, transaccion);
        }
    }

    /**
     * Metodo para obtener el numero de tarjeta desde el dispositivo
     *
     * @param context
     * @param transaccion
     */
    @Override
    public void registrarTransaccion(Context context, Transaccion transaccion) {
        if (creditoScreenView != null) {
            creditoScreenInteractor.registrarTransaccion(context, transaccion);
        }
    }

    /**
     * Metodo que imprime el recibo de la transaccion
     *
     * @param context
     */
    @Override
    public void imprimirRecibo(Context context, String stringCopia) {
        if (creditoScreenView != null) {
            creditoScreenInteractor.imprimirRecibo(context,stringCopia);
        }
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param creditoScreenEvent
     */
    @Override
    public void onEventMainThread(CreditoScreenEvent creditoScreenEvent) {
        switch (creditoScreenEvent.getEventType()) {

            case CreditoScreenEvent.onConsultarServiciosSuccess:
                onConsultarServiciosSuccess(creditoScreenEvent.getListServicios());
                break;
            case CreditoScreenEvent.onTransaccionSuccess:
                onTransaccionSuccess();
                break;
            case CreditoScreenEvent.onTransaccionWSConexionError:
                onTransaccionWSConexionError();
                break;
            case CreditoScreenEvent.onTransaccionWSRegisterError:
                onTransaccionWSRegisterError(creditoScreenEvent.getErrorMessage());
                break;
            case CreditoScreenEvent.onTransaccionDBRegisterError:
                onTransaccionDBRegisterError();
                break;
            case CreditoScreenEvent.onImprecionReciboSuccess:
                //onImprimirSuccess();
                break;
            case CreditoScreenEvent.onImprecionReciboError:
                onImprimirError(creditoScreenEvent.getErrorMessage());
                break;
            case CreditoScreenEvent.onConsultarServiciosError:
                if(intentos <= 3){
                    creditoScreenInteractor.consultarServicios(context, transaccion);
                    intentos++;
                }else{
                    onImprimirError(creditoScreenEvent.getErrorMessage());
                }
                break;
            case CreditoScreenEvent.onTransaccionConError:
                onTransaccionConError(creditoScreenEvent.getErrorMessage());
                break;
            case CreditoScreenEvent.onTarjetaInactiva:
                onTarjetaInactiva(creditoScreenEvent.getErrorMessage());
                break;
            case CreditoScreenEvent.onTarjetaNoValida:
                onTarjetaNoValida(creditoScreenEvent.getErrorMessage());
                break;
            case CreditoScreenEvent.onCambioDeClaveObligatorio:
                onCambioDeClaveObligatorio();
                break;
            case CreditoScreenEvent.onCosumirServiciosSuccess:
                onCosumirServiciosSuccess(creditoScreenEvent.getInformacionTransaccion());
                break;
            case CreditoScreenEvent.onCosumirServiciosError:
                onCosumirServiciosError(creditoScreenEvent.getErrorMessage());
                break;
            case CreditoScreenEvent.onTerminalInactiva:
                onTarjetaInactiva(creditoScreenEvent.getErrorMessage());
                break;

        }
    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    private void onConsultarServiciosSuccess(ArrayList<Servicio> lista) {
        if (creditoScreenView != null) {
            creditoScreenView.handleConsultarServiciosSuccess(lista);
        }
    }

    private void onCosumirServiciosError(String errorMessage) {
        if (creditoScreenView != null) {
            creditoScreenView.handleCosumirServiciosError(errorMessage);
        }
    }

    private void onCosumirServiciosSuccess(InformacionTransaccion informacionTransaccion) {
        if (creditoScreenView != null) {
//            creditoScreenView.handleCosumirServiciosSuccess();
            creditoScreenInteractor.cosumirSuccess(context, informacionTransaccion);

        }
    }



    /**
     * Metodo para manejar la transaccion del Web Service Correcta
     */
    private void onTransaccionSuccess() {
        if (creditoScreenView != null) {
            creditoScreenView.handleTransaccionSuccess();
        }
    }

    /**
     * Metodo para manejar la conexion del Web Service Erronea
     */
    private void onTransaccionWSConexionError() {
        if (creditoScreenView != null) {
            creditoScreenView.handleTransaccionWSConexionError();
        }
    }

    /**
     * Metodo para manejar la transaccion erronea desde el Web Service
     *
     * @param errorMessage
     */
    private void onTransaccionWSRegisterError(String errorMessage) {
        if (creditoScreenView != null) {
            creditoScreenView.handleTransaccionWSRegisterError(errorMessage);
        }
    }

    /**
     * Metodo para manejar la transaccion erronea desde la base de datos
     */
    private void onTransaccionDBRegisterError() {
        if (creditoScreenView != null) {
            creditoScreenView.handleTransaccionDBRegisterError();
        }
    }

    /**
     * Metodo para manejar la transaccion del Web Service Correcta
     */
    private void onImprimirSuccess() {
        if (creditoScreenView != null) {
            creditoScreenView.handleImprimirReciboSuccess();
        }
    }
    /**
     * Metodo para manejar la transaccion del Web Service Correcta
     */
    private void onImprimirError(String errorMessage) {
        if (creditoScreenView != null) {
            creditoScreenView.handleImprimirReciboError(errorMessage);
        }
    }

    private void onCambioDeClaveObligatorio() {

        if (creditoScreenView != null) {
            creditoScreenView.handleMostrarErrorEnVista("Cambio Obligatorio de Clave");
        }
    }

    private void onTarjetaNoValida(String errorMessage) {
        if (creditoScreenView != null) {
            creditoScreenView.handleMostrarErrorEnVista(errorMessage);
        }
    }

    private void onTarjetaInactiva(String errorMessage) {
        if (creditoScreenView != null) {
            creditoScreenView.handleMostrarErrorEnVista(errorMessage);
        }
    }

    private void onTransaccionConError(String errorMessage) {
        if (creditoScreenView != null) {
            creditoScreenView.handleTransaccionConError(errorMessage);
        }
    }

}
