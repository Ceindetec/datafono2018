package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.cofrem.transacciones.global.InfoGlobalTransaccionREST;
import com.cofrem.transacciones.lib.VolleyTransaction;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.events.RegisterConfigurationScreenEvent;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.lib.MD5;
import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.ConexionEstablecimiento;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.InformacionEstablecimiento;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.modules.moduleConfiguration.testCommunicationScreen.events.TestCommunicationScreenEvent;
import com.google.gson.JsonObject;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class RegisterConfigurationScreenRepositoryImpl implements RegisterConfigurationScreenRepository {

    public static Configurations configurations;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public RegisterConfigurationScreenRepositoryImpl() {
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

        int validateExistValorAcceso = AppDatabase.getInstance(context).validarAccesoByClaveTecnica(MD5.crypt(passAdmin));

        switch (validateExistValorAcceso) {
            case 0:
                postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaNoValida);
                break;
            case 1:
                postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaValida);
                break;
            default:
                postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaError);
                break;
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

        final HashMap<String, String> parameters = new HashMap<>();

        String codigo = AppDatabase.getInstance(context).obtenerCodigoTerminal();

        parameters.put("codigo", codigo);
        parameters.put("password", passAdmin);

        VolleyTransaction volleyTransaction = new VolleyTransaction();

        //Se crea una nueva instancia del model establecimiento para ser retornada

        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_CLAVE_TERMINAL,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        if (data.get(InfoGlobalTransaccionREST.KEY_JSON_ESTADO).getAsBoolean()) {
                            postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaValida);
                        }else{
                            postEvent(RegisterConfigurationScreenEvent.onClaveTecnicaNoValida);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(RegisterConfigurationScreenEvent.onVerifyConsumoWSError,errorMessage);

                    }
                });




    }

    /**
     * Metodo encargado de validar la existencia de una terminal
     *
     * @param context
     * @param configurations
     */
    @Override
    public void validarTerminal(Context context, Configurations configurations) {

        this.configurations = configurations;

        final HashMap<String, String> parameters = new HashMap<>();

        String codigo = configurations.getCodigoDispositivo();

        while (codigo.length() < 15) {
            codigo = "0"+codigo;
        }

        parameters.put("codigo", codigo);

        VolleyTransaction volleyTransaction = new VolleyTransaction();

        //Se crea una nueva instancia del model establecimiento para ser retornada
        
        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        configurations.getHost() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_TERMINAL,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if (data.get(InfoGlobalTransaccionREST.KEY_JSON_ESTADO).getAsBoolean()) {

                            Establecimiento establecimiento = null;

                            //Inicializacion del modelo InformacionEstablecimiento
                            InformacionEstablecimiento informacionEstablecimiento = new InformacionEstablecimiento(data.get(InfoGlobalTransaccionREST.KEY_JSON_DATA).getAsJsonObject());

                            ConexionEstablecimiento conexionEstablecimienton = new ConexionEstablecimiento(data.get(InfoGlobalTransaccionREST.KEY_JSON_DATA).getAsJsonObject());

                            establecimiento = new Establecimiento(informacionEstablecimiento,conexionEstablecimienton,data.get(InfoGlobalTransaccionREST.KEY_JSON_MENSAJE).getAsString());
                            
                            postEvent(RegisterConfigurationScreenEvent.onVerifyTerminalSuccess,establecimiento);
                        }else{
                            postEvent(RegisterConfigurationScreenEvent.onVerifyTerminalError,data.get(InfoGlobalTransaccionREST.KEY_JSON_MENSAJE).getAsString());
                        }
                        
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(RegisterConfigurationScreenEvent.onVerifyConsumoWSError,errorMessage);

                    }
                });
        
    }

    /**
     * setAsignaID
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param establecimiento
     */
    @Override
    public void setAsignaID(Context context, Establecimiento establecimiento) {

        String myIMEI = "" ;
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null){
            myIMEI = mTelephony.getDeviceId();
        }

        String myMAC = "";
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiMan!=null) {
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            if (wifiInf!=null) {
                myMAC = wifiInf.getMacAddress();
            }
        }

        String uuid = UUID.randomUUID().toString();

        final HashMap<String, String> parameters = new HashMap<>();

        String codigo = configurations.getCodigoDispositivo();

        while (codigo.length() < 15) {
            codigo = "0"+codigo;
        }

        parameters.put("codigo", codigo);
        parameters.put("uuid", uuid);
        parameters.put("imei", myIMEI);
        parameters.put("mac", myMAC);

        VolleyTransaction volleyTransaction = new VolleyTransaction();

        //Se crea una nueva instancia del model establecimiento para ser retornada


        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        configurations.getHost() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_ASIGNAID,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if (data.get("estado").getAsBoolean()) {

                            postEvent(RegisterConfigurationScreenEvent.onAsignaIDSuccess);
                        }else{
                            postEvent(RegisterConfigurationScreenEvent.onAsignaIDError);
                        }


                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(RegisterConfigurationScreenEvent.onVerifyConsumoWSError,errorMessage);
                    }

                });




    }
    

    /**
     * Registra los parametros de conexion del dispositivo
     *
     * @param establecimiento
     */
    @Override
    public void registerConexionEstablecimineto(Establecimiento establecimiento) {

        Context context = establecimiento.getContext();

        //Si la informacion del estableceimiento no es nula, la recoleccion de la informacion es correcta
        if (establecimiento != null) {

                //Se registra la configuracion de la conexion en el dispositivo
                if (AppDatabase.getInstance(context).insertConfiguracionConexion(establecimiento.getConexionEstablecimiento())) {

                    //Se registra la configuracion de la conexion en el establecimiento
                    if (AppDatabase.getInstance(context).processInfoEstablecimiento(establecimiento)) {

                        AppDatabase.getInstance(context).obtenerInfoHeader();

                        //Evento Correcto de registro de informacion del establecimiento desde el WS
                        // y actualizacion de accesos
                        postEvent(RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoSuccess);

                    } else {

                        //Evento Erroneo de registro de informacion del establecimiento desde el WS y actualizacion de accesos
                        postEvent(RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoError);

                    }
                } else {

                    //Evento Erroneo de registro de configuracion de la conexion en el establecimiento
                    postEvent(RegisterConfigurationScreenEvent.onRegistroConfigConexionError);
                }
            
        } else {

            AppDatabase.getInstance(context).deleteConfiguracionConexion();

            //Evento Erroneo de recepcion de informacion del establecimiento desde el WS
            postEvent(RegisterConfigurationScreenEvent.onInformacionDispositivoErrorConexion);

        }

    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################     *
     */

    /**
     * Metodo que:
     * - valida mediante el WS la existencia del dispositivo
     * - Extrae la informacion del establecimiento
     *
     * @param context
     * @param configurations
     * @return
     */
     private void validarInfoDispositivo(Context context, Configurations configurations) {

//        //Se crea una nueva instancia del model establecimiento para ser retornada
//        Establecimiento establecimiento = null;
//
//        //Inicializacion y declaracion de parametros para la peticion web service
//        String[] params = new String[]{InfoGlobalTransaccionSOAP.PARAM_NAME_TERMINAL_CODIGO_TERMINAL, configurations.getCodigoDispositivo()};
//
//        //Creacion del modelo TransactionWS para ser usado dentro del webservice
//        TransactionWS transactionWS = new TransactionWS(
//                InfoGlobalTransaccionSOAP.HTTP + configurations.getHost() + ":" + configurations.getPort() + InfoGlobalTransaccionSOAP.WEB_SERVICE_URI,
//                InfoGlobalTransaccionSOAP.HTTP + InfoGlobalTransaccionSOAP.NAME_SPACE,
//                InfoGlobalTransaccionSOAP.METHOD_NAME_TERMINAL,
//                new String[][]{params});
//
//        //Inicializacion del objeto que sera devuelto por la transaccion del webservice
//        SoapObject soapTransaction = null;
//
//        try {
//
//            //Transaccion solicitada al web service
//            soapTransaction = new KsoapAsync(new KsoapAsync.ResponseKsoapAsync() {
//
//                /**
//                 * Metodo sobrecargado que maneja el callback de los datos
//                 *
//                 * @param soapResponse
//                 * @return
//                 */
//                @Override
//                public SoapObject processFinish(SoapObject soapResponse) {
//                    return soapResponse;
//                }
//
//            }).execute(transactionWS).get();
//
//        } catch (InterruptedException | ExecutionException e) {
//
//            e.printStackTrace();
//
//        }
//
//        //Si la transaccion no genero resultado regresa un establecimiento vacio
//        if (soapTransaction != null) {
//
//            //Inicializacion del modelo MessageWS
//            MessageWS messageWS = new MessageWS((SoapObject) soapTransaction.getProperty(0));
//
//            switch (messageWS.getCodigoMensaje()) {
//
//                //Informacion encontrada
//                case MessageWS.statusTransaccionExitosa:
//                case MessageWS.statusTerminalExiste:
//                case MessageWS.statusConsultaExitosa:
//
//
//                    //Inicializacion del modelo InformacionEstablecimiento
//                    InformacionEstablecimiento informacionEstablecimiento = new InformacionEstablecimiento((SoapObject) soapTransaction.getProperty(1));
//
//                    //Inicializacion del modelo ConexionEstablecimiento
//                    ConexionEstablecimiento conexionEstablecimiento = new ConexionEstablecimiento((SoapObject) soapTransaction.getProperty(1));
//
//                    //Inicializacion del modelo establecimiento
//                    establecimiento = new Establecimiento(
//                            informacionEstablecimiento,
//                            conexionEstablecimiento,
//                            messageWS
//                    );
//                    break;
//
//
//                //Terminal no existe
//                case MessageWS.statusTerminalNoExiste:
//                    //Inicializacion del modelo establecimiento
//                    establecimiento = new Establecimiento(
//                            messageWS
//                    );
//                    break;
//
//                //Error general en la transaccion
//                case MessageWS.statusTerminalErrorException:
//                    //Inicializacion del modelo establecimiento
//                    establecimiento = new Establecimiento(
//                            messageWS
//                    );
//                    break;
//            }
//
//        }
//        //Retorno del establecimiento
//        return establecimiento;
    }

    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage, Establecimiento establecimiento) {
        RegisterConfigurationScreenEvent registerConfigurationScreenEvent = new RegisterConfigurationScreenEvent();
        registerConfigurationScreenEvent.setEventType(type);
        if (errorMessage != null) {
            registerConfigurationScreenEvent.setErrorMessage(errorMessage);
        }

        if (establecimiento != null) {
            registerConfigurationScreenEvent.setEstablecimiento(establecimiento);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(registerConfigurationScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null,null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, String errorMessage) {

        postEvent(type, errorMessage,null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, Establecimiento establecimiento) {

        postEvent(type, null,establecimiento);

    }
}
