package com.zeus.migue.notes.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.composite_entities.EntityIDs;
import com.zeus.migue.notes.data.room.entities.Note;

import java.util.List;

@Dao
public interface NotesDao extends BaseDao<Note> {
    @Query("SELECT * FROM Notes WHERE IsDeleted=:showDeleted ORDER BY ModificationDate DESC")
    LiveData<List<NoteDTO>> getAllNotes(boolean showDeleted);
    @Query("SELECT * FROM Notes WHERE IsDeleted=:showDeleted AND (Title || Content) LIKE :filter ORDER BY ModificationDate DESC")
    LiveData<List<NoteDTO>> getNotesByFilter(boolean showDeleted, String filter);
    @Query("SELECT * FROM Notes WHERE IsUploaded = 0 ORDER BY CreationDate DESC")
    List<NoteDTO> getNewNotes();
    @Query("SELECT * FROM Notes WHERE ModificationDate > :lastSync AND IsUploaded = 1 ORDER BY CreationDate DESC")
    List<NoteDTO> getModifiedNotes(String lastSync);
    @Query("DELETE FROM Notes WHERE IsUploaded = 0")
    int deleteUnsynced();
    @Query("SELECT Id, RemoteId FROM Notes WHERE IsUploaded = 1")
    List<EntityIDs> getIDs();
}