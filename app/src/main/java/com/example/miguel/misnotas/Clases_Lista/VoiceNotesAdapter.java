package com.example.miguel.misnotas.Clases_Lista;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miguel.misnotas.R;

import java.util.List;

/**
 * Created by 79812 on 22/11/2017.
 */
public class VoiceNotesAdapter extends RecyclerView.Adapter<VoiceNotesAdapter.ItemView> {
    private List<VoiceNote> data;
    private AdapterActions listener;

    public VoiceNotesAdapter(List<VoiceNote> data, AdapterActions listener) {
        //this.data = data;
        this.data = data;
        this.listener = listener;
    }


    //Sacado de aqui -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(List<VoiceNote> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_note, parent, false);
        ItemView holder = new ItemView(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.lastUpdate.setText(data.get(position).getLastUpdate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface AdapterActions {
        void onClick(int position);
        void onLongClick(int position);
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title, lastUpdate;

        ItemView(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.voice_note_title);
            lastUpdate = itemView.findViewById(R.id.voice_note_last_update);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(getAdapterPosition());
            return true;
        }
    }


}

