package com.example.miguel.misnotas.Clases_Lista;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.miguel.misnotas.R;
import java.util.List;

/**
 * Created by Miguel on 20/06/2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ItemView> {
    private List<Elemento_Nota> data;
    private MyRecyclerViewActions listener;
    public interface MyRecyclerViewActions{
        void onSwipe(int position);
        void onTouch(int position);
    }

    public NotesAdapter(List<Elemento_Nota> data, MyRecyclerViewActions listener) {
        this.data = data;
        this.listener=listener;
    }
    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView noteImage;
        public TextView title, modificationDate;
        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            noteImage = (ImageView) itemView.findViewById(R.id.foto);
            title = (TextView)itemView.findViewById(R.id.titulo);
            modificationDate = (TextView)itemView.findViewById(R.id.fecha_modificacion);
        }

        @Override
        public void onClick(View v) {
            //Create intent to access the other activity with note data
            listener.onTouch(getAdapterPosition());
        }
    }

    //Gotten here -> https://stackoverflow.com/questions/17341066/android-listview-does-not-update-onresume
    public void setData(List<Elemento_Nota> data){
        this.data= data;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_nota, parent, false);
        ItemView holder = new ItemView(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.noteImage.setImageResource(data.get(position).getID_Imagen());
        holder.title.setText(data.get(position).getTitulo());
        holder.modificationDate.setText("Última modificación: "+data.get(position).getFecha_modificacion());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |           Clase para gestionar los movimientos de cada item del RecyclerView              |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    public class TripItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
        NotesAdapter MyAdapter;
        RecyclerView MyRecyclerView;
        public  TripItemTouchHelperCallback (NotesAdapter mAdapter, RecyclerView mRecyclerView){
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
            listener.onSwipe(viewHolder.getAdapterPosition());
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
