package com.zeus.migue.notes.ui.activity_main.fragments.notes.shared;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.recyclerview.CustomViewHolder;
import com.zeus.migue.notes.ui.shared.recyclerview.GenericRecyclerViewAdapter;
import com.zeus.migue.notes.ui.shared.recyclerview.ItemTouchHelperCallback;

public class BaseNotesFragmentAdapter extends GenericRecyclerViewAdapter<NoteDTO, BaseNotesFragmentAdapter.NoteViewHolder> {
    public BaseNotesFragmentAdapter(ItemTouchHelperCallback.ItemTouchHelperConfiguration itemTouchHelperConfiguration) {
        super(itemTouchHelperConfiguration);
    }

    private boolean showDeleted;

    public BaseNotesFragmentAdapter(boolean showDeleted, ItemSwipeListener<NoteDTO> itemSwipeListener) {
        super(new ItemTouchHelperCallback.ItemTouchHelperConfiguration(showDeleted ? R.drawable.ic_done : R.drawable.ic_delete_icon, R.drawable.ic_delete_icon, showDeleted ? Color.parseColor("#00897B") : Color.parseColor("#f44336"), Color.parseColor("#f44336")));
        itemTouchHelperConfiguration.setItemSwipeListenerMin((pos, swipeDir) -> itemSwipeListener.onItemSwiped(items.get(pos), pos, swipeDir));
        this.showDeleted = showDeleted;
    }

    public boolean shouldShowDeleted() {
        return showDeleted;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_generic, parent, false);
        return new NoteViewHolder(layoutView);
    }

    public class NoteViewHolder extends CustomViewHolder<NoteDTO> {
        private TextView titleText, contentText, modificationDateText, modificationTimeText;
        private MaterialLetterIcon materialLetterIcon;
        private View itemView;
        NoteViewHolder(View itemView) {
            super(itemView, itemClickListener);
            this.itemView = itemView;
            titleText = itemView.findViewById(R.id.title_text);
            contentText = itemView.findViewById(R.id.content_text);
            modificationDateText = itemView.findViewById(R.id.modification_date);
            modificationTimeText = itemView.findViewById(R.id.modification_time);
            materialLetterIcon = itemView.findViewById(R.id.letter);
            materialLetterIcon.setShapeColor(Utils.pickRandomColor(itemView.getContext()));
            itemView.setOnClickListener(this);
        }

        @Override
        public void renderItem(NoteDTO item) {
            String title = Utils.stringIsNullOrEmpty(item.getTitle()) ? itemView.getContext().getString(R.string.no_title_note) : item.getTitle();
            materialLetterIcon.setLetter("" + title.charAt(0));
            titleText.setText(title);
            String date = Utils.niceDateFormat(Utils.fromIso8601(item.getModificationDate(), true));
            String[] dateParts = date.split(" ");
            modificationDateText.setText(dateParts[0]);
            modificationTimeText.setText(String.format("%s %s", dateParts[1], dateParts[2]));

            String content = Utils.stringIsNullOrEmpty(item.getContent()) ? itemView.getContext().getString(R.string.no_content_note) : item.getContent();
            contentText.setText(content);
        }

        @Override
        public NoteDTO getItem() {
            return items.get(getAdapterPosition());
        }
    }
}
