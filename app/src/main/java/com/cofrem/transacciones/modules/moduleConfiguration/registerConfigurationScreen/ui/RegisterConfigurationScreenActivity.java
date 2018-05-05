package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.ConfigurationScreenActivity_;
import com.cofrem.transacciones.global.InfoGlobalSettings;
import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.models.modelsWS.modelEstablecimiento.Establecimiento;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.RegisterConfigurationScreenPresenter;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.RegisterConfigurationScreenPresenterImpl;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.splashScreen.ui.SplashScreenActivity_;
import com.cofrem.transacciones.lib.KeyBoard;
import com.cofrem.transacciones.models.Configurations;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import static android.view.KeyEvent.KEYCODE_ENTER;

@EActivity(R.layout.activity_configuration_register_screen)
public class RegisterConfigurationScreenActivity extends Activity implements RegisterConfigurationScreenView {

    /*
      #############################################################################################
      Declaracion de componentes y variables
      #############################################################################################
     */

    //Almacena el valor de la pantalla anterior
    int valorRetorno;

    /**
     * Declaracion de los Contoles
     */

    //Elementos del header
    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;

    //Controles del modulo
    @ViewById
    RelativeLayout bodyContentConfiguration;
    @ViewById
    RelativeLayout bodyContentConfigurationInfoTerminal;
    @ViewById
    RelativeLayout bodyContentConfigurationPassTecnico;
    @ViewById
    RelativeLayout bodyContentConfigurationHost;
    @ViewById
    RelativeLayout bodyContentConfigurationDispositivo;
    @ViewById
    RelativeLayout bodyContentConfigurationExito;
    @ViewById
    FrameLayout frlPgbHldRegisterScreen;

    //Paso configuracion_register_paso_pass_tecnico
    @ViewById
    Button btnConfiguracionRegisterPassTecnicoBotonCancelar;
    @ViewById
    EditText edtConfiguracionRegisterPassTecnicoContenidoClave;

    //Paso configuracion_register_paso_pass_host
    @ViewById
    Button btnConfiguracionRegisterBotonCancelar;
    @ViewById
    EditText edtConfiguracionRegisterHostContenidoValor;

    //Paso configuracion_register_paso_pass_dispositivo
    @ViewById
    Button btnConfiguracionRegisterDispositivoBotonCancelar;
    @ViewById
    EditText edtConfiguracionRegisterDispositivoContenidoValor;


    @ViewById
    TextView txvConfiguracionRegisterEstableciminetoContenido;
    @ViewById
    TextView txvConfiguracionRegisterSucursalContenido;
    @ViewById
    TextView txvConfiguracionRegisterCodigoTerminalContenido;

    //Model que almacena la configuracion del dispositivo
    Configurations modelConfiguration = new Configurations();

    private Establecimiento establecimiento;

    String passwordAdmin = "";

    //Pasos definidos
    int pasoRegisterConfiguration = 0; //Define el paso actual

    final static int PASO_PASS_TECNICO_LOCAL = 0;
    final static int PASO_PASS_TECNICO_WEB = 1;
    final static int PASO_HOST = 2;
//    final static int PASO_PORT = 3;
    final static int PASO_DISPOSITIVO = 3;

