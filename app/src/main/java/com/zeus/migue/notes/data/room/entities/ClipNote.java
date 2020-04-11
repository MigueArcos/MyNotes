package com.zeus.migue.notes.data.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "ClipNotes")
public class ClipNote extends BaseEntity{
    @ColumnInfo(name = "Content")
    private String content;

    public ClipNote() {
    }
    @Ignore
    public ClipNote(long id, String remoteId, String content, String creationDate, String modificationDate, boolean isUploaded) {
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
