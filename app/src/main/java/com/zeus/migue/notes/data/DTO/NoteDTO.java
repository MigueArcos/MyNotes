package com.zeus.migue.notes.data.DTO;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.contracts.JsonConverter;

import java.util.Locale;
import java.util.Objects;

public class NoteDTO extends JsonConverter implements IEntityConverter<Note>, IFilterable {
    @ColumnInfo(name = "Id")
    private long id;

    @SerializedName("Id")
    @ColumnInfo(name = "RemoteId")
    @Expose
    private String remoteId;

    @SerializedName("Title")
    @ColumnInfo(name = "Title")
    @Expose
    private String title;
    @SerializedName("CreationDate")
    @ColumnInfo(name = "CreationDate")
    @Expose
    private String creationDate;
    @SerializedName("ModificationDate")
    @ColumnInfo(name = "ModificationDate")
    @Expose
    private String modificationDate;
    @SerializedName("Content")
    @ColumnInfo(name = "Content")
    @Expose
    private String content;
    @SerializedName("IsDeleted")
    @ColumnInfo(name = "IsDeleted")
    @Expose
    private boolean isDeleted;
    @ColumnInfo(name = "IsUploaded")
    private boolean isUploaded;

    public NoteDTO(){

    }
    @Ignore
    public NoteDTO(String title, String content, String creationDate, String modificationDate, boolean isDeleted) {
        this.title = title;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.content = content;
        this.isDeleted = isDeleted;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public Note toEntity(){
        return new Note(id, remoteId, title, content, creationDate, modificationDate, isDeleted, isUploaded);
    }

    @Override
    public boolean passFilter(String filter) {
        return String.format(Locale.US, "%s%s", title, content).contains(filter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, remoteId, title, content, creationDate, modificationDate, isDeleted, isUploaded);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof  NoteDTO)) return false;
        NoteDTO source = (NoteDTO) obj;
        return source.id == id && Objects.equals(source.remoteId, remoteId) && Objects.equals(source.title, title) && Objects.equals(source.content, content) && Objects.equals(source.creationDate, creationDate) && Objects.equals(source.modificationDate, modificationDate) && source.isDeleted == isDeleted && source.isUploaded == isUploaded;
    }
}
