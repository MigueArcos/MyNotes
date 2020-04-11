package com.zeus.migue.notes.data.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "Notes", indices = @Index(value = "RemoteId", unique = true))
public class Note extends BaseEntity{
    @ColumnInfo(name = "Title")
    private String title;
    @ColumnInfo(name = "Content")
    private String content;
    @ColumnInfo(name = "IsDeleted")
    private boolean isDeleted = false;

    public Note() {
    }
    @Ignore
    public Note(long id, String remoteId, String title, String content, String creationDate, String modificationDate, boolean isDeleted, boolean isUploaded) {
        super(id, remoteId, creationDate, modificationDate, isUploaded);
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
