package com.zeus.migue.notes.ui.activity_notes_editor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.repositories.NotesRepository;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;
import com.zeus.migue.notes.ui.shared.BasicViewModel;

public class NotesEditorViewModel extends BasicViewModel {
    private NotesRepository notesRepository;
    public NotesEditorViewModel(@NonNull Application application) {
        super(application);
        notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application).notesDao(), logger);
    }
    public LiveData<LiveDataEvent<Event>> getEvent() {
        return eventData;
    }

    public long insertNote(NoteDTO note) {
        try {
            return notesRepository.insert(note);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
            return 0;
        }
    }
    public void updateNote(NoteDTO note){
        try {
            notesRepository.update(note);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }
}
