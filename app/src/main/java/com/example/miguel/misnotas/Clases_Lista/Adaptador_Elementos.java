package com.example.miguel.misnotas.Clases_Lista;

/**
 * Created by Miguel on 13/06/2016.
 */
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguel.misnotas.R;

public class Adaptador_Elementos extends ArrayAdapter<Elemento_Lista>{
    private Context context;
    private ArrayList<Elemento_Lista> datos;
    public class vista_item {
        public ImageView foto;
        public TextView nombre, valor;
    }
    vista_item holder;
    public Adaptador_Elementos(Context context, ArrayList<Elemento_Lista> datos) {
        super(context, R.layout.listview_item, datos);
        this.context = context;
        this.datos = datos;
    }
    @Override
    public View getView(final int position, View convertView,ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            item = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
            holder = new vista_item();
            holder.foto = (ImageView) item.findViewById(R.id.foto);
            holder.nombre = (TextView) item.findViewById(R.id.nombre);
            holder.valor = (TextView) item.findViewById(R.id.valor);
            item.setTag(holder);
        }
        else {
            holder = (vista_item) item.getTag();
        }
        //Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
        if(datos.get(position).isticked()){
            item.setBackgroundColor(Color.rgb(200,200,200));
        }
        else {
            item.setBackgroundColor(Color.argb(0,0,0,0));
        }
        holder.foto.setImageResource(datos.get(position).getDrawableImageID());
        holder.nombre.setText(datos.get(position).getNombre());
        holder.valor.setText("$ " + datos.get(position).getvalor());
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datos.get(position).isticked()) {
                    datos.get(position).untick();
                } else {
                    Toast.makeText(getContext(), "" + position + " " + datos.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                    datos.get(position).setTicked(R.drawable.ok);
                }
                Adaptador_Elementos.this.notifyDataSetChanged();
            }
        });
        return item;
    }

}
