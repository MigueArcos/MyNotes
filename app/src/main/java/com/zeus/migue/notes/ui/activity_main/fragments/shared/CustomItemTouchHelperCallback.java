package com.zeus.migue.notes.ui.activity_main.fragments.shared;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zeus.migue.notes.data.DTO.NoteDTO;

public class CustomItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
    private Drawable deleteIcon;
    private Drawable recoverIcon;
    private BaseNotesFragmentAdapter adapter;
    public CustomItemTouchHelperCallback(BaseNotesFragmentAdapter adapter, Drawable deleteIcon, Drawable recoverIcon) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        this.deleteIcon = deleteIcon;
        this.recoverIcon = recoverIcon;
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //some "move" implementation
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
        BaseNotesFragmentAdapter.onItemSwipeListener<NoteDTO> onItemSwipeListener = adapter.getOnItemSwipe();
        if (onItemSwipeListener != null)
            onItemSwipeListener.onItemSwiped(adapter.getNotes().get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition(), swipeDir);
    }


    @Override
    public void onChildDraw(Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;
            Paint p = new Paint();
            int color = adapter.shouldShowDeleted() ? dX > 0 ? Color.parseColor("#00897B") : Color.parseColor("#f44336") : Color.parseColor("#f44336");
            Drawable icon = adapter.shouldShowDeleted() ? dX > 0 ? recoverIcon : deleteIcon : deleteIcon;
            if (dX > 0) {
                p.setColor(color);
                /* Set your color for positive displacement */
                // Draw Rect with varying right side, equal to displacement dX

                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);
                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                int iconBottom = iconTop + icon.getIntrinsicHeight();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                icon.draw(c);

            } else {
                p.setColor(color);

                /* Set your color for negative displacement */
                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), p);

                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                int iconBottom = iconTop + icon.getIntrinsicHeight();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                icon.draw(c);

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
