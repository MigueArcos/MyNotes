package com.example.miguel.misnotas.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 20/06/2016.
 */
public class DeletedNotesAdapter extends FilterableRecyclerViewAdapter<Note, DeletedNotesAdapter.ItemView> {
    private SparseBooleanArray expandedItems;
    private DeletedNotesAdapterActions listener;
    private Context context;
    public DeletedNotesAdapter(DeletedNotesAdapterActions listener, Context context) {
        this.context = context;
        this.listener = listener;
        expandedItems = new SparseBooleanArray();
    }

    public void notifyExpandedItemDeleted(int position) {
        expandedItems.delete(position);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TripItemTouchHelperCallback(this, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deleted_note, parent, false);
        return new ItemView(v);
    }


    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.icon.setImageResource(Note.imageId);
        holder.title.setText(data.get(position).getTitle());
        holder.modificationDate.setText(String.format("Última modificación: %s", MyUtils.getTime12HoursFormat(data.get(position).getModificationDate(), context.getResources().getString(R.string.date_format))));
        holder.content.setText(data.get(position).getContent());
        //Estas lineas if-else son necesarias porque cuando hacemos scroll en el recyclerview el viewholder se va reciclando, esto quiere decir que si un viewholder ya estaba expandido y este es reciclado pues va a seguir expandido, por eso se tiene que checar si la posicion de ese viewholder efectivamente es una posicion con el estatus de expandido
        if (expandedItems.get(position)) {
            holder.contentLayout.setVisibility(View.VISIBLE);
            holder.arrow.setImageResource(R.drawable.chevron_up);
        } else {
            holder.contentLayout.setVisibility(View.GONE);
            holder.arrow.setImageResource(R.drawable.chevron_down);
        }
    }



    @Override
    public void filterResults(String filter) {
        List<Note> filteredNotes = new ArrayList<>();
        if (filter.isEmpty()){
            setData(originalList);
            return;
        }
        for (Note note : originalList){
            String comparator = note.getTitle().concat(note.getContent()).toLowerCase();
            filter = filter.toLowerCase();
            if (comparator.contains(filter)){
                filteredNotes.add(note);
            }
        }
        setData(filteredNotes);
    }


    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView icon, arrow;
        private TextView title, modificationDate, content;
        private LinearLayout contentLayout;

        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.foto);
            arrow = itemView.findViewById(R.id.expand);
            title = itemView.findViewById(R.id.title);
            modificationDate = itemView.findViewById(R.id.modificationDate);
            content = itemView.findViewById(R.id.content);
            //This is to make EditText not editable
            content.setKeyListener(null);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            contentLayout.setVisibility(View.GONE);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandedItems.get(getAdapterPosition())) {
                        contentLayout.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.chevron_down);
                        expandedItems.delete(getAdapterPosition());
                    } else {
                        contentLayout.setVisibility(View.VISIBLE);
                        expandedItems.append(getAdapterPosition(), true);
                        arrow.setImageResource(R.drawable.chevron_up);
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

