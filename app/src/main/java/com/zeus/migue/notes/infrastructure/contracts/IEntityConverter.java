package com.zeus.migue.notes.infrastructure.contracts;

import com.zeus.migue.notes.data.room.entities.BaseEntity;

public interface IEntityConverter<T extends BaseEntity> {
    T toEntity();
}
