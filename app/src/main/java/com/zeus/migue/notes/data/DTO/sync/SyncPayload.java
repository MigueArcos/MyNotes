package com.zeus.migue.notes.data.DTO.sync;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.contracts.JsonConverter;

public class SyncPayload extends JsonConverter {
    @SerializedName("Notes")
    @Expose
    private EntityChanges<NoteDTO> notes;
    @SerializedName("ClipNotes")
    @Expose
    private EntityChanges<ClipItemDTO> clipNotes;
    @SerializedName("LastSync")
    @Expose
    private String lastSync;

    public SyncPayload() {
    }

    public EntityChanges<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(EntityChanges<NoteDTO> notes) {
        this.notes = notes;
    }

    public EntityChanges<ClipItemDTO> getClipNotes() {
        return clipNotes;
    }

    public void setClipNotes(EntityChanges<ClipItemDTO> clipNotes) {
        this.clipNotes = clipNotes;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }
}
