package com.zeus.migue.notes.ui.activity_main.fragments.clipboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.recyclerview.CustomViewHolder;
import com.zeus.migue.notes.ui.shared.recyclerview.GenericRecyclerViewAdapter;
import com.zeus.migue.notes.ui.shared.recyclerview.ItemTouchHelperCallback;

public class ClipboardAdapter extends GenericRecyclerViewAdapter<ClipItemDTO, ClipboardAdapter.ClipItemViewHolder> {
    public ClipboardAdapter(ItemSwipeListener<ClipItemDTO> itemSwipeListener) {
        super(new ItemTouchHelperCallback.ItemTouchHelperConfiguration(R.drawable.ic_delete_icon, R.drawable.ic_delete_icon, Color.parseColor("#f44336"), Color.parseColor("#f44336")));
        itemTouchHelperConfiguration.setItemSwipeListenerMin((pos, swipeDir) -> itemSwipeListener.onItemSwiped(items.get(pos), pos, swipeDir));
    }

    @NonNull
    @Override
    public ClipItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ClipItemViewHolder(layoutView);
    }

    public class ClipItemViewHolder extends CustomViewHolder<ClipItemDTO> {
        private TextView contentText, modificationDateText;
        private MaterialLetterIcon materialLetterIcon;
        ClipItemViewHolder(View itemView) {
            super(itemView, itemClickListener);
            contentText = itemView.findViewById(R.id.title_text);
            modificationDateText = itemView.findViewById(R.id.modification_date_text);
            materialLetterIcon = itemView.findViewById(R.id.letter);
            materialLetterIcon.setShapeColor(Utils.pickRandomColor(itemView.getContext()));
            itemView.setOnClickListener(this);
        }

        @Override
        public void renderItem(ClipItemDTO item) {
            String text = item.getContent();
            materialLetterIcon.setLetter("" + text.charAt(0));
            contentText.setText(text);
            modificationDateText.setText(Utils.niceDateFormat(Utils.fromIso8601(item.getModificationDate(), true)));
        }

        @Override
        public ClipItemDTO getItem() {
            return items.get(getAdapterPosition());
        }
    }
}
