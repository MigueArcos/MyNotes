package com.zeus.migue.notes.data.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "ClipItems", indices = @Index(value = "RemoteId", unique = true))
public class ClipItem extends BaseEntity{
    @ColumnInfo(name = "Content")
    private String content;

    public ClipItem() {
    }

    public ClipItem(long id, String remoteId, String content, String creationDate, String modificationDate,  boolean isUploaded) {
        super(id, remoteId, creationDate, modificationDate, isUploaded);
        this.content = content;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }
}
