package com.zeus.migue.notes.infrastructure.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zeus.migue.notes.data.room.entities.BaseEntity;

@Dao
public interface BaseDao<T extends BaseEntity> {

    @Insert
    long[] insert(T... data);

    @Insert
    long insert(T data);

    @Update
    void update(T... data);

    @Update
    void update(T data);

    @Delete
    void delete(T... data);

    @Delete
    void delete(T data);
}
