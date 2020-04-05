package com.zeus.migue.notes.infrastructure.repositories;

import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.data.room.entities.ClipItem;
import com.zeus.migue.notes.infrastructure.dao.ClipsDao;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;

public class ClipsRepository extends GenericRepository<ClipItem, ClipItemDTO> {
    private static ClipsRepository instance;

    private ClipsRepository(ClipsDao clipsDao, ILogger logger) {
        super(clipsDao, logger);
    }

    public static synchronized ClipsRepository getInstance(ClipsDao clipsDao, ILogger logger) {
        if (instance == null) {
            instance = new ClipsRepository(clipsDao, logger);
        }
        return instance;
    }
}
