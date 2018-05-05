package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen;

import android.content.Context;

import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalTransaccionREST;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.SHA256;
import com.cofrem.transacciones.lib.VolleyTransaction;
import com.cofrem.transacciones.models.Tarjeta;
import com.cofrem.transacciones.models.modelsWS.ApiWS;
import com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.events.CambioClaveScreenEvent;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class CambioClaveScreenRepositoryImpl implements CambioClaveScreenRepository {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */


    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public CambioClaveScreenRepositoryImpl() {
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
    public void validateAcces(Context context) {

        String pass ="";
//        try {
//            AESCrypt encrip = new AESCrypt();
//            pass = encrip.encrypt("1234");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        pass = SHA256.crypt("hola");

        postEvent(CambioClaveScreenEvent.onVerifySuccess);

    }

    @Override
    public void validarPassActual(Context context, Tarjeta tarjeta) {

        final HashMap<String, String> parameters = new HashMap<>();

        parameters.put("numero_tarjeta", tarjeta.getNumTarjeta());
        parameters.put("identificacion", tarjeta.getDocumento());
        parameters.put("password", tarjeta.getActual());

        VolleyTransaction volleyTransaction = new VolleyTransaction();


        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_CONSULTAR_CLAVE_TARJETA
                ,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if(data.get("estado").getAsBoolean()){
                            postEvent(CambioClaveScreenEvent.onValidarPassActualSuccess);
                        }else{

                            switch (data.get("codigo").getAsInt()){
                                case ApiWS.CODIGO_PASSWORD_INCORECTO:
                                    postEvent(CambioClaveScreenEvent.onValidarPassActualError,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_DOCUMENTIO_INCORRECTO:
                                    postEvent(CambioClaveScreenEvent.onDocumentoIncorrecto,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_INACTIVA:
                                    postEvent(CambioClaveScreenEvent.onTarjetaInactiva,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_NO_VALIDA:
                                    postEvent(CambioClaveScreenEvent.onTarjetaNoValida,data.get("mensaje").getAsString());
                                    break;
                                default:
                                    postEvent(CambioClaveScreenEvent.onTransaccionConError,data.get("mensaje").getAsString());
                            }

                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(CambioClaveScreenEvent.onTransaccionConError,errorMessage);
                    }

                });

    }

    @Override
    public void validarPassNuevo(Context context, Tarjeta tarjeta, String nuevoPass) {
        final HashMap<String, String> parameters = new HashMap<>();

        parameters.put("numero_tarjeta", tarjeta.getNumTarjeta());
        parameters.put("identificacion", tarjeta.getDocumento());
        parameters.put("password", tarjeta.getActual());
        parameters.put("nuevo_password", nuevoPass);

        VolleyTransaction volleyTransaction = new VolleyTransaction();


        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_ACTUALIZAR_CLAVE_TARJETA
                ,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if(data.get("estado").getAsBoolean()){
                            postEvent(CambioClaveScreenEvent.onValidarPassActualizarSuccess);
                        }else{

                            switch (data.get("codigo").getAsInt()){
                                case ApiWS.CODIGO_PASSWORD_DEBE_SER_NUM:
                                    postEvent(CambioClaveScreenEvent.onPasswordDedeSerNum,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_PASSWORD_INCORECTO:
                                    postEvent(CambioClaveScreenEvent.onValidarPassActualError,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_INACTIVA:
                                    postEvent(CambioClaveScreenEvent.onTarjetaInactiva,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_NO_VALIDA:
                                    postEvent(CambioClaveScreenEvent.onTarjetaNoValida,data.get("mensaje").getAsString());
                                    break;
                                default:
                                    postEvent(CambioClaveScreenEvent.onTransaccionConError,data.get("mensaje").getAsString());
                            }
//                            postEvent(CambioClaveScreenEvent.onValidarPassActualizarError,data.get("mensaje").getAsString());
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(CambioClaveScreenEvent.onTransaccionConError,errorMessage);
                    }

                });
    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage) {
        CambioClaveScreenEvent cambioClaveScreenEvent = new CambioClaveScreenEvent();
        cambioClaveScreenEvent.setEventType(type);
        if (errorMessage != null) {
            cambioClaveScreenEvent.setErrorMessage(errorMessage);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(cambioClaveScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null);

    }
}
