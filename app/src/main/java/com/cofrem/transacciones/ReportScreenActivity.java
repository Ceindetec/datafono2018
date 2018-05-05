package com.cofrem.transacciones;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.cofrem.transacciones.global.InfoGlobalSettingsBlockButtons;
import com.cofrem.transacciones.models.InfoHeaderApp;
import com.cofrem.transacciones.models.Reports;
import com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.ui.ReimpresionScreenActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_report_screen)
public class ReportScreenActivity extends Activity {

    /**
     * #############################################################################################
     * Definición de variables
     * #############################################################################################
     */
    @ViewById
    TextView txvHeaderIdDispositivo;
    @ViewById
    TextView txvHeaderIdPunto;
    @ViewById
    TextView txvHeaderEstablecimiento;
    @ViewById
    TextView txvHeaderPunto;

    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    @AfterViews
    void ReportInit() {
        setInfoHeader();
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados del sistema
     * #############################################################################################
     */

    /**
     * Metodo que interfiere la presion del boton "Back"
     */
    @Override
    public void onBackPressed() {
        String mensajeRegresar = getString(R.string.general_message_press_back) + getString(R.string.general_text_button_regresar);
        Toast.makeText(this, mensajeRegresar, Toast.LENGTH_SHORT).show();
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
    }
    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */
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
     * Metodo para navegar a la ventana de reportes
     */
    @Click(R.id.btnReportScreenModuleReimpresion)
    public void navigateToReportsReimpresion() {

        Bundle args = new Bundle();

        args.putInt(Reports.keyReport, Reports.reportReimpresionRecibo);

        Intent intent = new Intent(this, ReimpresionScreenActivity_.class);

        intent.putExtras(args);

        startActivity(intent);
    }

    /**
     * Metodo para navegar a la ventana de reportes
     */
    @Click(R.id.btnReportScreenModuleDetalle)
    public void navigateToReportsDetalle() {

        Bundle args = new Bundle();

        args.putInt(Reports.keyReport, Reports.reportReporteDetalle);

        Intent intent = new Intent(this, ReimpresionScreenActivity_.class);

        intent.putExtras(args);

        startActivity(intent);
    }

    /**
     * Metodo para navegar a la ventana de reportes
     */
    @Click(R.id.btnReportScreenModuleGeneral)
    public void navigateToReportsGeneral() {

        Bundle args = new Bundle();

        args.putInt(Reports.keyReport, Reports.reportReporteGeneral);

        Intent intent = new Intent(this, ReimpresionScreenActivity_.class);

        intent.putExtras(args);

        startActivity(intent);
    }

    /**
     * Metodo para navegar a la ventana de reportes
     */
    @Click(R.id.btnReportScreenModuleCierreLote)
    public void navigateToCierreLote() {

        Bundle args = new Bundle();

        args.putInt(Reports.keyReport, Reports.reportCierreLote);

        Intent intent = new Intent(this, ReimpresionScreenActivity_.class);

        intent.putExtras(args);

        startActivity(intent);
    }

    /**
     * Metodo para navegar a la ventana principal
     */
    @Click(R.id.btnReportScreenBack)
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, MainScreenActivity_.class);
        startActivity(intent);
    }

}