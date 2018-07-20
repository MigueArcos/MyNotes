package com.example.miguel.misnotas.notes;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.utilities.FilterableAdapterModel;

/**
 * Created by 79812 on 18/07/2018.
 */

public class NotesModel extends FilterableAdapterModel<Note> implements NotesContract.Model {
    private NotesContract.Presenter presenter;
    private boolean deletedNotes;
    public NotesModel(NotesContract.Presenter presenter, boolean deletedNotes) {
        super(Database.getInstance().getNotes(deletedNotes));
        this.deletedNotes = deletedNotes;
        this.presenter = presenter;
    }

    @Override
    public void bindHolderData(NotesContract.ItemView itemView, int position) {
        itemView.showTitle(currentData.get(position).getTitle());
        itemView.showModificationDate(String.format("Última modificación: %s", MyUtils.getTime12HoursFormat(currentData.get(position).getModificationDate())));
        itemView.showImageResource(Note.imageId);
    }

    @Override
    public void deleteNote(int noteId, boolean forever) {
        if (forever){
            Database.getInstance().deleteNoteCompletely(noteId);
        }
        else{
            Database.getInstance().deleteNote(noteId);
        }
    }

    @Override
    public Note getItem(int position) {
        return currentData.get(position);
    }

    @Override
    public void reloadData() {
       loadFromDataSource(Database.getInstance().getNotes(deletedNotes));
    }


    @Override
    public void recoverNote(int position) {
        Database.getInstance().recoverNote(currentData.get(position).getNoteId());
    }



}
