package com.zeus.migue.notes.ui.activity_main.fragments.shared;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BaseNotesFragmentAdapter extends RecyclerView.Adapter<BaseNotesFragmentAdapter.ItemViewHolder> {
    public interface onItemClickListener<T> {
        void onClickItem(T data, int position);
    }

    public interface onItemSwipeListener<T> {
        void onItemSwiped(T data, int position, int swipeDir);
    }


    private List<NoteDTO> notes = new ArrayList<>();
    private onItemClickListener<NoteDTO> onItemClick;
    private onItemSwipeListener<NoteDTO> onItemSwipe;
    private boolean showDeleted;

    public BaseNotesFragmentAdapter(boolean showDeleted) {
        this.showDeleted = showDeleted;
    }

    public boolean shouldShowDeleted() {
        return showDeleted;
    }

    public void setOnItemClick(onItemClickListener<NoteDTO> onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnItemSwipe(onItemSwipeListener<NoteDTO> onItemSwipe) {
        this.onItemSwipe = onItemSwipe;
    }

    public onItemSwipeListener<NoteDTO> getOnItemSwipe() {
        return onItemSwipe;
    }

    public List<NoteDTO> getNotes() {
        return notes;
    }

    public void updateData(List<NoteDTO> notes) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotesDiffCallback(this.notes, notes));
        diffResult.dispatchUpdatesTo(this);
        this.notes = notes;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CustomItemTouchHelperCallback(this, ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_delete_icon), ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_done)));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public BaseNotesFragmentAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new BaseNotesFragmentAdapter.ItemViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseNotesFragmentAdapter.ItemViewHolder holder, int position) {
        holder.setTitle(notes.get(position).getTitle());
        holder.setModificationDate(notes.get(position).getModificationDate());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleText, modificationDateText;

        ItemViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
            modificationDateText = itemView.findViewById(R.id.modification_date_text);
            itemView.setOnClickListener(this);
        }

        public void setTitle(String title) {
            titleText.setText(title);
        }

        public void setModificationDate(String modificationDate) {
            modificationDateText.setText(Utils.niceDateFormat(Utils.fromIso8601(modificationDate, true)));
        }

        @Override
        public void onClick(View v) {
            if (onItemClick != null)
                onItemClick.onClickItem(notes.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public class NotesDiffCallback extends DiffUtil.Callback {
        private List<NoteDTO> mOldList;
        private List<NoteDTO> mNewList;

        public NotesDiffCallback(List<NoteDTO> oldList, List<NoteDTO> newList) {
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
            return mNewList.get(newItemPosition).getId() == mOldList.get(oldItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
        }
    }
}
