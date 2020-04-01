package com.zeus.migue.notes.infrastructure.repositories;

import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.dao.NotesDao;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;

public class NotesRepository extends GenericRepository<Note, NoteDTO> {
    private static NotesRepository instance;

    private NotesRepository(NotesDao notesDao, ILogger logger) {
        super(notesDao, logger);
    }

    public static synchronized NotesRepository getInstance(NotesDao notesDao, ILogger logger) {
        if (instance == null) {
            instance = new NotesRepository(notesDao, logger);
        }
        return instance;
    }
}
