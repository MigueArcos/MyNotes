package com.zeus.migue.notes.ui.activity_main.fragments.clipboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.ClipNoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.recyclerview.CustomViewHolder;
import com.zeus.migue.notes.ui.shared.recyclerview.GenericRecyclerViewAdapter;
import com.zeus.migue.notes.ui.shared.recyclerview.ItemTouchHelperCallback;

public class ClipboardAdapter extends GenericRecyclerViewAdapter<ClipNoteDTO, ClipboardAdapter.ClipItemViewHolder> {
    public ClipboardAdapter(ItemSwipeListener<ClipNoteDTO> itemSwipeListener) {
        super(new ItemTouchHelperCallback.ItemTouchHelperConfiguration(R.drawable.ic_delete_icon, R.drawable.ic_delete_icon, Color.parseColor("#f44336"), Color.parseColor("#f44336")));
        itemTouchHelperConfiguration.setItemSwipeListenerMin((pos, swipeDir) -> itemSwipeListener.onItemSwiped(items.get(pos), pos, swipeDir));
    }

    @NonNull
    @Override
    public ClipItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_generic, parent, false);
        return new ClipItemViewHolder(layoutView);
    }

    public class ClipItemViewHolder extends CustomViewHolder<ClipNoteDTO> {
        private TextView titleText, modificationDateText, modificationTimeText;
        private MaterialLetterIcon materialLetterIcon;
        ClipItemViewHolder(View itemView) {
            super(itemView, itemClickListener);
            titleText = itemView.findViewById(R.id.title_text);
            itemView.findViewById(R.id.content_text).setVisibility(View.GONE);
            materialLetterIcon = itemView.findViewById(R.id.letter);
            materialLetterIcon.setShapeColor(Utils.pickRandomColor(itemView.getContext()));
            modificationDateText = itemView.findViewById(R.id.modification_date);
            modificationTimeText = itemView.findViewById(R.id.modification_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void renderItem(ClipNoteDTO item) {
            String text = item.getContent();
            materialLetterIcon.setLetter("" + text.charAt(0));
            titleText.setText(text);

            String date = Utils.niceDateFormat(Utils.fromIso8601(item.getModificationDate(), true));
            String[] dateParts = date.split(" ");
            modificationDateText.setText(dateParts[0]);
            modificationTimeText.setText(String.format("%s %s", dateParts[1], dateParts[2]));
        }

        @Override
        public ClipNoteDTO getItem() {
            return items.get(getAdapterPosition());
        }
    }
}
