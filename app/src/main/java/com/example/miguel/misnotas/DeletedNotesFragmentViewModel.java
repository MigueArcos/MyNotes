package com.example.miguel.misnotas;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.miguel.misnotas.Clases_Lista.Elemento_Nota;

import java.util.List;

/**
 * Created by Miguel Ángel López Arcos on 02/02/2018.
 * Copyright © 2018 Mezcal Development. All rights reserved.
 */

public class DeletedNotesFragmentViewModel extends AndroidViewModel {
    private List<Elemento_Nota> notesList;

    public DeletedNotesFragmentViewModel(@NonNull Application application) {
        super(application);
        notesList = Database.getInstance(getApplication()).leer_notas("SELECT * FROM notas WHERE eliminado='S' ORDER BY fecha_modificacion_orden DESC");
    }

    public List<Elemento_Nota> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<Elemento_Nota> notesList) {
        this.notesList = notesList;
    }


}