    //Creación del filtro para el ingreso de IP
    InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   android.text.Spanned dest,
                                   int dstart,
                                   int dend) {
            if (end > start) {
                String destTxt = dest.toString();
                String resultingTxt = destTxt.substring(0, dstart)
                        + source.subSequence(start, end)
                        + destTxt.substring(dend);
                if (!resultingTxt
                        .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                    return "";
                } else {
                    String[] splits = resultingTxt.split("\\.");
                    for (int i = 0; i < splits.length; i++) {
                        if (Integer.valueOf(splits[i]) > 255) {
                            return "";
                        }
                    }
                }
            }
            return null;
        }

    };

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface SaldoScreenPresenter
    private RegisterConfigurationScreenPresenter registerConfigurationScreenPresenter;


    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void ConfigurationInit() {

        //Instanciamiento e inicializacion del presentador
        registerConfigurationScreenPresenter = new RegisterConfigurationScreenPresenterImpl(this);

        //Llamada al metodo onCreate del presentador para el registro del bus de datos
        registerConfigurationScreenPresenter.onCreate();

        //Metodo que oculta por defecto los include de la vista
        inicializarOcultamientoVistas();

        // Metodo para colocar la orientacion de la app
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Coloca la informacion del encabezado
        setInfoHeader();

        //Recibe los parametros del Bundle
        Bundle args = getIntent().getExtras();

        //Inicializa el paso del registro de la configuracion
        pasoRegisterConfiguration = args.getInt(Configurations.keyPasoClaveTecnico);

        //Primera ventana visible
        bodyContentConfigurationPassTecnico.setVisibility(View.VISIBLE);

        valorRetorno = args.getInt(Configurations.keyConfiguration);

        if (valorRetorno == Configurations.configuracionRegistrarConfigInicial) {
            ocultarBotonesBack();
        }

        //Seteando el valor del filtro en el EditText
        edtConfiguracionRegisterHostContenidoValor.setFilters(new InputFilter[]{inputFilter});

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
        registerConfigurationScreenPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Metodo que intercepta las pulsaciones de las teclas del teclado fisico
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * Keycodes disponibles
         *
         * 4: Back
         * 66: Enter
         * 67: Delete
         *
         */
        switch (keyCode) {

            case KEYCODE_ENTER:

                //Ocula el soft keyboard al presionar la tecla enter
//                hideKeyBoard();

                switch (pasoRegisterConfiguration) {

                    case PASO_PASS_TECNICO_LOCAL:
                    case PASO_PASS_TECNICO_WEB:
                        //Metodo para validar la contraseña
                        validarPasswordTecnico();
                        break;

                    case PASO_HOST:
                        //Metodo para registrar el host de conexion
                        registrarHost();
                        break;

                    case PASO_DISPOSITIVO:
                        //Metodo para registrar el identificador del dispositivo
                        registrarDispositivo();
                        break;
                }
                break;

            default:
                Log.i("Key Pressed", String.valueOf(keyCode));
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Metodo que interfiere la presion del boton "Back"
     */
    @Override
    public void onBackPressed() {

        switch (pasoRegisterConfiguration) {

            case PASO_PASS_TECNICO_LOCAL:
            case PASO_PASS_TECNICO_WEB:
                //Vacia la caja de contraseña
                edtConfiguracionRegisterPassTecnicoContenidoClave.setText("");
                break;

            case PASO_HOST:
                //Vacia la caja del host de conexion
                edtConfiguracionRegisterHostContenidoValor.setText("");
                break;

            case PASO_DISPOSITIVO:
                //Vacia la caja del codigo de identificacion del dispositivo
                edtConfiguracionRegisterHostContenidoValor.setText("");

                break;

        }
    }

    /**
     * Metodo que interfiere en la presion del boton Task
     */
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    /**
     * Metodo sobrecargado de la vista para la presion de las teclas de volumen
     *
     * @param event evento de la presion de una tecla
     * @return regresa el rechazo de la presion
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (InfoGlobalSettingsBlockButtons.blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Metodo para manejar el valor de acceso valido
     */
    @Override
    public void handleClaveTecnicaValida() {

        //Oculta la barra de progreso
        hideProgress();

        //Oculta la vista de la contraseña de administracion tecnica
        bodyContentConfigurationPassTecnico.setVisibility(View.GONE);

        //Muestra la vista del Host de conexion
        bodyContentConfiguration.setVisibility(View.VISIBLE);

        //Actualiza el paso actual
        pasoRegisterConfiguration = PASO_HOST;
    }

    /**
     * Metodo para manejar el valor de acceso NO valido
     */
    @Override
    public void handleClaveTecnicaNoValida() {

        //Oculta la barra de progreso
        hideProgress();

        //Vacia la caja de contraseña de administracion tecnica
        edtConfiguracionRegisterPassTecnicoContenidoClave.setText("");

        //Muestra el mensaje de error en la contraseña de administracion tecnica
        Toast.makeText(this, R.string.configuration_text_clave_no_valido, Toast.LENGTH_SHORT).show();

    }

    /**
     * Metodo para manejar el error en la configuracion de valor de acceso
     */
    @Override
    public void handleClaveTecnicaError() {

        //Oculta la barra de progreso
        hideProgress();

        //Vacia la caja de contraseña de administracion tecnica
        edtConfiguracionRegisterPassTecnicoContenidoClave.setText("");

        //Muestra el mensaje de error en la configuracion de la contraseña de administracion tecnica
        Toast.makeText(this, R.string.configuration_text_clave_error, Toast.LENGTH_SHORT).show();

    }

    /**
     * Metodo para manejar el registro de configuracion de conexion correcto
     */
    @Override
    public void handleRegistroConfigConexionSuccess() {

        //Muestra el mensaje de informacion del registro de conexion correcto
        Toast.makeText(this, R.string.configuration_text_registro_conexion_success, Toast.LENGTH_SHORT).show();

    }

    /**
     * Metodo para manejar el registro de configuracion de conexion incorrecto
     */
    @Override
    public void handleRegistroConfigConexionError() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de conexion incorrecto
        Toast.makeText(this, R.string.configuration_text_registro_conexion_error, Toast.LENGTH_SHORT).show();

    }

    /**
     * Metodo para manejar el registro de informacion del dispositivo correcto
     */
    @Override
    public void handleInformacionDispositivoSuccess() {

        //Muestra el mensaje de informacion del registro de informacion del dispositivo correcto
        Toast.makeText(this, R.string.configuration_text_informacion_dispositivo_success, Toast.LENGTH_SHORT).show();

    }

    /**
     * Metodo para manejar el registro de informacion del dispositivo incorrecto
     */
    @Override
    public void handleInformacionDispositivoErrorConexion() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de informacion del dispositivo incorrecto
        Toast.makeText(this, R.string.configuration_text_informacion_dispositivo_error_conexion, Toast.LENGTH_SHORT).show();

//        pasoRegisterConfiguration = PASO_HOST;

        //Oculta las vistas
//        inicializarOcultamientoVistas();

        //Muestra la vista del codigo de host
//        bodyContentConfiguration.setVisibility(View.VISIBLE);

    }

    /**
     * Metodo para manejar el registro de informacion del dispositivo incorrecto
     */
    @Override
    public void handleInformacionDispositivoErrorInformacion() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de informacion del dispositivo incorrecto
        Toast.makeText(this, R.string.configuration_text_informacion_dispositivo_error_informacion, Toast.LENGTH_SHORT).show();

        pasoRegisterConfiguration = PASO_DISPOSITIVO;

        //Oculta las vistas
        inicializarOcultamientoVistas();

        //Muestra la vista del codigo de dispositivo
        bodyContentConfigurationDispositivo.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo para manejar el registro de la informacion del dispositivo correcto
     */
    @Override
    public void handleProccessInformacionEstablecimientoSuccess() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de informacion del dispositivo incorrecto
        Toast.makeText(this, R.string.configuration_text_procesar_informacion_dispositivo_success, Toast.LENGTH_SHORT).show();

        switch (valorRetorno) {

            case Configurations.configuracionRegistrarConfigInicial:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(RegisterConfigurationScreenActivity.this, SplashScreenActivity_.class);

                        //Agregadas banderas para no retorno
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                    }
                }, 100);

                break;

            case Configurations.configuracionRegistrar:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(RegisterConfigurationScreenActivity.this, ConfigurationScreenActivity_.class);

                        //Agregadas banderas para no retorno
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                    }
                }, 100);

                break;

        }
    }

    /**
     * Metodo para manejar el registro de la informacion del dispositivo erronea
     */
    @Override
    public void handleProccessInformacionEstablecimientoError() {

        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de informacion del dispositivo incorrecto
        Toast.makeText(this, R.string.configuration_text_procesar_informacion_dispositivo_error, Toast.LENGTH_SHORT).show();

        pasoRegisterConfiguration = PASO_HOST;

        //Oculta las vistas
        inicializarOcultamientoVistas();

        //Muestra la vista del codigo de host
        bodyContentConfigurationHost.setVisibility(View.VISIBLE);

    }

    @Override
    public void handleVerifyTerminalSuccess(Establecimiento establecimiento) {

        //Oculta la barra de progreso
        hideProgress();

        this.establecimiento = establecimiento;

        bodyContentConfiguration.setVisibility(View.GONE);

        txvConfiguracionRegisterEstableciminetoContenido.setText(establecimiento.getInformacionEstablecimiento().getRazonSocialComercio());

        txvConfiguracionRegisterSucursalContenido.setText(establecimiento.getInformacionEstablecimiento().getNombrePunto());

        int codigo = Integer.parseInt(establecimiento.getConexionEstablecimiento().getCodigoTerminal());

        txvConfiguracionRegisterCodigoTerminalContenido.setText(String.valueOf(codigo));

        bodyContentConfigurationInfoTerminal.setVisibility(View.VISIBLE);


    }

    @Override
    public void handleVerifyTerminalError(String MessageError) {
        //Oculta la barra de progreso
        hideProgress();

        //Muestra el mensaje de error del registro de informacion del dispositivo incorrecto
        Toast.makeText(this,MessageError, Toast.LENGTH_SHORT).show();

    }

    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para mostrar la barra de progreso
     */
    private void showProgress() {
        //TODO: VERIFICAR QUE ESTA MOSTRANDO LA BARRA DE PROGRESO
        //Muestra la barra  de progreso
        frlPgbHldRegisterScreen.setVisibility(View.VISIBLE);
        frlPgbHldRegisterScreen.bringToFront();
        frlPgbHldRegisterScreen.invalidate();
    }

    /**
     * Metodo para ocultar la barra de progreso
     */
    private void hideProgress() {
        //Oculta la barra de progreso
        frlPgbHldRegisterScreen.setVisibility(View.GONE);
    }

    /**
     * Metodo que oculta por defecto los include de la vista
     */
    private void inicializarOcultamientoVistas() {

        bodyContentConfigurationHost.setVisibility(View.GONE);
        bodyContentConfigurationDispositivo.setVisibility(View.GONE);
        bodyContentConfigurationExito.setVisibility(View.GONE);
        bodyContentConfiguration.setVisibility(View.GONE);
        bodyContentConfigurationInfoTerminal.setVisibility(View.GONE);

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
     * Metodo que oculta por defecto los botones back
     */
    private void ocultarBotonesBack() {
        btnConfiguracionRegisterPassTecnicoBotonCancelar.setVisibility(View.GONE);
        btnConfiguracionRegisterBotonCancelar.setVisibility(View.GONE);
        btnConfiguracionRegisterDispositivoBotonCancelar.setVisibility(View.GONE);
    }

    private void iniciarColorEditex(){
        edtConfiguracionRegisterPassTecnicoContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT));
        edtConfiguracionRegisterHostContenidoValor.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT));
        edtConfiguracionRegisterDispositivoContenidoValor.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT));
    }

    /**
     * Metodo que oculta el teclado al presionar el EditText
     */
    @LongClick({R.id.edtConfiguracionRegisterPassTecnicoContenidoClave,
            R.id.edtConfiguracionRegisterHostContenidoValor,
            R.id.edtConfiguracionRegisterDispositivoContenidoValor,
    })
    @Click({R.id.edtConfiguracionRegisterPassTecnicoContenidoClave,
            R.id.edtConfiguracionRegisterHostContenidoValor,
            R.id.edtConfiguracionRegisterDispositivoContenidoValor,
    })
    @Touch({R.id.edtConfiguracionRegisterPassTecnicoContenidoClave,
            R.id.edtConfiguracionRegisterHostContenidoValor,
            R.id.edtConfiguracionRegisterDispositivoContenidoValor,
    })
    public void hideKeyBoard(View v) {

        //TODO:VERIFICAR QUE EL TECLADO SE ESTA OCULTANDO
        //Oculta el teclado
        KeyBoard.hide(this);

        iniciarColorEditex();

        switch(v.getId()) {
            case R.id.edtConfiguracionRegisterPassTecnicoContenidoClave:
                edtConfiguracionRegisterPassTecnicoContenidoClave.requestFocus();
                edtConfiguracionRegisterPassTecnicoContenidoClave.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT_COFUS));
                break;
            case  R.id.edtConfiguracionRegisterHostContenidoValor:
                edtConfiguracionRegisterHostContenidoValor.requestFocus();
                edtConfiguracionRegisterHostContenidoValor.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT_COFUS));
                break;
            case  R.id.edtConfiguracionRegisterDispositivoContenidoValor:
                edtConfiguracionRegisterDispositivoContenidoValor.requestFocus();
                edtConfiguracionRegisterDispositivoContenidoValor.setBackgroundColor(Color.parseColor(InfoGlobalSettings.COLOR_EDITEXT_COFUS));
                break;
        }

    }

    /**
     * Metodo para regresar a la ventana de configuracion
     */
    @Click({R.id.btnConfiguracionRegisterPassTecnicoBotonCancelar,
            R.id.btnConfiguracionRegisterBotonCancelar,
            R.id.btnConfiguracionRegisterPortBotonCancelar,
            R.id.btnConfiguracionRegisterDispositivoBotonCancelar
    })
    public void navigateToConfigurationScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(RegisterConfigurationScreenActivity.this, ConfigurationScreenActivity_.class);
                //Agregadas banderas para no retorno
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 100);
    }

    /**
     * Metodo que envia la contraseña ingresada para su validacion
     */
    @Click(R.id.btnConfiguracionRegisterPassTecnicoBotonAceptar)
    public void validarPasswordTecnico() {

        //Se obtiene el texto de la contraseña
        passwordAdmin = edtConfiguracionRegisterPassTecnicoContenidoClave.getText().toString();

        if (passwordAdmin.length() == 4) {

            //Muestra la barra de progreso
            showProgress();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(pasoRegisterConfiguration == PASO_PASS_TECNICO_LOCAL){
                        registerConfigurationScreenPresenter.validarPasswordTecnicoLocal(RegisterConfigurationScreenActivity.this, passwordAdmin);
                    }else{
                        registerConfigurationScreenPresenter.validarPasswordTecnicoWeb(RegisterConfigurationScreenActivity.this, passwordAdmin);
                    }
                }
            }, 100);

        } else {

            //Vacia la caja de contraseña
            edtConfiguracionRegisterPassTecnicoContenidoClave.setText("");

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.configuration_error_format_clave_tecnica, Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Metodo que registra la configuracion del host
     */
    @Click(R.id.btnConfiguracionRegisterBotonAceptar)
    public void registrarHost() {

        //Se obtiene el texto del Host
        String host = edtConfiguracionRegisterHostContenidoValor.getText().toString();

        //Se obtiene el texto del codigo del dispositivo
        String codigoDispositivo = edtConfiguracionRegisterDispositivoContenidoValor.getText().toString();

        //Vacia la caja de el contenido del host
//        edtConfiguracionRegisterHostContenidoValor.setText("");

        if (host.length() > 6 && host.length() < 16) {
            //Registra el valor del host en el modelo de la configuracion
            modelConfiguration.setHost(host);

//            //Actualiza el paso actual
//            pasoRegisterConfiguration = PASO_DISPOSITIVO;

            if (codigoDispositivo.length() >= 1 &&codigoDispositivo.length() < 15) {

                //Registra el valor del codigo de dispositivo en el modelo de la configuracion
                modelConfiguration.setCodigoDispositivo(codigoDispositivo);

                showProgress();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        registerConfigurationScreenPresenter.validarTerminal(RegisterConfigurationScreenActivity.this, modelConfiguration);
                    }
                }, 100);

                //Actualiza el paso actual
//                pasoRegisterConfiguration++;

            } else {

                //Muestra el mensaje de error de formato de la contraseña
                Toast.makeText(this, R.string.configuration_error_format_codigo_establecimiento, Toast.LENGTH_SHORT).show();

            }

        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.configuration_error_format_host, Toast.LENGTH_SHORT).show();

        }

    }

    @Click(R.id.btnConfiguracionRegisterInfoTerminalBotonCancelar)
    public void rechazarInformacionTerminal(){

        bodyContentConfigurationInfoTerminal.setVisibility(View.GONE);

        bodyContentConfiguration.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btnConfiguracionRegisterInfoTerminalBotonAceptar)
    public void aceptarInformacionTerminal(){

        showProgress();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                registerConfigurationScreenPresenter.setAsignaID(RegisterConfigurationScreenActivity.this,establecimiento );
            }
        }, 100);

    }

    /**
     * Metodo que registra la configuracion del dispositivo
     */
    @Click(R.id.btnConfiguracionRegisterDispositivoBotonAceptar)
    public void registrarDispositivo() {

        //Se obtiene el texto del codigo del dispositivo
        String codigoDispositivo = edtConfiguracionRegisterDispositivoContenidoValor.getText().toString();

        //Vacia la caja del valor del codigo del dispositivo
        edtConfiguracionRegisterDispositivoContenidoValor.setText("");

        if (codigoDispositivo.length() < 15) {

            //Registra el valor del codigo de dispositivo en el modelo de la configuracion
            modelConfiguration.setCodigoDispositivo(codigoDispositivo);

            showProgress();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    registerConfigurationScreenPresenter.validarTerminal(RegisterConfigurationScreenActivity.this, modelConfiguration);
                }
            }, 100);

            //Actualiza el paso actual
            pasoRegisterConfiguration++;

        } else {

            //Muestra el mensaje de error de formato de la contraseña
            Toast.makeText(this, R.string.configuration_error_format_codigo_establecimiento, Toast.LENGTH_SHORT).show();

        }


    }
}
