package com.zeus.migue.notes.ui.activity_main.fragments.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.dao.NotesDao;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.network.services.AuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.IAuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.INotesSynchronizer;
import com.zeus.migue.notes.infrastructure.network.services.NotesSynchronizer;
import com.zeus.migue.notes.infrastructure.repositories.NotesRepository;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.List;

public class BaseNotesFragmentViewModel extends AndroidViewModel {
    private NotesRepository notesRepository;

    private MutableLiveData<LiveDataEvent<Event>> eventData;
    private MutableLiveData<String> filterLiveData;
    private LiveData<List<NoteDTO>> notesLiveData;
    public BaseNotesFragmentViewModel(@NonNull Application application, boolean deletedNotes) {
        super(application);

        notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application).notesDao(), Logger.getInstance(application));

        eventData = new MutableLiveData<>();
        filterLiveData = new MutableLiveData<>();
        notesLiveData = Transformations.switchMap(filterLiveData, input -> {
            if (Utils.stringIsNullOrEmpty(input)){
                return ((NotesDao) notesRepository.getDao()).getAllNotes(deletedNotes);
            }
            else {
                return ((NotesDao) notesRepository.getDao()).getNotesByFilter(deletedNotes, "%" + input + "%");
            }
        });
        filterLiveData.setValue(Utils.EMPTY_STRING);
    }


    public LiveData<List<NoteDTO>> getNotes() {
        return notesLiveData;
    }

    public LiveData<LiveDataEvent<Event>> getEventData() {
        return eventData;
    }

    public void filterNotes(String filter){
        filterLiveData.setValue(filter);
    }

    public void updateNote(NoteDTO note){
        try {
            Note n = note.toEntity();
            notesRepository.update(note);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }

    public void deleteNote(NoteDTO note){
        try {
            Note n = note.toEntity();
            notesRepository.delete(note);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }
}
