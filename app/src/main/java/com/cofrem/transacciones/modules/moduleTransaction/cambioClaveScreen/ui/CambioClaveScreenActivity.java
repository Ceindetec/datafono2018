package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.TransactionScreenActivity_;
import com.cofrem.transacciones.global.InfoGlobalSettings;
import com.cofrem.transacciones.lib.KeyBoard;
import com.cofrem.transacciones.lib.MagneticHandler;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.models.Tarjeta;
import com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.CambioClaveScreenPresenter;
import com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.CambioClaveScreenPresenterImpl;
import com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.ui.CreditoScreenActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

import static android.view.KeyEvent.KEYCODE_ENTER;

@EActivity(R.layout.activity_transaction_cambio_clave_screen)
public class CambioClaveScreenActivity extends Activity implements CambioClaveScreenView {

    /**
     #############################################################################################
     Declaracion de componentes y variables
     #############################################################################################
     */

    /**
     * Declaracion de los Contoles
     */

    //Controles del header
    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;


    //Contenedores

    @ViewById
    RelativeLayout bodyContentCambioClaveNumeroDocumento;
    @ViewById
    RelativeLayout bodyContentCambioClaveDesliceTarjeta;
    @ViewById
    RelativeLayout bodyContentCambioClavePassActual;
    @ViewById
    RelativeLayout bodyContentCambioClaveDesliceTarjetaIncorrecta;
    @ViewById
    RelativeLayout bodyContentCambioClavePassNueva;
    @ViewById
    RelativeLayout bodyContentCambioClaveErrorTarjeta;
    @ViewById
    RelativeLayout bodyContentCambioClaveSuccessTarjeta;

    @ViewById
    FrameLayout frlPgbHldTransactionCambioClave;


    @ViewById
    EditText edtCambioClaveTransactionNumeroDocumentoValor;
    @ViewById
    EditText edtCambioClaveTransactionClaveUsuarioContenidoClave;
    @ViewById
    EditText edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave;
    @ViewById
    EditText edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave;

    @ViewById
    TextView txvCambioClaveTransactionDesliceTarjetaNumeroDocumento;
    @ViewById
    TextView txvCambioClaveTransactionErrorMensaje;


    Tarjeta infoTarjeta = new Tarjeta();

    int pasoCambioClave;

    final static int PASO_DOCUMENTO = 0;
    final static int PASO_DESLIZAR_TARJETA = 1;
    final static int PASO_CLAVE_ACTUAL = 2;
    final static int PASO_CLAVE_NUEVA = 3;
    final static int PASO_CLAVE_REPETIR_NUEVA = 4;


    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface SaldoScreenPresenter
    private CambioClaveScreenPresenter cambioClaveScreenPresenter;


    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void MainInit() {

        /**
         * Instanciamiento e inicializacion del presentador
         */
        cambioClaveScreenPresenter = new CambioClaveScreenPresenterImpl(this);

        /**
         * Llamada al metodo onCreate del presentador para el registro del bus de datos
         */
        cambioClaveScreenPresenter.onCreate();

        inicializarOcultamientoVistas();

        pasoCambioClave = PASO_DOCUMENTO;

        //Coloca la informacion del encabezado
        setInfoHeader();

        /**
         * metodo verificar acceso
         */
        //TODO: crear metodos
        cambioClaveScreenPresenter.VerifySuccess(this);

        bodyContentCambioClaveNumeroDocumento.setVisibility(View.VISIBLE);

    }

    /**
     * #############################################################################################
     * Metodos sobrecargados del sistema
     * #############################################################################################
     */

    /**
     * Metodo sobrecargado de la vista para la destruccion de la activity
     */
    @Override
    public void onDestroy() {
        cambioClaveScreenPresenter.onDestroy();
        super.onDestroy();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KEYCODE_ENTER) {

            switch (pasoCambioClave) {
                case PASO_DOCUMENTO:
                    ingresarDocumentoAceptar();
                    break;
                case PASO_DESLIZAR_TARJETA:
                    break;
                case PASO_CLAVE_ACTUAL:
                    break;
                case PASO_CLAVE_NUEVA:
                    break;
                case PASO_CLAVE_REPETIR_NUEVA:
                    break;

            }
        } else {

        }


