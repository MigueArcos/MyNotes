package com.zeus.migue.notes.data.DTO;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zeus.migue.notes.data.room.entities.ClipNote;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.contracts.JsonConverter;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.Objects;

public class ClipNoteDTO extends JsonConverter implements IEntityConverter<ClipNote>, IFilterable {
    @ColumnInfo(name = "Id")
    private long id;

    @SerializedName("Id")
    @ColumnInfo(name = "RemoteId")
    @Expose
    private String remoteId;
    @SerializedName("Content")
    @ColumnInfo(name = "Content")
    @Expose
    private String content;
    @SerializedName("CreationDate")
    @ColumnInfo(name = "CreationDate")
    @Expose
    private String creationDate;
    @SerializedName("ModificationDate")
    @ColumnInfo(name = "ModificationDate")
    @Expose
    private String modificationDate;
    @ColumnInfo(name = "IsUploaded")
    private boolean isUploaded;

    public ClipNoteDTO() {

    }
    @Ignore
    public ClipNoteDTO(String content, String creationDate, String modificationDate) {
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }


    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }


    @Override
    public ClipNote toEntity() {
        return new ClipNote(id, remoteId, content, creationDate, modificationDate, isUploaded);
    }

    @Override
    public boolean passFilter(String filter) {
        return Utils.stringIsNullOrEmpty(content) && content.contains(filter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, remoteId, content, creationDate, modificationDate, isUploaded);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ClipNoteDTO)) return false;
        ClipNoteDTO source = (ClipNoteDTO) obj;
        return source.id == id && Objects.equals(source.remoteId, remoteId) && Objects.equals(source.content, content) && Objects.equals(source.creationDate, creationDate) && Objects.equals(source.modificationDate, modificationDate) && source.isUploaded == isUploaded;
    }
}
