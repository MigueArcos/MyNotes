package com.zeus.migue.notes.data.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BaseEntity  {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private long id;
    @ColumnInfo(name = "RemoteId")
    private String remoteId = "";
    @ColumnInfo(name = "CreationDate")
    private String creationDate;
    @ColumnInfo(name = "ModificationDate")
    private String modificationDate;
    @ColumnInfo(name = "IsUploaded")
    private boolean isUploaded = true;

    public BaseEntity() {
    }


    public BaseEntity(long id, String remoteId, String creationDate, String modificationDate, boolean isUploaded) {
        this.id = id;
        this.remoteId = remoteId;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.isUploaded = isUploaded;
    }

    public long getId() {
        return id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public void setIsUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }
}
