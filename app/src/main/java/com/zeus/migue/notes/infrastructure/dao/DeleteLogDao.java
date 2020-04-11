package com.zeus.migue.notes.infrastructure.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeleteLogDao {
    @Query("DELETE FROM DeleteLog WHERE EntityName = :entityName")
    int deleteAfterSync(String entityName);

    @Query("SELECT RemoteId FROM DeleteLog WHERE EntityName = :entityName")
    List<String> getIDsToDelete(String entityName);
}
