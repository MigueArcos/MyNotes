package com.zeus.migue.notes.infrastructure.network.services;

import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.DTO.sync.SyncPayload;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;

import java.util.List;

public interface ISynchronizer {
    void syncDatabases(String token, String refreshToken, String lastSyncDate, IResponseListener<SyncPayload> successListener, IResponseListener<ErrorCode> errorListener);
}
