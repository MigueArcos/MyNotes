package com.zeus.migue.notes.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.entities.Note;

import java.util.List;

@Dao
public interface NotesDao extends BaseDao<Note> {
    @Query("SELECT * FROM Notes WHERE IsDeleted=:showDeleted ORDER BY ModificationDate DESC")
    LiveData<List<NoteDTO>> getAllNotes(boolean showDeleted);
    @Query("SELECT * FROM Notes WHERE IsDeleted=:showDeleted AND (Title || Content) LIKE :filter ORDER BY ModificationDate DESC")
    LiveData<List<NoteDTO>> getNotesByFilter(boolean showDeleted, String filter);
}