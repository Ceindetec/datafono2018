package com.cofrem.transacciones.modules.moduleTransaction.creditoScreen.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cofrem.transacciones.R;
import com.cofrem.transacciones.models.Servicio;

import java.util.List;

/**
 * RecyclerView.Adapter AdapterFuncionariaMensajes
 *
 * @author Christhian Hernando Torres - 2017
 * @version 1.0
 */
public class AdapterServicioNew extends RecyclerView.Adapter<AdapterServicioNew.ViewHolder> {

    private List<Servicio> items;

    private OnItemClickListener onItemClickListener;

    /**
     * #############################################################################################
     * Constructor  de  la clase
     * #############################################################################################
     */
    public AdapterServicioNew(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }


    /**
     * Escuchador de click
     */
    public interface OnItemClickListener {

        /**
         * Metodo para obtener el click del servicio seleccionado
         *
         * @param servicio
         * @param paramPosition
         */
        void onClickItemServicio(Servicio servicio, int paramPosition);

        /**
         * Metodo para obtener el click sostenido del servicio seleccionado
         *
         * @param servicio
         * @param paramPosition
         */
        void onLongClickItemServicio(Servicio servicio, int paramPosition);
    }

    /*
    #############################################################################################
    Metodo sobrecargados del sistema
    #############################################################################################
    */

    /**
     * Metodo sobre cargado del sistema que es llamado cuando se crea el Recycler View
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_servicio, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Metodo sobre cargado del sistema que es llamado cuando se enlaza la informacion con el Recycler View
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        Servicio servicio = items.get(position);

        viewHolder.item_servicio_texto.setText(servicio.getDescripcion());

        viewHolder.item_servicio_valor.setText(servicio.getValor());

        viewHolder.item_servicio_icino.setImageDrawable(servicio.getImagen());
//
//        viewHolder.item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewHolder.item.setBackgroundColor(context.getResources().getColor(R.color.appColorGraySecondary));
//            }
//        });


    }

    /**
     * Metodo sobre cargado del sistema que es llamado cuando se solicita el conteo de items
     *
     * @return
     */
    @Override
    public int getItemCount() {

        if (items != null)

            return items.size();

        return 0;
    }

    /**
     * @param servicioList
     */
    public void swapData(List<Servicio> servicioList) {

        if (servicioList != null) {

            items = servicioList;

        } else {

            items = null;

        }

        notifyDataSetChanged();
    }

    /**
     * Un {@link RecyclerView.ViewHolder} que gestiona la vista formato del Recycler View
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        /**
         * #############################################################################################
         * Definición de variables y controles
         * #############################################################################################
         */

        TextView item_servicio_texto;

        TextView item_servicio_valor;

        ImageView item_servicio_icino;

        RelativeLayout item;

        /**
         * @param view
         */
        ViewHolder(View view) {

            super(view);

            item_servicio_texto = (TextView) view.findViewById(R.id.item_servicio_texto);

            item_servicio_valor = (TextView) view.findViewById(R.id.item_servicio_valor);

            item_servicio_icino = (ImageView) view.findViewById(R.id.item_servicio_icino);

            item = (RelativeLayout) view.findViewById(R.id.item);

            view.setOnClickListener(this);

            view.setOnLongClickListener(this);


        }

        /**
         * @param view
         */
        @Override
        public void onClick(View view) {

            item.setBackgroundColor(view.getContext().getResources().getColor(R.color.appColorGrayTerciary));

            onItemClickListener.onClickItemServicio(
                    obtenerServicio(getAdapterPosition()),
                    getAdapterPosition()
            );

        }


        @Override
        public boolean onLongClick(View view) {
            item.setBackgroundColor(view.getContext().getResources().getColor(R.color.appColorWhite));

            onItemClickListener.onLongClickItemServicio(
                    obtenerServicio(getAdapterPosition()),
                    getAdapterPosition()
            );
            return true;
        }
    }

    /**
     * @param adapterPosition
     * @return
     */
    private Servicio obtenerServicio(int adapterPosition) {


        if (items != null) {

            Servicio servicio = items.get(adapterPosition);

            if (servicio != null) {

                return servicio;

            }

        }

        return null;

    }

}