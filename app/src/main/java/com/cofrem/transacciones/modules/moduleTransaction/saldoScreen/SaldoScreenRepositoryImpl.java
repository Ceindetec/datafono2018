package com.cofrem.transacciones.modules.moduleTransaction.saldoScreen;

import android.content.Context;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.database.AppDatabase;
import com.cofrem.transacciones.global.InfoGlobalSettingsPrint;
import com.cofrem.transacciones.global.InfoGlobalTransaccionREST;
import com.cofrem.transacciones.global.InfoGlobalTransaccionSOAP;
import com.cofrem.transacciones.lib.KsoapAsync;
import com.cofrem.transacciones.lib.PrinterHandler;
import com.cofrem.transacciones.lib.StyleConfig;
import com.cofrem.transacciones.lib.VolleyTransaction;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.models.PrintRow;
import com.cofrem.transacciones.models.Servicio;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.ApiWS;
import com.cofrem.transacciones.models.modelsWS.MessageWS;
import com.cofrem.transacciones.models.modelsWS.TransactionWS;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionSaldo;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionTransaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.ResultadoTransaccion;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.events.SaldoScreenEvent;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SaldoScreenRepositoryImpl implements SaldoScreenRepository {

    ResultadoTransaccion resultadoTransaccionRecibo;


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
    public SaldoScreenRepositoryImpl() {
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
    public void registrarTransaccion(Context context, Transaccion transaccion) {


        final HashMap<String, String> parameters = new HashMap<>();

        final String codigo = AppDatabase.getInstance(context).obtenerCodigoTerminal();

        parameters.put("codigo", codigo);
        parameters.put("numero_tarjeta", transaccion.getNumero_tarjeta());
        parameters.put("password", transaccion.getClave() + "");


        VolleyTransaction volleyTransaction = new VolleyTransaction();


        volleyTransaction.getData(context,
                parameters,
                InfoGlobalTransaccionREST.HTTP +
                        AppDatabase.getInstance(context).obtenerURLConfiguracionConexion() +
                        InfoGlobalTransaccionREST.WEB_SERVICE_URI +
                        InfoGlobalTransaccionREST.METHODO_SALDO
                ,
                new VolleyTransaction.VolleyCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {

                        if (data.get("estado").getAsBoolean()) {

                            ArrayList<Servicio> listServicios = new ArrayList<Servicio>();

                            JsonArray servicios = data.get("Saldos").getAsJsonArray();
                            int sizeArray = servicios.size();
                            for (int i = 0; i < sizeArray; i++) {
                                JsonObject js3 = (JsonObject) servicios.get(i);
                                Servicio servicio = new Servicio();

                                servicio.setCodigo(js3.get("codigo_servicio").getAsString());
                                servicio.setDescripcion(toTextCase(js3.get("servicio").getAsString().toLowerCase()));
                                servicio.setValor("$" + js3.get("saldo").getAsString());

                                listServicios.add(servicio);
                            }
//                            listServicios.add(new Servicio("B","Bono Empresarial",null));
                            postEvent(SaldoScreenEvent.onTransaccionSuccess, listServicios);
                        } else {

                            postEvent(SaldoScreenEvent.onTransaccionError, data.get("mensaje").getAsString());

                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        postEvent(SaldoScreenEvent.onTransaccionWSConexionError);
                    }

                });


    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo encargado de Pasar de minusculas a mayuscula la primera letra de cada palabra
     *
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
     * Metodo que imprime el recibo de la transaccion
     *
     * @param context
     */
    public void imprimirRecibo(Context context) {

        ConfigurationPrinter configurationPrinter = AppDatabase.getInstance(context).getConfigurationPrinter();

        int gray = configurationPrinter.getGray_level();

        // creamos el ArrayList se que encarga de almacenar los rows del recibo
        ArrayList<PrintRow> printRows = new ArrayList<PrintRow>();

        //Se agrega el logo al primer renglon del recibo y se coloca en el centro
        printRows.add(PrintRow.printLogo(context, gray));

        PrintRow.printCofrem(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_title_consulta_saldo), new StyleConfig(StyleConfig.Align.CENTER, gray, 20)));


        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_operador), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1)));
        PrintRow.printOperador(context, printRows, gray, 10);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_documento), resultadoTransaccionRecibo.getInformacionSaldo().getCedulaUsuario(), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_numero_tarjeta), PrinterHandler.getFormatNumTarjeta(resultadoTransaccionRecibo.getInformacionSaldo().getNumeroTarjeta()), new StyleConfig(StyleConfig.Align.LEFT, gray, 20)));

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 10)));

        int saldo = Integer.parseInt(resultadoTransaccionRecibo.getInformacionSaldo().getValor().split(".0")[0]);

        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_total), PrintRow.numberFormat(saldo), new StyleConfig(StyleConfig.Align.LEFT, gray)));
        printRows.add(new PrintRow(context.getResources().getString(
                R.string.recibo_separador_linea), new StyleConfig(StyleConfig.Align.LEFT, gray, StyleConfig.FontSize.F1, 50)));

        printRows.add(new PrintRow(".", new StyleConfig(StyleConfig.Align.LEFT, 1)));


        int status = new PrinterHandler().imprimerTexto(printRows);

        if (status == InfoGlobalSettingsPrint.PRINTER_OK) {
            postEvent(SaldoScreenEvent.onImprecionReciboSuccess);
        } else {
            postEvent(SaldoScreenEvent.onImprecionReciboError, PrinterHandler.stringErrorPrinter(status, context));
        }

    }

    /**
     * Metodo que registra los eventos
     *
     * @param type
     * @param errorMessage
     */
    private void postEvent(int type, String errorMessage, InformacionSaldo informacionSaldo, ArrayList<Servicio> listServicios) {
        SaldoScreenEvent saldoScreenEvent = new SaldoScreenEvent();
        saldoScreenEvent.setEventType(type);
        if (errorMessage != null) {
            saldoScreenEvent.setErrorMessage(errorMessage);
        }

        if (informacionSaldo != null) {
            saldoScreenEvent.setInformacionSaldo(informacionSaldo);
        }

        if (listServicios != null) {
            saldoScreenEvent.setListServicios(listServicios);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();

        eventBus.post(saldoScreenEvent);
    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type) {

        postEvent(type, null, null, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, String errorMessage) {

        postEvent(type, errorMessage, null, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, InformacionSaldo informacionSaldo) {

        postEvent(type, null, informacionSaldo, null);

    }

    /**
     * Sobrecarga del metodo postevent
     *
     * @param type
     */
    private void postEvent(int type, ArrayList<Servicio> listServicios) {

        postEvent(type, null, null, listServicios);

    }
}
