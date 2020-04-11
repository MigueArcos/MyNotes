package com.zeus.migue.notes.ui.shared.recyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    public static class ItemTouchHelperConfiguration {
        private int iconLeft;
        private int iconRight;
        private int colorLeft;
        private int colorRight;
        private GenericRecyclerViewAdapter.ItemSwipeListenerMin itemSwipeListener;
        private Class<?>[] holdersNotSwipeable;
        public ItemTouchHelperConfiguration(int iconLeft, int iconRight, int colorLeft, int colorRight) {
            this.iconLeft = iconLeft;
            this.iconRight = iconRight;
            this.colorLeft = colorLeft;
            this.colorRight = colorRight;
        }
        public ItemTouchHelperConfiguration(int iconLeft, int iconRight, int colorLeft, int colorRight, Class<?>... classes) {
            this.iconLeft = iconLeft;
            this.iconRight = iconRight;
            this.colorLeft = colorLeft;
            this.colorRight = colorRight;
            this.holdersNotSwipeable = classes;
        }
        public void setItemSwipeListenerMin(GenericRecyclerViewAdapter.ItemSwipeListenerMin itemSwipeListener){
            this.itemSwipeListener = itemSwipeListener;
        }
        public <T extends RecyclerView.ViewHolder> boolean  holderIsSwipeable(T holder) {
            if (holdersNotSwipeable == null || holdersNotSwipeable.length == 0) return true;
            for (Class<?> clazz : holdersNotSwipeable){
                if (clazz.isInstance(holder)) return false;
            }
            return true;
        }
    }

    private ItemTouchHelperConfiguration configuration;
    public ItemTouchHelperCallback(ItemTouchHelperConfiguration configuration) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        this.configuration = configuration;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //some "move" implementation
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
        if (configuration.itemSwipeListener != null)
            configuration.itemSwipeListener.onItemSwiped(viewHolder.getAdapterPosition(), swipeDir);
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (!configuration.holderIsSwipeable(viewHolder)) return 0;
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;
            Paint p = new Paint();
            int color = dX > 0 ? configuration.colorLeft : configuration.colorRight;
            Drawable icon = recyclerView.getContext().getDrawable(dX > 0 ? configuration.iconLeft : configuration.iconRight);
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
}
