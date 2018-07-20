package com.example.miguel.misnotas.notes.deleted_notes;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.notes.NotesContract;

/**
 * Created by Miguel on 20/06/2016.
 */
public class DeletedNotesAdapter extends RecyclerView.Adapter<DeletedNotesAdapter.ItemView> {
    private AdapterActions listener;
    private Context context;
    private NotesContract.Presenter presenter;
    private DataObserver dataObserver;

    public interface AdapterActions {
        void onItemClick(int position);

        void onSwipe(int position);

        void onArrowClick(int position);
    }

    public interface DataObserver{
        void onChanged(int listSize);
    }

    public DeletedNotesAdapter(Context context) {
        this.context = context;
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TripItemTouchHelperCallback(this, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deleted_note, parent, false);
        return new ItemView(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemView holder, int position) {
        presenter.bindHolderData(holder, position);
    }

    @Override
    public int getItemCount() {
        if (dataObserver != null){
            dataObserver.onChanged(presenter.getItemCount());
        }
        return presenter.getItemCount();
    }

    public void observeData(){
        if (dataObserver != null){
            dataObserver.onChanged(presenter.getItemCount());
        }
    }

    /***
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * |                    Clase Holder que contiene la vista de cada item                        |
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * */
    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener, NotesContract.ItemView {
        private ImageView icon, arrow;
        private TextView titleText, modificationDateText, contentText;
        private LinearLayout contentLayout;

        ItemView(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            arrow = itemView.findViewById(R.id.expand);
            titleText = itemView.findViewById(R.id.title);
            modificationDateText = itemView.findViewById(R.id.modification_date);
            contentText = itemView.findViewById(R.id.content);
            //This is to make EditText not editable
            contentText.setKeyListener(null);
            contentLayout = itemView.findViewById(R.id.content_layout);
            contentLayout.setVisibility(View.GONE);

            arrow.setOnClickListener(v -> {
                listener.onArrowClick(getAdapterPosition());
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }

        @Override
        public void showTitle(String title) {
            titleText.setText(title);
        }

        @Override
        public void showContent(String content) {
            contentText.setText(content);
        }

        @Override
        public void showModificationDate(String modificationDate) {
            modificationDateText.setText(modificationDate);
        }

        @Override
        public void showImageResource(int resourceId) {
            icon.setImageResource(resourceId);
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

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;
                Paint p = new Paint();
                p.setColor(Color.parseColor("#f44336"));
                Drawable deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete_forever);


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
    }

}

