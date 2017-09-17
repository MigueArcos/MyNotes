package com.example.miguel.misnotas.Clases_Lista;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.R;

import java.util.ArrayList;

/**
 * Created by Miguel on 20/06/2016.
 */
public class Adaptador_Notas_Eliminadas extends RecyclerView.Adapter<Adaptador_Notas_Eliminadas.vista_item> {
    private ArrayList<Elemento_Nota> datos;
    private Context Contexto_Mi_Actividad;
    private AlertDialog mensaje;
    private AlertDialog.Builder builder;
    private ArrayList<Integer> expandidos;
    public interface AdapterActions{
        void onClick(int position);
        void onSwipe(int position);
    }
    public Adaptador_Notas_Eliminadas(ArrayList<Elemento_Nota> datos, Context Contexto_Mi_Actividad) {
        this.Contexto_Mi_Actividad=Contexto_Mi_Actividad;
        expandidos=new ArrayList<>();
        builder=new AlertDialog.Builder(Contexto_Mi_Actividad);
        builder.setTitle("Notas de MigueLópez :D");
        mensaje=null;
        this.datos = datos;
    }
    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class vista_item extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imagen, flechita;
        private TextView titulo, fecha_modificacion, contenido;
        private LinearLayout layout_contenido;
        vista_item(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imagen = (ImageView) itemView.findViewById(R.id.foto);
            flechita=(ImageView)itemView.findViewById(R.id.expand);
            titulo = (TextView)itemView.findViewById(R.id.titulo);
            fecha_modificacion = (TextView)itemView.findViewById(R.id.fecha_modificacion);
            contenido=(TextView)itemView.findViewById(R.id.contenido);
            layout_contenido=(LinearLayout)itemView.findViewById(R.id.layout_contenido);
            layout_contenido.setVisibility(View.GONE);
            flechita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandidos.contains(getAdapterPosition())){
                        layout_contenido.setVisibility(View.GONE);
                        flechita.setImageResource(R.drawable.chevron_down);
                        expandidos.remove(new Integer(getAdapterPosition()));

                    }
                    else{
                        layout_contenido.setVisibility(View.VISIBLE);
                        expandidos.add(getAdapterPosition());
                        flechita.setImageResource(R.drawable.chevron_up);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            final int posicion=getAdapterPosition();
            builder.setMessage("¿Te gustaría recuperar esta nota?").
                    setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Database.getInstance(Contexto_Mi_Actividad).recuperar_nota(datos.get(posicion).getID_Nota());
                            datos.remove(posicion);
                            notifyItemRemoved(posicion);
                            expandidos.remove(new Integer(posicion));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            builder.setCancelable(false);
            mensaje=builder.create();
            mensaje.show();
        }
    }
    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(ArrayList<Elemento_Nota> data){
        this.datos= data;
        notifyDataSetChanged();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TripItemTouchHelperCallback(this, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public vista_item onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota_eliminada, parent, false);
        vista_item holder = new vista_item(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(vista_item holder, int position) {
        holder.imagen.setImageResource(datos.get(position).getID_Imagen());
        holder.titulo.setText(datos.get(position).getTitulo());
        holder.fecha_modificacion.setText("Última modificación: "+datos.get(position).getFecha_modificacion());
        holder.contenido.setText(datos.get(position).getContenido());
        //Estas lineas if-else son necesarias porque cuando hacemos scroll en el recyclerview el viewholder se va reciclando, esto quiere decir que si un viewholder ya estaba expandido y este es reciclado pues va a seguir expandido, por eso se tiene que checar si la posicion de ese viewholder efectivamente es una posicion con el estatus de expandido
        if (expandidos.contains(position)){
            holder.layout_contenido.setVisibility(View.VISIBLE);
            holder.flechita.setImageResource(R.drawable.chevron_up);
        }
        else{
            holder.layout_contenido.setVisibility(View.GONE);
            holder.flechita.setImageResource(R.drawable.chevron_down);
        }
    }
    public void QuitarAlertDialog(){
        if (mensaje!=null && mensaje.isShowing()){
            mensaje.dismiss();
        }
    }
    @Override
    public int getItemCount() {
        return datos.size();
    }
    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |           Clase para gestionar los movimientos de cada item del RecyclerView              |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    public class TripItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
        Adaptador_Notas_Eliminadas MyAdapter;
        RecyclerView MyRecyclerView;
        public  TripItemTouchHelperCallback (Adaptador_Notas_Eliminadas mAdapter, RecyclerView mRecyclerView){
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
            this.MyAdapter=mAdapter;
            this.MyRecyclerView=mRecyclerView;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //some "move" implementation
            return false;
        }
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            final Elemento_Nota notita=datos.get(adapterPosition);
            final int id_nota_a_borrar=datos.get(adapterPosition).getID_Nota();
            builder.setMessage("¿Estás seguro de que deseas eliminar completamente esta nota?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Database.getInstance(Contexto_Mi_Actividad).eliminar_nota_completamente(id_nota_a_borrar);
                            expandidos.remove(new Integer(adapterPosition));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            datos.add(adapterPosition, notita);
                            notifyItemInserted(adapterPosition);
                            MyRecyclerView.scrollToPosition(adapterPosition);
                        }
                    });
            // Create the AlertDialog object and return it
            builder.setCancelable(false);
            mensaje=builder.create();
            mensaje.show();
            datos.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }


    }

}

