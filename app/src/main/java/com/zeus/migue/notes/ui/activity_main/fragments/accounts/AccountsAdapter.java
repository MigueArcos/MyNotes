package com.zeus.migue.notes.ui.activity_main.fragments.accounts;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.AccountDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.recyclerview.CustomViewHolder;
import com.zeus.migue.notes.ui.shared.recyclerview.GenericRecyclerViewAdapter;
import com.zeus.migue.notes.ui.shared.recyclerview.ItemTouchHelperCallback;

import java.util.List;

public class AccountsAdapter extends GenericRecyclerViewAdapter<AccountDTO, RecyclerView.ViewHolder> {
    public static final int HEADER = 1;
    public static final int ITEM = 2;

    public AccountsAdapter(ItemSwipeListener<AccountDTO> itemSwipeListener) {
        super(new ItemTouchHelperCallback.ItemTouchHelperConfiguration(R.drawable.ic_delete_icon, R.drawable.ic_delete_icon, Color.parseColor("#f44336"), Color.parseColor("#f44336"), AccountsTotalViewHolder.class));
        itemTouchHelperConfiguration.setItemSwipeListenerMin((pos, swipeDir) -> itemSwipeListener.onItemSwiped(items.get(pos), pos, swipeDir));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_total, parent, false);
                return new AccountsTotalViewHolder(headerView);
            case ITEM:
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
                return new AccountViewHolder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == HEADER) {
            ((AccountsTotalViewHolder) holder).setTotal(getTotal());
        } else if (viewType == ITEM) {
            ((AccountViewHolder) holder).renderItem(items.get(position - 1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : ITEM;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() + 1 : 1;
    }

    @Override
    public void updateData(List<AccountDTO> items) {
        notifyDataSetChanged();
        this.items = items;
    }

    private double getTotal() {
        return Utils.listIsNullOrEmpty(items) ? 0 : items.stream().mapToDouble(AccountDTO::getTotal).sum();
    }


    public class AccountViewHolder extends CustomViewHolder<AccountDTO> {
        private TextView titleText, modificationDateText;
        private MaterialLetterIcon materialLetterIcon;

        AccountViewHolder(View itemView) {
            super(itemView, itemClickListener);
            titleText = itemView.findViewById(R.id.title_text);
            modificationDateText = itemView.findViewById(R.id.modification_date_text);
            materialLetterIcon = itemView.findViewById(R.id.letter);
            materialLetterIcon.setShapeColor(Utils.pickRandomColor(itemView.getContext()));
            itemView.setOnClickListener(this);
        }

        @Override
        public void renderItem(AccountDTO item) {
            String text = item.getName();
            materialLetterIcon.setLetter("" + text.charAt(0));
            titleText.setText(text);
            modificationDateText.setText(Utils.niceDateFormat(Utils.fromIso8601(item.getModificationDate(), true)));
        }

        @Override
        public AccountDTO getItem() {
            return items.get(getAdapterPosition() - 1);
        }
    }

    public class AccountsTotalViewHolder extends RecyclerView.ViewHolder {
        private TextView totalText;

        AccountsTotalViewHolder(View itemView) {
            super(itemView);
            totalText = itemView.findViewById(R.id.total_text);
        }

        public void setTotal(double total) {
            totalText.setText(Utils.formatCurrency(total));
        }
    }
}
