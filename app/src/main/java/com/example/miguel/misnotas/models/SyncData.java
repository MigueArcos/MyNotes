package com.example.miguel.misnotas.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by 79812 on 05/03/2018.
 */

public class SyncData {
    private List<Note> newNotes;
    private List<Note> modifiedNotes;
    private List<Integer> idsToDelete;
    private SyncInfo syncInfo;

    public SyncInfo getSyncInfo() {
        return syncInfo;
    }

    public void setSyncInfo(SyncInfo syncInfo) {
        this.syncInfo = syncInfo;
    }

    public List<Note> getNewNotes() {
        return newNotes;
    }

    public void setNewNotes(List<Note> newNotes) {
        this.newNotes = newNotes;
    }

    public List<Note> getModifiedNotes() {
        return modifiedNotes;
    }

    public void setModifiedNotes(List<Note> modifiedNotes) {
        this.modifiedNotes = modifiedNotes.size() > 0 ? modifiedNotes: null;
    }

    public List<Integer> getIdsToDelete() {
        return idsToDelete;
    }

    public void setIdsToDelete(List<Integer> idsToDelete) {
        this.idsToDelete = idsToDelete;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
    public static class SyncInfo{
        private String userId;
        private int lastSyncedId;

        public SyncInfo(String userId, int lastSyncedId) {
            this.userId = userId;
            this.lastSyncedId = lastSyncedId;
        }

        public SyncInfo(int lastSyncedId) {
            this.lastSyncedId = lastSyncedId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getLastSyncedId() {
            return lastSyncedId;
        }

        public void setLastSyncedId(int lastSyncedId) {
            this.lastSyncedId = lastSyncedId;
        }
    }
}
