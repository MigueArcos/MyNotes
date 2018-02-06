package com.example.miguel.misnotas.Clases_Lista;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miguel.misnotas.R;

import java.util.ArrayList;

/**
 * Created by Miguel on 20/06/2016.
 */
public class DeletedNotesAdapter extends RecyclerView.Adapter<DeletedNotesAdapter.ItemView> {
    private ArrayList<Elemento_Nota> data;
    private SparseBooleanArray expandedItems;
    private AdapterActions listener;

    public DeletedNotesAdapter(ArrayList<Elemento_Nota> data, AdapterActions listener) {
        this.data = data;
        this.listener = listener;
        expandedItems = new SparseBooleanArray();
    }

    public void notifyExpandedItemDeleted(int position) {
        expandedItems.delete(position);
    }

    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(ArrayList<Elemento_Nota> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TripItemTouchHelperCallback(this, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota_eliminada, parent, false);
        return new ItemView(v);
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.imagen.setImageResource(data.get(position).getID_Imagen());
        holder.titulo.setText(data.get(position).getTitulo());
        holder.fecha_modificacion.setText(String.format("Última modificación: %s", data.get(position).getFecha_modificacion()));
        holder.contenido.setText(data.get(position).getContenido());
        //Estas lineas if-else son necesarias porque cuando hacemos scroll en el recyclerview el viewholder se va reciclando, esto quiere decir que si un viewholder ya estaba expandido y este es reciclado pues va a seguir expandido, por eso se tiene que checar si la posicion de ese viewholder efectivamente es una posicion con el estatus de expandido
        if (expandedItems.get(position)) {
            holder.layout_contenido.setVisibility(View.VISIBLE);
            holder.flechita.setImageResource(R.drawable.chevron_up);
        } else {
            holder.layout_contenido.setVisibility(View.GONE);
            holder.flechita.setImageResource(R.drawable.chevron_down);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface AdapterActions {
        void onClick(int position);

        void onSwipe(int position);
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imagen, flechita;
        private TextView titulo, fecha_modificacion, contenido;
        private LinearLayout layout_contenido;

        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imagen = (ImageView) itemView.findViewById(R.id.foto);
            flechita = (ImageView) itemView.findViewById(R.id.expand);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            fecha_modificacion = (TextView) itemView.findViewById(R.id.fecha_modificacion);
            contenido = (TextView) itemView.findViewById(R.id.contenido);
            //This is to make EditText not editable
            contenido.setKeyListener(null);
            layout_contenido = (LinearLayout) itemView.findViewById(R.id.layout_contenido);
            layout_contenido.setVisibility(View.GONE);
            flechita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandedItems.get(getAdapterPosition())) {
                        layout_contenido.setVisibility(View.GONE);
                        flechita.setImageResource(R.drawable.chevron_down);
                        expandedItems.delete(getAdapterPosition());
                    } else {
                        layout_contenido.setVisibility(View.VISIBLE);
                        expandedItems.append(getAdapterPosition(), true);
                        flechita.setImageResource(R.drawable.chevron_up);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |           Clase para gestionar los movimientos de cada item del RecyclerView              |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    public class TripItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
        DeletedNotesAdapter MyAdapter;
        RecyclerView MyRecyclerView;

        public TripItemTouchHelperCallback(DeletedNotesAdapter mAdapter, RecyclerView mRecyclerView) {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
            this.MyAdapter = mAdapter;
            this.MyRecyclerView = mRecyclerView;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //some "move" implementation
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            listener.onSwipe(viewHolder.getAdapterPosition());
        }
    }

}

