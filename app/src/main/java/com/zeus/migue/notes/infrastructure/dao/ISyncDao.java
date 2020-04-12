package com.zeus.migue.notes.infrastructure.dao;

import com.zeus.migue.notes.data.room.composite_entities.EntityIDs;

import java.util.List;

public interface ISyncDao {
    List<EntityIDs> getIDs();
    int deleteUploaded(boolean isUploaded);
}
