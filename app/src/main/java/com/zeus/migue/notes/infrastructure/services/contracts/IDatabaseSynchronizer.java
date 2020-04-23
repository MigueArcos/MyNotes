package com.zeus.migue.notes.infrastructure.services.contracts;

import com.zeus.migue.notes.data.DTO.sync.SyncPayload;

public interface IDatabaseSynchronizer {
    SyncPayload buildLocalPayload(String lastSyncDate);
    boolean synchronize(SyncPayload remotePayload);
}
