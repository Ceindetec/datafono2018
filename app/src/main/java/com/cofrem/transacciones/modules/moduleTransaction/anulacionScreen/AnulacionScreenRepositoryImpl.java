package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen;

import android.content.Context;
import android.util.Log;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.global.InfoGlobalTransaccionREST;
import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.lib.MD5;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.lib.StyleConfig;
import com.cofrem.transacciones.lib.VolleyTransaction;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.ApiWS;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.ResultadoTransaccion;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.events.AnulacionScreenEvent;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.google.gson.JsonObject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class AnulacionScreenRepositoryImpl implements AnulacionScreenRepository {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    Transaccion modelTransaccion;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public AnulacionScreenRepositoryImpl() {
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Metodo quevalida la existencia de la clave de administracion
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordAdministrador(Context context, String passAdmin) {

        final HashMap<String, String> parameters = new HashMap<>();

        final String codigo = AppDatabase.getInstance(context).obtenerCodigoTerminal();

        parameters.put("codigo", codigo);
        parameters.put("password", passAdmin);

        VolleyTransaction volleyTransaction = new VolleyTransaction();

        //Consulta la clave del administrador para compararla con la ingresada en la vista
        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_CLAVE_SUCURSAL
                ,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if (data.get("estado").getAsBoolean()) {

                            // Registra el evento de que la clave es correcta
                            postEvent(AnulacionScreenEvent.onClaveAdministracionValida);
                        } else {
                            // Registra el evento de que la clave es Incorrecta
                            postEvent(AnulacionScreenEvent.onClaveAdministracionNoValida);

                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(AnulacionScreenEvent.onTransaccionError,errorMessage);
                    }

                });

    }

    /**
     * @param context
     * @param numeroCargo
     */
    @Override
    public void obtenerValorTransaccion(Context context, String numeroCargo) {

        Log.i("Repositorio cargo", numeroCargo);

        int valorTransaccion = AppDatabase.getInstance(context).obtenerValorTransaccion(numeroCargo);

        switch (valorTransaccion) {
            case AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO:
                postEvent(AnulacionScreenEvent.onNumeroCargoNoRelacionado);
                break;
            default:
                postEvent(AnulacionScreenEvent.onNumeroCargoRelacionado, valorTransaccion);
                break;
        }
    }

    /**
     * @param context
     * @param transaccion
     */
    @Override
    public void registrarTransaccion(Context context, Transaccion transaccion) {


        final HashMap<String, String> parameters = new HashMap<>();

        final String codigo = AppDatabase.getInstance(context).obtenerCodigoTerminal();

        parameters.put("codigo", codigo);
        parameters.put("numero_tarjeta", transaccion.getNumero_tarjeta());
        parameters.put("numero_transaccion", transaccion.getNumero_transaccion());
        parameters.put("identificacion", transaccion.getNumero_documento());
        parameters.put("password", transaccion.getClave() + "");

        VolleyTransaction volleyTransaction = new VolleyTransaction();

        //Consulta la clave del administrador para compararla con la ingresada en la vista
        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_ANULACION
                ,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if (data.get("estado").getAsBoolean()) {

                            InformacionTransaccion informacionTransaccion = new InformacionTransaccion(data);

                            postEvent(AnulacionScreenEvent.onTransaccionAnulacionSuccess, informacionTransaccion);

                        } else {
                            postEvent(AnulacionScreenEvent.onTransaccionAnulacionError, data.get("mensaje").getAsString());
                        }

                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(AnulacionScreenEvent.onClaveAdministracionError);
                    }

                });


    }


    @Override
    public void anularSuccess(Context context, InformacionTransaccion informacionTransaccion) {

        //Registro en la base de datos de la transaccion
        if (registrarTransaccionConsumoDB(context, informacionTransaccion)) {

            postEvent(AnulacionScreenEvent.onTransaccionSuccess);

            //Imprime el recibo
            imprimirRecibo(context, context.getResources().getString(
                    R.string.recibo_copia_comercio));

        } else {

            //Error en el registro en la Base de Datos la transaccion
            postEvent(AnulacionScreenEvent.onTransaccionDBRegisterError);

        }
    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

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
        if (AppDatabase.getInstance(context).insertRegistroTransaction(informacionTransaccion, Transaccion.TIPO_TRANSACCION_ANULACION)) {
            statusTransaction = true;
        }

        return statusTransaction;
    }


    /**
     * Metodo que imprime el recibo de la transaccion
     *
     * @param context
     * @param stringCopia
     */
    @Override
    public void imprimirRecibo(Context context, String stringCopia) {

        ConfigurationPrinter configurationPrinter = AppDatabase.getInstance(context).getConfigurationPrinter();

        int gray = configurationPrinter.getGray_level();

        Transaccion modelTransaccionAnulada = AppDatabase.getInstance(context).obtenerUltimaTransaccionAnulada();

//        String fecha_transaccion = AppDatabase.getInstance(context).obtenerFechaTransaccionNumCargo(modelTransaccion.getNumero_cargo());

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        PrintRow.printCofrem(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_anulacion), new StyleConfig(StyleConfig.Align.CENTER, gray, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_operador), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
        PrintRow.printOperador(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_transaccion), modelTransaccion.getNumero_transaccion(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
//        printRows.add(new PrintRow(context.getResources().getString(
//                R.string.recibo_fecha),fecha_transaccion, new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_fecha_anulacion), modelTransaccionAnulada.getFullFechaServer(), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));

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
                R.string.recibo_valor_anulado), PrintRow.numberFormat(modelTransaccionAnulada.getValor()), new StyleConfig(StyleConfig.Align.LEFT, gray)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_entidad), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_vigilado), new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 20)));
        printRows.add(new PrintRow(stringCopia, new StyleConfig(StyleConfig.Align.CENTER, gray, StyleConfig.FontSize.F3, 60)));


        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));

        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            int i = 0;
        } else {
            int i = 1;
        }


    }

    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage, int valorInt, InformacionTransaccion informacionTransaccion) {

        AnulacionScreenEvent anulacionScreenEvent = new AnulacionScreenEvent();

        anulacionScreenEvent.setEventType(type);

        if (errorMessage != null) {
            anulacionScreenEvent.setErrorMessage(errorMessage);
        }

        if (valorInt != AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO) {
            anulacionScreenEvent.setValorInt(valorInt);
        }

        if (informacionTransaccion != null) {
            anulacionScreenEvent.setInformacionTransaccion(informacionTransaccion);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(anulacionScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null, AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, int valorInt) {

        postEvent(type, null, valorInt, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, String errorMessage) {

        postEvent(type, errorMessage, AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO, null);

    }

    private void postEvent(int type, InformacionTransaccion informacionTransaccion) {

        postEvent(type, null, AnulacionScreenEvent.VALOR_TRANSACCION_NO_VALIDO, informacionTransaccion);

    }
}
