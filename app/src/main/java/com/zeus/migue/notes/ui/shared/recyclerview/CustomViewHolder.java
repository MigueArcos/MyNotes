package com.zeus.migue.notes.ui.shared.recyclerview;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class CustomViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{
    private GenericRecyclerViewAdapter.ItemClickListener<T> itemClickListener;
    public CustomViewHolder(View itemView, GenericRecyclerViewAdapter.ItemClickListener<T> itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
    }
    public abstract void renderItem(T item);
    public abstract T getItem();

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) itemClickListener.onClickItem(getItem(), getAdapterPosition());
    }
}