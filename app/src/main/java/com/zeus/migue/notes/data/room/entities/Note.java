package com.zeus.migue.notes.data.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "Notes", indices = @Index(value = "RemoteId", unique = true))
public class Note extends BaseEntity{
    @ColumnInfo(name = "Title")
    private String title;
    @ColumnInfo(name = "Content")
    private String content;
    @ColumnInfo(name = "IsDeleted")
    private boolean isDeleted = false;
    @ColumnInfo(name = "IsModified")
    private boolean isModified = false;

    public Note() {
    }

    public Note(long id, String remoteId, String title, String content, String creationDate, String modificationDate, boolean isDeleted, boolean isModified, boolean isUploaded) {
        super(id, remoteId, creationDate, modificationDate, isUploaded);
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
        this.isModified = isModified;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public boolean getIsModified() {
        return isModified;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setIsModified(boolean isModified) {
        this.isModified = isModified;
    }
}
