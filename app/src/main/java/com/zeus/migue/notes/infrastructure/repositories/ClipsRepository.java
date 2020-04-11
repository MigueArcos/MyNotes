package com.zeus.migue.notes.infrastructure.repositories;

import com.zeus.migue.notes.data.DTO.ClipNoteDTO;
import com.zeus.migue.notes.data.room.entities.ClipNote;
import com.zeus.migue.notes.infrastructure.dao.ClipNotesDao;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;

public class ClipsRepository extends GenericRepository<ClipNote, ClipNoteDTO> {
    private static ClipsRepository instance;

    private ClipsRepository(ClipNotesDao clipsDao, ILogger logger) {
        super(clipsDao, logger);
    }

    public static synchronized ClipsRepository getInstance(ClipNotesDao clipsDao, ILogger logger) {
        if (instance == null) {
            instance = new ClipsRepository(clipsDao, logger);
        }
        return instance;
    }
}
