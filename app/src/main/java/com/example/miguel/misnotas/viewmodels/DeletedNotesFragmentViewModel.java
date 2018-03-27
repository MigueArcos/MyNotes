package com.example.miguel.misnotas.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.models.Note;

import java.util.List;

/**
 * Created by Miguel Ángel López Arcos on 02/02/2018.
 * Copyright © 2018 Mezcal Development. All rights reserved.
 */

public class DeletedNotesFragmentViewModel extends AndroidViewModel {
    private List<Note> notesList;

    public DeletedNotesFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Note> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<Note> notesList) {
        this.notesList = notesList;
    }


}
