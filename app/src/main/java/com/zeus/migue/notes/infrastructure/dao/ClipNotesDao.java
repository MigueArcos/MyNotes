package com.zeus.migue.notes.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.zeus.migue.notes.data.DTO.ClipNoteDTO;
import com.zeus.migue.notes.data.room.composite_entities.EntityIDs;
import com.zeus.migue.notes.data.room.entities.ClipNote;

import java.util.List;
@Dao
public interface ClipNotesDao extends BaseDao<ClipNote> {
    @Query("SELECT * FROM ClipNotes ORDER BY ModificationDate DESC")
    LiveData<List<ClipNoteDTO>> getAllClips();

    @Query("SELECT * FROM ClipNotes WHERE Content LIKE :filter ORDER BY ModificationDate DESC")
    LiveData<List<ClipNoteDTO>> getClipsByFilter(String filter);

    @Query("DELETE FROM ClipNotes WHERE Id IN (SELECT Id FROM ClipNotes ORDER BY ModificationDate DESC LIMIT -1 OFFSET :limit)")
    int deleteAfterLimit(int limit);

    @Query("DELETE FROM ClipNotes WHERE Content = :content")
    int deleteRepeated(String content);

    @Query("SELECT * FROM ClipNotes WHERE IsUploaded = 0 ORDER BY CreationDate DESC")
    List<ClipNoteDTO> getNewClipNotes();
    @Query("SELECT * FROM ClipNotes WHERE ModificationDate > :lastSync AND IsUploaded = 1 ORDER BY CreationDate DESC")
    List<ClipNoteDTO> getModifiedClipNotes(String lastSync);
    @Query("DELETE FROM ClipNotes WHERE IsUploaded = :isUploaded")
    int deleteUploaded(boolean isUploaded);

    @Query("SELECT Id, RemoteId FROM ClipNotes WHERE IsUploaded = 1")
    List<EntityIDs> getIDs();
}
