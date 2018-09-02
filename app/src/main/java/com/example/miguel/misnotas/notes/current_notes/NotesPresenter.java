package com.example.miguel.misnotas.notes.current_notes;

import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.notes.NotesContract;
import com.example.miguel.misnotas.notes.NotesModel;

import java.util.List;

/**
 * Created by 79812 on 18/07/2018.
 */

public class NotesPresenter implements NotesContract.Presenter {
    private NotesContract.View view;
    private NotesContract.Model model;

    public NotesPresenter(NotesContract.View view, boolean deletedNotes) {
        this.view = view;
        model = new NotesModel(this, deletedNotes);
    }

    @Override
    public void bindHolderData(NotesContract.ItemView itemView, int position) {
        model.bindHolderData(itemView, position);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemIconClick(int position) {

    }


    @Override
    public void onItemSwiped(int position) {
        model.deleteItem(position, false);
        view.notifyItemDeleted(position);
    }

    @Override
    public int getItemCount() {
        return model.getItemCount();
    }

    @Override
    public List<Note> getCurrentData() {
        return model.getCurrentData();
    }

    @Override
    public void addItem(Note item) {
        model.addItem(item, 0);
        view.notifyItemCreated(0);
    }

    @Override
    public void addItem(Note item, int position) {
        model.addItem(item, position);
        view.notifyItemCreated(position);
    }

    @Override
    public void deleteItem(int position, boolean forever) {
        model.deleteItem(position, forever);
        view.notifyItemDeleted(position);
    }

    @Override
    public void deleteItemForever(Note item) {
        model.deleteItemForever(item);
    }

    @Override
    public void updateItem(Note newItem, int position) {
        model.updateItem(newItem, position);
        //By default all updated items gets the position 0 in list because they are the newest updated item
        view.notifyItemDeleted(position);
        view.notifyItemCreated(0);
    }

    @Override
    public void deleteNote(int noteId, boolean forever) {
        //TODO: after delete note we must delete the note item from original data since we didn't do it before because it could have been recovered
        model.deleteNote(noteId, forever);
    }


    @Override
    public void filterResults(String filter) {
        model.filterResults(filter);
        view.notifyChanges();
    }

    @Override
    public void cancelItemDeletion(Note selectedItem, int position) {
        model.cancelItemDeletion(selectedItem, position);
        view.notifyItemCreated(position);
    }

    @Override
    public Note getItem(int position) {
        return model.getItem(position);
    }

    @Override
    public void reloadData() {
        model.reloadData();
        view.notifyChanges();
    }
}
