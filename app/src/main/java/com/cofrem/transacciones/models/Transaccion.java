package com.cofrem.transacciones.models;

public class Transaccion {


    /**
     * Valores posibles que puede tomar el atributo tipo transaccion dentro del sistema
     */
    public final static int TIPO_TRANSACCION_CONSUMO = 1;
    public final static int TIPO_TRANSACCION_ANULACION = 2;

    /**
     * ## TABLA ENTREGADA POR COFREM ########
     * ## Version 2017/07/27 ################
     * Valores posibles que puede tomar el atributo tipo encriptacion
     * 0	SIN ENCRIPTAR
     * 1	ALGORITMO COFREM
     * 2	MD5
     * 3	DOBLE MD5
     * 4	SHA1
     * 5	DOBLE SHA1
     * 6	SHA2
     * 7	DOBLE SHA2
     */
    public final static int CODIGO_ENCR_NO_ENCRIPTADO = 0;
    public final static int CODIGO_ENCR_ALGOR_COFREM = 1;
    public final static int CODIGO_ENCR_MD5 = 2;
    public final static int CODIGO_ENCR_DOBLE_MD5 = 3;
    public final static int CODIGO_ENCR_SHA1 = 4;
    public final static int CODIGO_ENCR_DOBLE_SHA1 = 5;
    public final static int CODIGO_ENCR_SHA2 = 6;
    public final static int CODIGO_ENCR_DOBLE_SHA2 = 7;
    /**
     * ## TABLA ENTREGADA POR COFREM ########
     * ## Version 2017/07/27 ################
     * Valores posibles que puede tomar el atributo tipo_transaccion
     * 1	CONSUMO O COMPRA
     * 2	AVANCE O RETIRO
     */
    public final static int CODIGO_TIPO_TRANSACCION_CONSUMO = 1;
    public final static int CODIGO_TIPO_TRANSACCION_AVANCE = 2;

    /**
     * ## TABLA ENTREGADA POR COFREM ########
     * ## Version 2017/07/27 ################
     * Valores posibles que puede tomar el atributo servicios
     * 01	CUPO ROTATIVO
     * 02	BONO DE BIENESTAR
     * 03	TARJETA REGALO
     * 04	BONO ALIMENTARIO FOSFEC
     * 05	CUOTA MONETARIA FOSFEC
     * 06	CUOTA MONETARIA
     */
    public final static int CODIGO_PRODUCTO_CUPO_ROTATIVO = 1;
    public final static int CODIGO_PRODUCTO_BONO_BIENESTAR = 2;
    public final static int CODIGO_PRODUCTO_TARJETA_REGALO = 3;
    public final static int CODIGO_PRODUCTO_BONO_ALIMENTO_FOSFEC = 4;
    public final static int CODIGO_PRODUCTO_CUOTA_MONTETARIA_FOSFEC = 5;
    public final static int CODIGO_PRODUCTO_CUOTA_MONETARIA = 6;


    /**
     * Modelo para el registro del Establishment
     */
    private int id;
    private String numero_transaccion;

    private String numero_documento;
    private int clave;
    private String numero_tarjeta;

    //Array de servicios
    private String servicios;
    private int valor;

    private int tipo_encriptacion;
    private int tipo_transaccion;

    private String nombre_usuario;
    private String fecha_server;
    private String hora_server;


    private String registro;
    private int estado;

    /**
     * Constructor vacio de la clase
     */
    public Transaccion() {
        servicios = "";
    }

    public Transaccion(
            int id,
            String numero_transaccion,
            String numero_documento,
            int clave,
            String numero_tarjeta,
            String servicios,
            int valor,
            int tipo_encriptacion,
            int tipo_transaccion,
            String registro,
            int estado) {
        this.id = id;
        this.numero_transaccion = numero_transaccion;
        this.numero_documento = numero_documento;
        this.clave = clave;
        this.numero_tarjeta = numero_tarjeta;
        this.servicios = servicios;
        this.valor = valor;
        this.tipo_encriptacion = tipo_encriptacion;
        this.tipo_transaccion = tipo_transaccion;
        this.registro = registro;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero_transaccion() {
        return numero_transaccion;
    }

    public void setNumero_transaccion(String numero_transaccion) {
        this.numero_transaccion = numero_transaccion;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public void addServicio(String servicios) {
        if(this.servicios.equals(""))
            this.servicios = this.servicios + servicios;
        else
            this.servicios = this.servicios + "," + servicios;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getTipo_encriptacion() {
        return tipo_encriptacion;
    }

    public void setTipo_encriptacion(int tipo_encriptacion) {
        this.tipo_encriptacion = tipo_encriptacion;
    }

    public int getTipo_transaccion() {
        return tipo_transaccion;
    }

    public void setTipo_transaccion(int tipo_transaccion) {
        this.tipo_transaccion = tipo_transaccion;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFecha_server() {
        return fecha_server;
    }

    public void setFecha_server(String fecha_server) {
        this.fecha_server = fecha_server;
    }

    public String getHora_server() {
        return hora_server;
    }

    public void setHora_server(String hora_server) {
        this.hora_server = hora_server;
    }

    public String getFullFechaServer() {
        return fecha_server + " " + hora_server;
    }
}
