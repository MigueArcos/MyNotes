package com.example.miguel.misnotas.notes.current_notes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.notes.NotesContract;
import com.example.miguel.misnotas.utilities.ActionModeAdapterCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 20/06/2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ItemView> implements ActionModeAdapterCallbacks<Note> {

    public interface AdapterActions {
        void onSwipe(int position);

        void onItemClick(View v, int position);

        void onLongClick(View v, int position);

        void onIconClick(View v, int position);
    }

    public interface DataObserver{
        void onChanged(int listSize);
    }


    private AdapterActions listener;
    private SparseBooleanArray selectedItemsIds;
    private NotesContract.Presenter presenter;
    private Context context;
    private DataObserver dataObserver;


    public NotesAdapter(Context context) {
        this.context = context;
        selectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TripItemTouchHelperCallback(this, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ItemView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemView holder, int position) {
        presenter.bindHolderData(holder, position);
        holder.itemView.setActivated(selectedItemsIds.get(position));
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    public void setListener(AdapterActions listener) {
        this.listener = listener;
    }

    public void setPresenter(NotesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setDataObserver(DataObserver dataObserver) {
        this.dataObserver = dataObserver;
    }

    public void observeData(){
        if (dataObserver != null){
            dataObserver.onChanged(presenter.getItemCount());
        }
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
            selectedItemList.add(presenter.getItem(selectedItemsIds.keyAt(i)));
        }
        return selectedItemList;
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, NotesContract.ItemView {
        private ImageView noteIcon;
        private TextView titleText, modificationDateText;

        ItemView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            noteIcon = itemView.findViewById(R.id.icon);
            titleText = itemView.findViewById(R.id.title);
            modificationDateText = itemView.findViewById(R.id.modification_date);

            noteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_out);
                    noteIcon.startAnimation(animation);
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
                            noteIcon.startAnimation(animation2);
                            toggleSelection(getAdapterPosition());
                            listener.onIconClick(view, getAdapterPosition());
                            boolean isSelected = selectedItemsIds.get(getAdapterPosition());

                            noteIcon.setImageResource(isSelected? R.drawable.ok : R.drawable.note);
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
            //Return false to indicate that this event has been consumed, if we don't do this then both events will be fired
            return false;
        }

        @Override
        public void showTitle(String title) {
            titleText.setText(title);
        }

        @Override
        public void showContent(String content) {

        }


        @Override
        public void showModificationDate(String modificationDate) {
            modificationDateText.setText(modificationDate);
        }

        @Override
        public void showImageResource(int resourceId) {
            noteIcon.setImageResource(resourceId);
        }
    }


    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |           Clase para gestionar los movimientos de cada item del RecyclerView              |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    public class TripItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        private static final float buttonWidth = 300;
        private RecyclerView.ViewHolder selectedViewHolder;

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
                p.setColor(Color.parseColor("#f44336"));
                Drawable deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete_icon);


                if (dX > 0) {
                    /* Set your color for positive displacement */
                    // Draw Rect with varying right side, equal to displacement dX

                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), p);
                    int deleteIconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                    int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                    int deleteIconLeft = itemView.getLeft() + deleteIconMargin;
                    int deleteIconRight = itemView.getLeft() + deleteIconMargin + deleteIcon.getIntrinsicWidth();
                    int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();
                    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                    deleteIcon.draw(c);

                } else {
                    /* Set your color for negative displacement */
                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    int deleteIconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                    int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                    int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth();
                    int deleteIconRight = itemView.getRight() - deleteIconMargin;
                    int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();

                    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                    deleteIcon.draw(c);
                }

                //dX = (dX > 0) ? Math.min(dX, buttonWidth) : Math.max(dX, -buttonWidth);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        private void clearCanvas(Canvas c) {
            Paint p = new Paint();
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
    }
}
