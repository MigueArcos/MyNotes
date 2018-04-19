package com.example.miguel.misnotas.adapters;

/**
 * Created by Miguel on 13/06/2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Finance;


public class FinancesAdapter extends ArrayAdapter<Finance> implements FilterableRecyclerViewAdapter.ActionModeAdapterCallbacks<Finance> {
    private Context context;
    private final SparseBooleanArray selectedItemsIds;
    private ArrayList<Finance> datos;
    private boolean adapterNeedsActionMode = false;

    public boolean needsActionMode() {
        return adapterNeedsActionMode ;
    }



    @Override
    public void toggleSelection(int position) {
        if (selectedItemsIds.get(position)) {
            selectedItemsIds.delete(position);
        } else {
            selectedItemsIds.put(position, true);
        }
        notifyDataSetChanged();
    }

    @Override
    public void clearSelections() {
        selectedItemsIds.clear();
        adapterNeedsActionMode = false;
        notifyDataSetChanged();
    }

    @Override
    public int getSelectedCount() {
        return selectedItemsIds.size();
    }

    @Override
    public List<Finance> getSelectedItems() {
        final List<Finance> selectedItemList = new ArrayList<>();
        for (int i = 0; i < selectedItemsIds.size(); i++) {
            selectedItemList.add(datos.get(selectedItemsIds.keyAt(i)));
        }
        return selectedItemList;
    }

    public class vista_item {
        public ImageView foto;
        public TextView nombre, valor;
    }
    vista_item holder;


    public FinancesAdapter(Context context, ArrayList<Finance> datos) {
        super(context, R.layout.item_finance, datos);
        this.context = context;
        this.datos = datos;

        selectedItemsIds = new SparseBooleanArray();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            item = LayoutInflater.from(context).inflate(R.layout.item_finance, null);
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

        item.setBackgroundColor(selectedItemsIds.get(position) ? 0x9934B5E4 : Color.TRANSPARENT);

        holder.foto.setImageResource(datos.get(position).getDrawableImageID());
        holder.nombre.setText(datos.get(position).getNombre());
        holder.valor.setText("$ " + datos.get(position).getvalor());
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(position);
                adapterNeedsActionMode = true;
                ((ListView) parent).performItemClick(v, position, v.getId());
            }
        });
        return item;
    }

}
