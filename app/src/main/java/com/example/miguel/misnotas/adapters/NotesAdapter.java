package com.example.miguel.misnotas.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 20/06/2016.
 */
public class NotesAdapter extends FilterableRecyclerViewAdapter<Note, NotesAdapter.ItemView> implements FilterableRecyclerViewAdapter.ActionModeAdapterCallbacks<Note>{
    public interface NotesAdapterActions {
        void onSwipe(int position);

        void onItemClick(View v, int position);

        void onLongClick(View v, int position);

        void onIconClick(View v, int position);
    }
    private NotesAdapterActions listener;
    private Context context;
    private SparseBooleanArray selectedItemsIds;
    public NotesAdapter(NotesAdapterActions listener, Context context) {
        this.context = context;
        this.listener = listener;
        selectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public void toggleSelection(int position) {
        if (selectedItemsIds.get(position)) {
            selectedItemsIds.delete(position);
        } else {
            selectedItemsIds.put(position, true);
        }
    }

    @Override
    public void clearSelections() {
        selectedItemsIds.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getSelectedCount() {
        return selectedItemsIds.size();
    }

    @Override
    public List<Note> getSelectedItems() {
        final List<Note> selectedItemList = new ArrayList<>();
        for (int i = 0; i < selectedItemsIds.size(); i++) {
            selectedItemList.add(data.get(selectedItemsIds.keyAt(i)));
        }
        return selectedItemList;
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView noteImage;
        private TextView title, modificationDate;

        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            noteImage = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            modificationDate = itemView.findViewById(R.id.modification_date);

            noteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_out);
                    noteImage.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.scale_in);
                            noteImage.startAnimation(animation2);
                            toggleSelection(getAdapterPosition());
                            listener.onIconClick(view, getAdapterPosition());
                            boolean isSelected = selectedItemsIds.get(getAdapterPosition());

                            noteImage.setImageResource(isSelected? R.drawable.ok : R.drawable.note);
                            itemView.setActivated(isSelected);
                        }
                    });
                }
            });
        }

        @Override
        public void onClick(View v) {
            //Create intent to access the other activity with note data
            listener.onItemClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClick(v, getAdapterPosition());
            //Return true to indicate that this event has been consumed, if we don't do this then both events will be called
            return true;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TripItemTouchHelperCallback(this, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ItemView(v);
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.noteImage.setImageResource(selectedItemsIds.get(position) ? R.drawable.ok : R.drawable.note);
        holder.title.setText(data.get(position).getTitle());
        holder.modificationDate.setText(String.format("Última modificación: %s", MyUtils.getTime12HoursFormat(data.get(position).getModificationDate(), context.getResources().getString(R.string.date_format))));
        holder.itemView.setActivated(selectedItemsIds.get(position));
    }


    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |           Clase para gestionar los movimientos de cada item del RecyclerView              |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    public class TripItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        public TripItemTouchHelperCallback(NotesAdapter mAdapter, RecyclerView mRecyclerView) {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
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

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;
                Paint p = new Paint();
                p.setColor(Color.rgb(96, 125, 139));
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
