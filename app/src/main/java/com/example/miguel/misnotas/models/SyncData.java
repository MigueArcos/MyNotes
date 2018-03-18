package com.example.miguel.misnotas.models;

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

    public static class SyncInfo{
        private int userId;
        private int lastSyncedId;

        public SyncInfo(int userId, int lastSyncedId) {
            this.userId = userId;
            this.lastSyncedId = lastSyncedId;
        }

        public SyncInfo(int lastSyncedId) {
            this.lastSyncedId = lastSyncedId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
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
