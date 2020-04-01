package com.zeus.migue.notes.infrastructure.network.services;

import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.DTO.SyncRequest;
import com.zeus.migue.notes.data.DTO.SyncResponse;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;

import java.util.List;

public interface INotesSynchronizer {
    void syncDatabases(SyncRequest syncRequest, IResponseListener<SyncResponse> successListener, IResponseListener<ErrorCode> errorListener);
    void getUserNotes(String token, IResponseListener<List<NoteDTO>> successListener, IResponseListener<ErrorCode> errorListener);
}
