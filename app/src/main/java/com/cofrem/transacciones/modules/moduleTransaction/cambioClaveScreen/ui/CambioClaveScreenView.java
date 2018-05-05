package com.cofrem.transacciones.modules.moduleTransaction.cambioClaveScreen.ui;

public interface CambioClaveScreenView {

    /**
     * Metodo para manejar la verificacion exitosa
     */
    void handleVerifySuccess();

    void handleValidarPassActualSuccess();

    void handleValidarPassActualError(String error);

    void handleValidarPassActualizarSuccess();

    void handleValidarPassActualizarError(String error);

    void handleTransaccionConError(String error);

    void handleDocumentoIncorrecto(String error);

    void handleTarjetaInactiva(String error);

    void handleTarjetaNoValida(String error);

    void handlePasswordDedeSerNum(String errorMessage);
}
