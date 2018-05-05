package com.cofrem.transacciones.lib;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cofrem.transacciones.global.InfoGlobalTransaccionREST;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.HashMap;

public class VolleyTransaction {


    private static final String TAG_ERROR_JSON = "Error Json: ";
    private static final String TAG_ERROR_VOLLEY = "Error Volley: ";

    /**
     * Metodo que hace la Transaccion de datos por el Metodo POST
     *
     * @param context
     * @param parameters
     * @param URLTransaccion
     * @param callback
     */
    public void getData(Context context,
                        HashMap<String, String> parameters,
                        String URLTransaccion,
                        final VolleyCallback callback) {

        // Añadir petición GSON a la cola
        VolleySingleton.getInstance(context).addToRequestQueue(

                //Llamado al constructor de la clase GsonRequest
                new GsonRequest<JsonObject>(

                        //@param int method
                        Request.Method.POST,

                        //@param PARAM_URL
                         URLTransaccion,

                        //@param Class<T> clazz Clase o modelo en el que se formatean los datos
                        JsonObject.class,

                        //@param headers
                        parameters,

                        //@param Listener
                        new Response.Listener<JsonObject>() {
                            @Override
                            public void onResponse(JsonObject response) {

                                try {
                                    JsonObject jsonObject = response.getAsJsonObject("resultado");
                                    callback.onSuccess(jsonObject);

                                } catch (JsonIOException e) {

                                    e.printStackTrace();
                                    Log.e(TAG_ERROR_JSON, e.toString());

                                } catch (JsonParseException e) {

                                    e.printStackTrace();
                                    Log.e(TAG_ERROR_JSON, e.toString());

                                }
                            }
                        },
                        //@param errorListener
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                callback.onError("Error: no fue posible establecer comunicación con el servidor");

                            }
                        }
                )
        );
    }

    /**
     * Metodo que hace la Transaccion de datos por el Metodo GET
     *
     * @param context
     * @param moduleTransaccion
     * @param callback
     */
    public void getData(Context context,
                        String moduleTransaccion,
                        String URLTransaccion,
                        final VolleyCallback callback) {

        // Añadir petición GSON a la cola
        VolleySingleton.getInstance(context).addToRequestQueue(

                //Llamado al constructor de la clase GsonRequest
                new GsonRequest<JsonObject>(

                        //@param int method
                        Request.Method.GET,

                        //@param PARAM_URL
                        URLTransaccion,

                        //@param Class<T> clazz Clase o modelo en el que se formatean los datos
                        JsonObject.class,

                        //@param headers
                        null,

                        //@param Listener
                        new Response.Listener<JsonObject>() {
                            @Override
                            public void onResponse(JsonObject response) {

                                try {

                                    JsonObject jsonObject = response.getAsJsonObject(InfoGlobalTransaccionREST.FEED_KEY);
                                    callback.onSuccess(jsonObject);


                                } catch (JsonIOException e) {

                                    e.printStackTrace();
                                    Log.e(TAG_ERROR_JSON, e.toString());

                                } catch (JsonParseException e) {

                                    e.printStackTrace();
                                    Log.e(TAG_ERROR_JSON, e.toString());

                                }
                            }
                        },
                        //@param errorListener
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d(TAG_ERROR_VOLLEY, error.getMessage());
                                callback.onError(error.getMessage());

                            }
                        }
                )
        );
    }

    /**
     * Interface de Callback de InfoGlobalTransaccionREST Volley
     */
    public interface VolleyCallback {
        /**
         * Retorno en exito
         *
         * @param data
         */
        void onSuccess(JsonObject data);

        /**
         * Retorno en fallo
         *
         * @param errorMessage
         */
        void onError(String errorMessage);
    }

}
