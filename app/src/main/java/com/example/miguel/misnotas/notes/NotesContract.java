package com.example.miguel.misnotas.notes;

import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.utilities.FilterableAdapterPresenterContract;

/**
 * Created by 79812 on 18/07/2018.
 */

public class NotesContract {

    public interface ItemView{
        void showTitle(String title);
        void showContent(String content);
        void showModificationDate(String modificationDate);
        void showImageResource(int resourceId);
    }

    public interface View{
        void notifyItemCreated();

        void notifyItemCreated(int position);

        void notifyItemUpdated(int position);

        void notifyItemDeleted(int position);

        void notifyChanges();

    }

    public interface Presenter extends FilterableAdapterPresenterContract<Note> {
        void bindHolderData(ItemView itemView, int position);

        void onItemClick(int position);

        void onItemIconClick(int position);

        void onItemSwiped(int position);

        void deleteNote(int noteId, boolean forever);

    }

    public interface Model extends FilterableAdapterPresenterContract<Note> {
        void bindHolderData(ItemView itemView, int position);

        void deleteNote(int noteId, boolean forever);

        Note getItem(int position);

        void recoverNote(int position);

    }
}
