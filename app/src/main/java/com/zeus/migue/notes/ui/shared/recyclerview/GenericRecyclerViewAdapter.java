package com.zeus.migue.notes.ui.shared.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class GenericRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public interface ItemClickListener<T> {
        void onClickItem(T data, int position);
    }

    public interface ItemSwipeListener<T> {
        void onItemSwiped(T data, int position, int swipeDir);
    }

    public interface ItemSwipeListenerMin {
        void onItemSwiped(int position, int swipeDir);
    }

    protected List<T> items;
    protected ItemTouchHelperCallback.ItemTouchHelperConfiguration itemTouchHelperConfiguration;
    protected ItemClickListener<T> itemClickListener;
    public GenericRecyclerViewAdapter(ItemTouchHelperCallback.ItemTouchHelperConfiguration itemTouchHelperConfiguration) {
        this.itemTouchHelperConfiguration = itemTouchHelperConfiguration;
    }

    public void setItemClickListener(ItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        if (itemTouchHelperConfiguration != null) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(itemTouchHelperConfiguration));
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ((CustomViewHolder<T>)holder).renderItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateData(List<T> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ItemDiffCallback(this.items, items));
        diffResult.dispatchUpdatesTo(this);
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }
    public class ItemDiffCallback extends DiffUtil.Callback {
        private List<T> mOldList;
        private List<T> mNewList;

        public ItemDiffCallback(List<T> oldList, List<T> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList != null ? mOldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewList != null ? mNewList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mNewList.get(newItemPosition).hashCode() == mOldList.get(oldItemPosition).hashCode();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
        }
    }

}
