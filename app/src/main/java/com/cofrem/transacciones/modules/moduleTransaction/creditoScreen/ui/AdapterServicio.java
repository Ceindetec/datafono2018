package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cofrem.transacciones.R;
import com.cofrem.transacciones.models.Servicio;

import java.util.ArrayList;

/**
 * Created by luisp on 17/10/2017.
 */

public class AdapterServicio extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Servicio> items;

    public AdapterServicio(Activity activity, ArrayList<Servicio> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Servicio> servicios) {
        for (int i = 0; i < servicios.size(); i++) {
            items.add(servicios.get(i));
        }
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_servicio, null);
        }

        Servicio servicio = items.get(position);

        TextView texto = (TextView) v.findViewById(R.id.item_servicio_texto);
        texto.setText(servicio.getDescripcion());

        ImageView imagen = (ImageView) v.findViewById(R.id.item_servicio_icino);
        imagen.setImageDrawable(servicio.getImagen());

        return v;
    }
}