        return super.onKeyUp(keyCode, event);
    }

    /**
     * Metodo que interfiere la presion del boton "Back"
     */
    @Override
    public void onBackPressed() {

    }

    @Override
    public void handleValidarPassActualSuccess() {
        hideProgress();

        bodyContentCambioClavePassActual.setVisibility(View.GONE);

        bodyContentCambioClavePassNueva.setVisibility(View.VISIBLE);

        pasoCambioClave = PASO_CLAVE_NUEVA;

    }

    @Override
    public void handleValidarPassActualError(String error) {
        hideProgress();

        //Muestra el mensaje de error de formato de la contraseña
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void handleValidarPassActualizarSuccess() {
        hideProgress();

        bodyContentCambioClavePassNueva.setVisibility(View.GONE);

        bodyContentCambioClaveSuccessTarjeta.setVisibility(View.VISIBLE);



    }

    @Override
    public void handleValidarPassActualizarError(String error) {
        hideProgress();

        //Muestra el mensaje de error de formato de la contraseña
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handlePasswordDedeSerNum(String errorMessage) {
        hideProgress();

        //Muestra el mensaje de error de formato de la contraseña
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleDocumentoIncorrecto(String error) {
        hideProgress();

        //Actualiza el paso actual
        pasoCambioClave = PASO_DOCUMENTO;

        bodyContentCambioClavePassActual.setVisibility(View.GONE);

        bodyContentCambioClaveNumeroDocumento.setVisibility(View.VISIBLE);

        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void handleTarjetaInactiva(String error) {
        hideProgress();

        txvCambioClaveTransactionErrorMensaje.setText(error);

        bodyContentCambioClavePassActual.setVisibility(View.GONE);

        bodyContentCambioClaveErrorTarjeta.setVisibility(View.VISIBLE);

    }

    @Override
    public void handleTarjetaNoValida(String error) {
        hideProgress();

        txvCambioClaveTransactionErrorMensaje.setText(error);

        bodyContentCambioClavePassActual.setVisibility(View.GONE);

        bodyContentCambioClaveErrorTarjeta.setVisibility(View.VISIBLE);

    }

    @Override
    public void handleTransaccionConError(String error) {
        hideProgress();

        //Muestra el mensaje de error de formato de la contraseña
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Metodo para manejar la verificacion exitosa
     */
    public void handleVerifySuccess() {

    }

        /*
      #############################################################################################
      Metodo propios de la clase
      #############################################################################################
     */

    /**
     * Metodo para mostrar la barra de progreso
     */
    private void showProgress() {
        //TODO: VERIFICAR QUE ESTA MOSTRANDO LA BARRA DE PROGRESO
        //Muestra la barra  de progreso
        frlPgbHldTransactionCambioClave.setVisibility(View.VISIBLE);
        frlPgbHldTransactionCambioClave.bringToFront();
        frlPgbHldTransactionCambioClave.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        //Oculta la barra de progreso
        frlPgbHldTransactionCambioClave.setVisibility(View.GONE);
    }

    /**
     * Metodo que llena el header de la App
     */
    private void setInfoHeader() {

        txvHeaderIdDispositivo.setText(
                String.format(
                        getString(R.string.header_text_id_dispositivo_registrado)
                        , InfoHeaderApp.getInstance().getIdDispositivo()
                )
        );

        txvHeaderIdPunto.setText(
                String.format(
                        getString(R.string.header_text_id_punto_registrado)
                        , InfoHeaderApp.getInstance().getIdPunto()
                )
        );

        txvHeaderEstablecimiento.setText(
                String.format(
                        getString(R.string.header_text_nombre_establecimiento_registrado)
                        , InfoHeaderApp.getInstance().getNombreEstablecimiento()
                )
        );

        txvHeaderPunto.setText(
                String.format(
                        getString(R.string.header_text_nombre_punto_registrado)
                        , InfoHeaderApp.getInstance().getNombrePunto()
                )
        );

    }

    /**
     * Metodo que oculta por defecto los include de la vista
     */
    private void inicializarOcultamientoVistas() {
        bodyContentCambioClaveNumeroDocumento.setVisibility(View.GONE);
        bodyContentCambioClaveDesliceTarjeta.setVisibility(View.GONE);
        bodyContentCambioClaveDesliceTarjetaIncorrecta.setVisibility(View.GONE);
        bodyContentCambioClavePassActual.setVisibility(View.GONE);
        bodyContentCambioClavePassNueva.setVisibility(View.GONE);
        bodyContentCambioClaveErrorTarjeta.setVisibility(View.GONE);
        bodyContentCambioClaveSuccessTarjeta.setVisibility(View.GONE);
    }

    private void iniciarColorEditex(){
        edtCambioClaveTransactionNumeroDocumentoValor.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT));
        edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT));
        edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT));
        edtCambioClaveTransactionClaveUsuarioContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT));
    }

    @LongClick({R.id.edtCambioClaveTransactionNumeroDocumentoValor,
            R.id.edtCambioClaveTransactionClaveUsuarioContenidoClave,
            R.id.edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave,
            R.id.edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave,})
    @Click({R.id.edtCambioClaveTransactionNumeroDocumentoValor,
            R.id.edtCambioClaveTransactionClaveUsuarioContenidoClave,
            R.id.edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave,
            R.id.edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave,})
    @Touch({R.id.edtCambioClaveTransactionNumeroDocumentoValor,
            R.id.edtCambioClaveTransactionClaveUsuarioContenidoClave,
            R.id.edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave,
            R.id.edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave,})
    public void hideKeyBoard(View v) {
        //Oculta el teclado
        KeyBoard.hide(this);

        iniciarColorEditex();

        switch(v.getId()){
            case R.id.edtCambioClaveTransactionNumeroDocumentoValor:
                edtCambioClaveTransactionNumeroDocumentoValor.requestFocus();
                edtCambioClaveTransactionNumeroDocumentoValor.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT_COFUS));
                break;
            case R.id.edtCambioClaveTransactionClaveUsuarioContenidoClave:
                edtCambioClaveTransactionClaveUsuarioContenidoClave.requestFocus();
                edtCambioClaveTransactionClaveUsuarioContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT_COFUS));
                break;
            case R.id.edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave:
                edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave.requestFocus();
                edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT_COFUS));
                break;
            case R.id.edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave:
                edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave.requestFocus();
                edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT_COFUS));
                break;
        }

    }

    /**
     * Metodo para mostrar la orden de deslizar la tarjeta
     */
    public void deslizarTarjeta() {

        //Obtiene la lectura de la banda magnetica
        String[] magneticHandler = new MagneticHandler().readMagnetic();

        //Determina si la lectura fue correcta
        if (magneticHandler != null) {

            //Limpia el formato de la lectura
            String numeroTarjeta = magneticHandler[1]
                    .replace(";", "")
                    .replace("!", "")
                    .replace("#", "")
                    .replace("$", "")
                    .replace("&", "")
                    .replace("/", "")
                    .replace("|", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("=", "")
                    .replace("?", "")
                    .replace("¿", "")
                    .replace("¿", "")
                    .replace("¡", "")
                    .replace("*", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", "")
                    .replace(".", "")
                    .replace("-", "")
                    .replace("_", "")
                    .replace("%", "");

            //Registra el valor del numero de tarjeta en el modelo de la tarjeta
            infoTarjeta.setNumTarjeta(numeroTarjeta);

            //En caso de la lectura correcta se continua el proceso
            lecturaTarjetaCorrecta();

        } else {

            //En caso de la lectura erronea se muestra la pantalla de error
            lecturaTarjetaErronea();

        }

    }

    /**
     * Metodo para mostrar la lectura correcta de tarjeta
     */
    private void lecturaTarjetaCorrecta() {

        bodyContentCambioClaveDesliceTarjeta.setVisibility(View.GONE);

        bodyContentCambioClavePassActual.setVisibility(View.VISIBLE);
//        bodyContentCambioClavePassNueva.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoCambioClave = PASO_CLAVE_ACTUAL;

    }

    /**
     * Metodo para mostrar la lectura erronea de tarjeta
     */
    private void lecturaTarjetaErronea() {

        bodyContentCambioClaveDesliceTarjeta.setVisibility(View.GONE);

        bodyContentCambioClaveDesliceTarjetaIncorrecta.setVisibility(View.VISIBLE);

    }

    @Click(R.id.btnCambioClaveTransactionLecturaIncorrectaBotonSalir)
    public void salirDeLecturaIncorrecta() {

        bodyContentCambioClaveDesliceTarjetaIncorrecta.setVisibility(View.GONE);

        bodyContentCambioClaveNumeroDocumento.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoCambioClave = PASO_DOCUMENTO;
    }

    /**
     * boton Aceptar contenedor de numero documento
     */
    @Click(R.id.btnCambioClaveTransactionNumeroDocumentoBotonAceptar)
    public void ingresarDocumentoAceptar() {

        String numeroDocumento = edtCambioClaveTransactionNumeroDocumentoValor.getText().toString();


        if (numeroDocumento.length() > 0) {
            infoTarjeta.setDocumento(numeroDocumento);

            txvCambioClaveTransactionDesliceTarjetaNumeroDocumento.setText(numeroDocumento);

            bodyContentCambioClaveNumeroDocumento.setVisibility(View.GONE);

            bodyContentCambioClaveDesliceTarjeta.setVisibility(View.VISIBLE);

            //Actualiza el paso actual
            pasoCambioClave = PASO_DESLIZAR_TARJETA;

            edtCambioClaveTransactionNumeroDocumentoValor.setText("");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    deslizarTarjeta();
                }
            }, 100);

        } else {
            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_numero_documento, Toast.LENGTH_SHORT).show();

        }
    }

    @Click(R.id.btnCambioClaveTransactionClaveUsuarioBotonAceptar)
    public void validarPassActual(){

        String password = edtCambioClaveTransactionClaveUsuarioContenidoClave.getText().toString();


        //La contraseña debe ser de exactamente 4 caracteres
        if (password.length() == 4) {

            infoTarjeta.setActual(password);

            showProgress();

            edtCambioClaveTransactionClaveUsuarioContenidoClave.setText("");

            //Valida la contraseña
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cambioClaveScreenPresenter.validarPassActual(CambioClaveScreenActivity.this, infoTarjeta);
                }
            }, 100);


        }else{
            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_clave_administrador, Toast.LENGTH_SHORT).show();

        }
    }

    String nuevoPassword;
    @Click(R.id.btnCambioClaveTransactionClaveNuevaUsuarioBotonAceptar)
    public void validarPassNuevo(){

        nuevoPassword = edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave.getText().toString();
        String RepetirPassword = edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave.getText().toString();

        if(nuevoPassword.length() == 4){
            if(nuevoPassword.equals(RepetirPassword)){
                showProgress();

                edtCambioClaveTransactionClaveNuevaUsuarioContenidoClave.setText("");

                edtCambioClaveTransactionClaveNuevaRepetirUsuarioContenidoClave.setText("");

                //TODO: aqui tengo que enviar para actualizar
                //Valida la contraseña
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cambioClaveScreenPresenter.validarPassNuevo(CambioClaveScreenActivity.this, infoTarjeta,nuevoPassword);
                    }
                }, 100);

            }else{
                //Muestra el mensaje de error de formato de la contraseña
                Toast.makeText(this, R.string.transaction_error_clave_no_coincide, Toast.LENGTH_SHORT).show();

            }
        }else{
            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.transaction_error_format_clave_administrador, Toast.LENGTH_SHORT).show();

        }

    }



    @Click({R.id.btnCambioClaveTransactionNumeroDocumentoBotonCancelar,
            R.id.btnCambioClaveTransactionClaveUsuarioBotonCancelar,
            R.id.btnCambioClaveTransactionClaveNuevaUsuarioBotonCancelar,
            R.id.btnCambioClaveTransactionErrorTarjetaBotonSalir,
            R.id.btnCambioClaveTransactionSuccessTarjetaBotonSalir})
    public void RegresarALaVistaTransaccion() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(CambioClaveScreenActivity.this, TransactionScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);


    }


}
