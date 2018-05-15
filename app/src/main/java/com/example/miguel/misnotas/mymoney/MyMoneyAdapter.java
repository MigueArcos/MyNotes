package com.example.miguel.misnotas.mymoney;

/**
 * Created by Miguel on 13/06/2016.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.adapters.FilterableRecyclerViewAdapter;
import com.example.miguel.misnotas.models.Finance;

import java.util.List;


public class MyMoneyAdapter extends RecyclerView.Adapter<MyMoneyAdapter.ItemView> implements FilterableRecyclerViewAdapter.ActionModeAdapterCallbacks<Finance> {

    private final SparseBooleanArray selectedItemsIds;
    private MyMoneyPresenter presenter;
    private MyMoneyAdapterActions listener;
    private Context context;

    public interface MyMoneyAdapterActions {

        void onItemClick(View v, int position);

        void onLongClick(View v, int position);

        void onIconClick(View v, int position);
    }

    public MyMoneyAdapter(MyMoneyPresenter presenter, Context context) {
        selectedItemsIds = new SparseBooleanArray();
        this.presenter = presenter;
        this.context = context;
    }

    public void setFinancesAdapterActions(MyMoneyAdapterActions listener){
        this.listener = listener;
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
    public List<Finance> getSelectedItems() {
        return presenter.getSelectedItemsList(selectedItemsIds);
    }

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemView(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_finance, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemView holder, int position) {
        presenter.onBindViewAtPosition(position, holder);
        boolean isSelected = selectedItemsIds.get(position);
        holder.icon.setImageResource(isSelected? R.drawable.ok : R.drawable.mercedes_benz_logo);
        holder.itemView.setActivated(isSelected);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }


    public class ItemView extends RecyclerView.ViewHolder implements MyMoneyMVP.ItemView, View.OnClickListener, View.OnLongClickListener{
        private ImageView icon;
        private TextView nameText, valueText;

        public ItemView(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            nameText = itemView.findViewById(R.id.name);
            valueText = itemView.findViewById(R.id.value);
            icon.setOnClickListener(view -> listener.onIconClick(view, getAdapterPosition()));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            icon.setOnClickListener(view -> {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_out);
                icon.startAnimation(animation);
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
                        icon.startAnimation(animation2);
                        toggleSelection(getAdapterPosition());
                        listener.onIconClick(view, getAdapterPosition());
                        boolean isSelected = selectedItemsIds.get(getAdapterPosition());

                        icon.setImageResource(isSelected? R.drawable.ok : R.drawable.mercedes_benz_logo);
                        itemView.setActivated(isSelected);
                    }
                });
            });
        }

        @Override
        public void setName(String name) {
            nameText.setText(name);
        }

        @Override
        public void setValue(int value) {
            valueText.setText(String.valueOf(value));
        }

        @Override
        public void setIcon(int iconResourceId) {
            icon.setImageResource(iconResourceId);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(view, getAdapterPosition());
            return true;
        }
    }
}
