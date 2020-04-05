package com.zeus.migue.notes.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.data.room.entities.ClipItem;

import java.util.List;
@Dao
public interface ClipsDao extends BaseDao<ClipItem> {
    @Query("SELECT * FROM ClipItems ORDER BY ModificationDate DESC")
    LiveData<List<ClipItemDTO>> getAllClips();

    @Query("SELECT * FROM ClipItems WHERE Content LIKE :filter ORDER BY ModificationDate DESC")
    LiveData<List<ClipItemDTO>> getClipsByFilter(String filter);
}
