package com.zeus.migue.notes.ui.activity_main.fragments.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.dao.NotesDao;
import com.zeus.migue.notes.infrastructure.repositories.NotesRepository;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.BaseListViewModel;

import java.util.List;

public class NotesViewModel extends BaseListViewModel<Note, NoteDTO> {
    private boolean showDeletedNotes;

    public NotesViewModel(@NonNull Application application, boolean showDeletedNotes) {
        super(application);
        this.showDeletedNotes = showDeletedNotes;
    }

    @Override
    public LiveData<List<NoteDTO>> initItemsLiveData() {
        return Transformations.switchMap(filterLiveData, input -> {
            if (Utils.stringIsNullOrEmpty(input)) {
                return ((NotesDao) repository.getDao()).getAllNotes(showDeletedNotes);
            } else {
                return ((NotesDao) repository.getDao()).getNotesByFilter(showDeletedNotes, "%" + input + "%");
            }
        });
    }

    @Override
    public NotesRepository getRepository(Application application) {
        return NotesRepository.getInstance(AppDatabase.getInstance(application).notesDao(), Logger.getInstance(application));
    }
}
