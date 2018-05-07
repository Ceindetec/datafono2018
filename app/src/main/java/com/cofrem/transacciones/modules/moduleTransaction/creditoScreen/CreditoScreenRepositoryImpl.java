package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen;

import android.content.Context;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.global.InfoGlobalTransaccionREST;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.lib.StyleConfig;
import com.cofrem.transacciones.lib.VolleyTransaction;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Servicio;
import com.cofrem.transacciones.models.modelsWS.ApiWS;
import com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.events.CambioClaveScreenEvent;
import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.events.CreditoScreenEvent;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.ResultadoTransaccion;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.models.Transaccion;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class CreditoScreenRepositoryImpl implements CreditoScreenRepository {

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
    public CreditoScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */


    /**
     * Medtodo encangaro de consultar los servicios disponibles para una tarjeta
     * @param context
     * @param transaccion
     */
    @Override
    public void consultarServicios(Context context, Transaccion transaccion) {
        final HashMap<String, String> parameters = new HashMap<>();

        final String codigo = AppDatabase.getInstance(context).obtenerCodigoTerminal();

        parameters.put("codigo", codigo);
        parameters.put("numero_tarjeta", transaccion.getNumero_tarjeta());
        parameters.put("identificacion", transaccion.getNumero_documento());


        VolleyTransaction volleyTransaction = new VolleyTransaction();


        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_GET_SERVICIOS
                ,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if(data.get("estado").getAsBoolean()){

                            ArrayList<Servicio> listServicios = new ArrayList<Servicio>();

                            JsonArray servicios = data.get("servicios").getAsJsonArray();
                            int sizeArray = servicios.size();

                            for(int i = 0; i<sizeArray;i++){
                                JsonObject js3 = (JsonObject) servicios.get(i);
                                Servicio servicio = new Servicio();

                                servicio.setCodigo(js3.get("codigo_servicio").getAsString());
                                servicio.setDescripcion(toTextCase(js3.get("descripcion").getAsString().toLowerCase()));
                                servicio.setValor("$"+js3.get("saldo").getAsString());

                                listServicios.add(servicio);
                            }
//                            listServicios.add(new Servicio("B","Bono Empresarial",null));
                            postEvent(CreditoScreenEvent.onConsultarServiciosSuccess,listServicios);
                        }else{

                            switch (data.get("codigo").getAsInt()){

                                case ApiWS.CODIGO_DOCUMENTIO_INCORRECTO:
                                    postEvent(CreditoScreenEvent.onDocumentoIncorrecto,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_CAMBIO_CLAVE:
                                    postEvent(CreditoScreenEvent.onCambioDeClaveObligatorio,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_INACTIVA:
                                    postEvent(CreditoScreenEvent.onTarjetaInactiva,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_NO_VALIDA:
                                    postEvent(CreditoScreenEvent.onTarjetaNoValida,data.get("mensaje").getAsString());
                                    break;
                                default:
                                    postEvent(CreditoScreenEvent.onTransaccionConError,data.get("mensaje").getAsString());
                            }

                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(CreditoScreenEvent.onConsultarServiciosError,errorMessage);
                    }

                });
    }


    @Override
    public void consumir(Context context, Transaccion transaccion) {

        final HashMap<String, String> parameters = new HashMap<>();

        final String codigo = AppDatabase.getInstance(context).obtenerCodigoTerminal();

        parameters.put("codigo", codigo);
        parameters.put("numero_tarjeta", transaccion.getNumero_tarjeta());
        parameters.put("valor", String.valueOf(transaccion.getValor()));
        parameters.put("servicios", transaccion.getServicios());
        parameters.put("password", String.valueOf(transaccion.getClave()));


        VolleyTransaction volleyTransaction = new VolleyTransaction();


        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_CONSUMO
                ,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if(data.get("estado").getAsBoolean()){

                            InformacionTransaccion informacionTransaccion = new InformacionTransaccion(data);

                            postEvent(CreditoScreenEvent.onCosumirServiciosSuccess,informacionTransaccion);

                        }else{

                            switch (data.get("codigo").getAsInt()){

                                case ApiWS.CODIGO_TERMINAL_INACTIVA:
                                    postEvent(CreditoScreenEvent.onTerminalInactiva,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TRANSACCION_INSUFICIENTE:
                                    postEvent(CreditoScreenEvent.onTransaccionConError,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_INACTIVA:
                                    postEvent(CreditoScreenEvent.onTarjetaInactiva,data.get("mensaje").getAsString());
                                    break;
                                case ApiWS.CODIGO_TARJETA_NO_VALIDA:
                                    postEvent(CreditoScreenEvent.onTarjetaNoValida,data.get("mensaje").getAsString());
                                    break;
                                default:
                                    postEvent(CreditoScreenEvent.onCosumirServiciosError,data.get("mensaje").getAsString());
                            }

                            //TODO: en este punto es necesario ver cuando los fondos son insuficientes para
                            // volver a pedir el otro servicio

                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(CreditoScreenEvent.onTransaccionConError,errorMessage);
                    }

                });



    }


    @Override
    public void cosumirSuccess(Context context, InformacionTransaccion informacionTransaccion) {

            //Registro en la base de datos de la transaccion
            if (registrarTransaccionConsumoDB(context, informacionTransaccion)) {

                postEvent(CreditoScreenEvent.onTransaccionSuccess);

                //Imprime el recibo
                imprimirRecibo(context,context.getResources().getString(
                        R.string.recibo_copia_comercio));

            } else {

                //Error en el registro en la Base de Datos la transaccion
                postEvent(CreditoScreenEvent.onTransaccionDBRegisterError);

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

//        ResultadoTransaccion resultadoTransaccion = registrarTransaccionConsumoWS(context, transaccion);

        //Registra mediante el WS la transaccion
//        if (resultadoTransaccion != null) {
//
//            MessageWS messageWS = resultadoTransaccion.getMessageWS();
//
//            if (messageWS.getCodigoMensaje() == MessageWS.statusTransaccionExitosa) {
//
//                //Registro en la base de datos de la transaccion
//                if (registrarTransaccionConsumoDB(context, resultadoTransaccion.getInformacionTransaccion())) {
//
//                    postEvent(CreditoScreenEvent.onTransaccionSuccess);
//
//                    //Imprime el recibo
//                    imprimirRecibo(context,context.getResources().getString(
//                            R.string.recibo_copia_comercio));
//
//                } else {
//
//                    //Error en el registro en la Base de Datos la transaccion
//                    postEvent(CreditoScreenEvent.onTransaccionDBRegisterError);
//
//                }
//            } else {
//                //Error en el registro de la transaccion del web service
//                postEvent(CreditoScreenEvent.onTransaccionWSRegisterError, messageWS.getDetalleMensaje());
//            }
//        } else {
//            //Error en la conexion con el Web Service
//            postEvent(CreditoScreenEvent.onTransaccionWSConexionError);
//        }

    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo que:
     * - registra mediante el WS la transaccion
     * - Extrae el estado de la transaccion
     *
     * @param context     contexto desde la cual se realiza la transaccion
     * @param transaccion datos de la transaccion
     * @return regreso del resultado de la transaccion
     */
//    private ResultadoTransaccion registrarTransaccionConsumoWS(Context context, Transaccion transaccion) {
//
//        //Se crea una variable de estado de la transaccion
//        ResultadoTransaccion resultadoTransaccion = null;
//
//        //Inicializacion y declaracion de parametros para la peticion web service
//        String[][] params = {
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_CODIGO_TERMINAL, AppDatabase.getInstance(context).obtenerCodigoTerminal()},
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_TIPO_TRANSACCION, String.valueOf(transaccion.getTipo_transaccion())},
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_CEDULA_USUARIO, transaccion.getNumero_documento()},
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_NUMERO_TARJETA, transaccion.getNumero_tarjeta()},
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_CLAVE_USUARIO, String.valueOf(transaccion.getClave())},
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_TIPO_ENCRIPTACION, String.valueOf(transaccion.getTipo_encriptacion())},
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_TIPO_SERVICIO, String.valueOf(transaccion.getTipo_servicio())},
//                {InfoGlobalTransaccionSOAP.PARAM_NAME_TRANSACCION_VALOR_SOLICITADO, String.valueOf(transaccion.getValor())},
//        };
//
//        //Creacion del modelo TransactionWS para ser usado dentro del webservice
//        TransactionWS transactionWS = new TransactionWS(
//                InfoGlobalTransaccionSOAP.HTTP + AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() + InfoGlobalTransaccionSOAP.WEB_SERVICE_URI,
//                InfoGlobalTransaccionSOAP.HTTP + InfoGlobalTransaccionSOAP.NAME_SPACE,
//                InfoGlobalTransaccionSOAP.METHOD_NAME_TRANSACCION,
//                params);
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
//            MessageWS messageWS = new MessageWS(
//                    (SoapObject) soapTransaction.getProperty(MessageWS.PROPERTY_MESSAGE)
//            );
//
//            switch (messageWS.getCodigoMensaje()) {
//
//                //Transaccion exitosa
//                case MessageWS.statusTransaccionExitosa:
//
//                    InformacionTransaccion informacionTransaccion = new InformacionTransaccion(
//                            (SoapObject) soapTransaction.getProperty(InformacionTransaccion.PROPERTY_TRANSAC_RESULT)
//                    );
//
//                    resultadoTransaccion = new ResultadoTransaccion(
//                            informacionTransaccion,
//                            messageWS
//                    );
//                    break;
//
//                default:
//                    resultadoTransaccion = new ResultadoTransaccion(
//                            messageWS
//                    );
//                    break;
//            }
//
//        }
//
//        //Retorno de estado de transaccion
//        return resultadoTransaccion;
//    }

    /**
     * Metodo que registra en la base de datos interna la transaccion
     *
     * @param context
     * @return
     */
    private boolean registrarTransaccionConsumoDB(Context context, InformacionTransaccion informacionTransaccion) {

        //Se crea una variable de estado de la transaccion
        boolean statusTransaction = false;

        //Se registra la transaccion
        if (AppDatabase.getInstance(context).insertRegistroTransaction(informacionTransaccion, Transaccion.CODIGO_TIPO_TRANSACCION_CONSUMO)) {
            statusTransaction = true;
        }

        return statusTransaction;
    }

    /**
     * Metodo que imprime el recibo de la transaccion
     *
     * @param context
     */
    public void imprimirRecibo(Context context, String stringCopia) {

        ConfigurationPrinter configurationPrinter = AppDatabase.getInstance(context).getConfigurationPrinter();

        int gray = configurationPrinter.getGray_level();

        ArrayList<Transaccion> modelsTransaccion = AppDatabase.getInstance(context).obtenerUltimaTransaccion();

        Transaccion modelTransaccion = modelsTransaccion.get(0);

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        PrintRow.printCofrem(context, printRows, gray, 10);

        //se siguen agregando cado auno de los String a los renglones (Rows) del recibo para imprimir

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_operador), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
        PrintRow.printOperador(context, printRows, gray, 10);


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_transaccion), modelTransaccion.getNumero_transaccion(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_fecha), modelTransaccion.getRegistro(), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_afiliado), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_documento), modelTransaccion.getNumero_documento(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(modelTransaccion.getNombre_usuario(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_tarjeta), PrinterHandler.getFormatNumTarjeta(modelTransaccion.getNumero_tarjeta()), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_detalle), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_valor), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_total), PrintRow.numberFormat(modelTransaccion.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 30)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_mensaje_final), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 30)));


        PrintRow.printFirma(context, printRows, gray);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_entidad), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_vigilado), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 20)));
        printRows.add(new PrintRow(stringCopia, new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 60)));


        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));

        //retornamos el estado de la impresora tras enviar los rows para imprimir

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(CreditoScreenEvent.onImprecionReciboSuccess);
        } else {
            postEvent(CreditoScreenEvent.onImprecionReciboError, PrinterHandler.stringErrorPrinter(status, context));
        }

    }


    /**
     * Metodo encargado de Pasar de minusculas a mayuscula la primera letra de cada palabra
     * @param givenString
     * @return
     */
    public static String toTextCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }


    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage,ArrayList<Servicio> listServicios,InformacionTransaccion informacionTransaccion) {
        CreditoScreenEvent creditoScreenEvent = new CreditoScreenEvent();
        creditoScreenEvent.setEventType(type);
        if (errorMessage != null) {
            creditoScreenEvent.setErrorMessage(errorMessage);
        }
        if (listServicios != null) {
            creditoScreenEvent.setListServicios(listServicios);
        }
        if (informacionTransaccion != null) {
            creditoScreenEvent.setInformacionTransaccion(informacionTransaccion);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(creditoScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null, null, null);

    }
    private void postEvent(int type,String error) {

        postEvent(type, error, null, null);

    }
    private void postEvent(int type,ArrayList<Servicio> listServicios) {

        postEvent(type, null, listServicios, null);

    }
    private void postEvent(int type,InformacionTransaccion informacionTransaccion) {

        postEvent(type, null,null, informacionTransaccion);

    }
}


