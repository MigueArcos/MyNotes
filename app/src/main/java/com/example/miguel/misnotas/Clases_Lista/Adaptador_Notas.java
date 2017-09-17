package com.example.miguel.misnotas.Clases_Lista;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miguel.misnotas.Base_Datos;
import com.example.miguel.misnotas.Editor_Notas;
import com.example.miguel.misnotas.R;

import java.util.ArrayList;

/**
 * Created by Miguel on 20/06/2016.
 */
public class Adaptador_Notas extends RecyclerView.Adapter<Adaptador_Notas.vista_item> {
    private ArrayList<Elemento_Nota> datos;
    Base_Datos ob;
    Context Contexto_Mi_Actividad;
    Snackbar snackbar;
    public Adaptador_Notas(ArrayList<Elemento_Nota> datos, Context Contexto_Mi_Actividad) {
        this.Contexto_Mi_Actividad=Contexto_Mi_Actividad;
        ob=new Base_Datos(Contexto_Mi_Actividad);
        this.datos = datos;
    }
    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class vista_item extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView foto;
        public TextView titulo, fecha_modificacion;
        vista_item(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            foto = (ImageView) itemView.findViewById(R.id.foto);
            titulo = (TextView)itemView.findViewById(R.id.titulo);
            fecha_modificacion = (TextView)itemView.findViewById(R.id.fecha_modificacion);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(Contexto_Mi_Actividad,"Holis "+datos.get(getAdapterPosition()).getId_nota(), Toast.LENGTH_SHORT).show();
            quitar_snack();
            Elemento_Nota nota_sel=datos.get(getAdapterPosition());
            Intent i = new Intent(Contexto_Mi_Actividad, Editor_Notas.class);
            Bundle paquete = new Bundle();
            //Add your data from getFactualResults method to bundle
            paquete.putBoolean("NuevaNota",false);
            paquete.putString("contenido",nota_sel.getContenido());
            paquete.putString("titulo",nota_sel.getTitulo());
            paquete.putInt("id_nota_mod",nota_sel.getID_Nota());
            //Add the bundle to the intent
            i.putExtras(paquete);
            Contexto_Mi_Actividad.startActivity(i);
        }
    }
    public void quitar_snack(){
        if (snackbar!=null){
            snackbar.dismiss();
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_nota, parent, false);
        vista_item holder = new vista_item(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(vista_item holder, int position) {
        holder.foto.setImageResource(datos.get(position).getID_Imagen());
        holder.titulo.setText(datos.get(position).getTitulo());
        holder.fecha_modificacion.setText("Última modificación: "+datos.get(position).getFecha_modificacion());
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
        Adaptador_Notas MyAdapter;
        RecyclerView MyRecyclerView;
        public  TripItemTouchHelperCallback (Adaptador_Notas mAdapter, RecyclerView mRecyclerView){
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

            snackbar = Snackbar.make(MyRecyclerView, "Nota eliminada", Snackbar.LENGTH_LONG).
                    setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Toast.makeText(recyclerView.getContext(),""+adapterPosition, Toast.LENGTH_SHORT).show();
                            datos.add(adapterPosition, notita);
                            notifyItemInserted(adapterPosition);
                            MyRecyclerView.scrollToPosition(adapterPosition);

                        }
                    }).setCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event!=DISMISS_EVENT_ACTION) {
                        ob.eliminar_nota(id_nota_a_borrar);
                    }
                    //Si hubiera sido por DISMISS_EVENT_ACTION significa que el usuario presiono deshacer y por lo tanto no quiere que se
                    //elimine de la base de datos
                }

                @Override
                public void onShown(Snackbar snackbar) {

                }

            });

            datos.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            snackbar.show();
        }
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;

                Paint p = new Paint();
                p.setColor(Color.rgb(96,125,139));
                if (dX > 0) {
            /* Set your color for positive displacement */

                    // Draw Rect with varying right side, equal to displacement dX
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), p);
                } else {
            /* Set your color for negative displacement */

                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    }
}
