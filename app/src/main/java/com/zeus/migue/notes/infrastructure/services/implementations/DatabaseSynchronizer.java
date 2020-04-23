package com.zeus.migue.notes.infrastructure.services.implementations;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zeus.migue.notes.data.DTO.ClipNoteDTO;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.DTO.sync.EntityChanges;
import com.zeus.migue.notes.data.DTO.sync.SyncPayload;
import com.zeus.migue.notes.data.enums.EntityName;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.ClipNote;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.dao.ClipNotesDao;
import com.zeus.migue.notes.infrastructure.dao.NotesDao;
import com.zeus.migue.notes.infrastructure.services.contracts.IDatabaseSynchronizer;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.utils.EntityChangesProcessor;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseSynchronizer implements IDatabaseSynchronizer {
    private AppDatabase appDatabase;
    private ILogger logger;
    private ExecutorService executorService;

    public DatabaseSynchronizer(Context context) {
        appDatabase = AppDatabase.getInstance(context);
        logger = Logger.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }

    public static class ChangeToLog{
        public String source;
        public EntityLog notes;
        public EntityLog clipNotes;
        public static class EntityLog{
            public List<String> toDelete;
            public Integer toAdd;
            public Integer toModify;
        }

    }
    private void cleanPayload(SyncPayload syncPayload){
        if (syncPayload.getNotes() != null){
            int size = syncPayload.getNotes().getChildsSize();
            if (size == 0) syncPayload.setNotes(null);
        }
        if (syncPayload.getClipNotes() != null){
            int size = syncPayload.getClipNotes().getChildsSize();
            if (size == 0) syncPayload.setClipNotes(null);
        }
    }
    private void prettyPrintLog(SyncPayload syncPayload, String source){
        ChangeToLog changeToLog = new ChangeToLog();
        changeToLog.source = source;
        if (syncPayload.getNotes() != null){
            changeToLog.notes = new ChangeToLog.EntityLog();
            changeToLog.notes.toAdd = Utils.getListSize(syncPayload.getNotes().getToAdd());
            changeToLog.notes.toModify = Utils.getListSize(syncPayload.getNotes().getToModify());
            if (!Utils.listIsNullOrEmpty(syncPayload.getNotes().getToDelete())) changeToLog.notes.toDelete = syncPayload.getNotes().getToDelete();
        }
        if (syncPayload.getClipNotes() != null){
            changeToLog.clipNotes = new ChangeToLog.EntityLog();
            changeToLog.clipNotes.toAdd = Utils.getListSize(syncPayload.getClipNotes().getToAdd());
            changeToLog.clipNotes.toModify = Utils.getListSize(syncPayload.getClipNotes().getToModify());
            if (!Utils.listIsNullOrEmpty(syncPayload.getClipNotes().getToDelete())) changeToLog.clipNotes.toDelete = syncPayload.getClipNotes().getToDelete();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        logger.log(gson.toJson(changeToLog));
    }

    @Override
    public SyncPayload buildLocalPayload(String lastSyncDate) {
        Future<SyncPayload> promise = executorService.submit(() -> {
            SyncPayload syncPayload = new SyncPayload();
            EntityChanges<NoteDTO> notesChanges = new EntityChanges<>();
            notesChanges.setToAdd(appDatabase.notesDao().getNewNotes());
            notesChanges.setToModify(appDatabase.notesDao().getModifiedNotes(lastSyncDate));
            notesChanges.setToDelete(appDatabase.deleteLogDao().getIDsToDelete(EntityName.Note.toString()));
            EntityChanges<ClipNoteDTO> clipNotesChanges = new EntityChanges<>();
            clipNotesChanges.setToAdd(appDatabase.clipsDao().getNewClipNotes());
            clipNotesChanges.setToModify(appDatabase.clipsDao().getModifiedClipNotes(lastSyncDate));
            clipNotesChanges.setToDelete(appDatabase.deleteLogDao().getIDsToDelete(EntityName.ClipNote.toString()));
            syncPayload.setLastSync(lastSyncDate);
            syncPayload.setNotes(notesChanges);
            syncPayload.setClipNotes(clipNotesChanges);
            cleanPayload(syncPayload);
            prettyPrintLog(syncPayload, "Local");
            return syncPayload;
        });
        try {
            return promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(InsertDB) Exception occurred: %s", ex.getMessage()));
            return null;
        }
    }

    @Override
    public boolean synchronize(SyncPayload remotePayload) {
        Future<Boolean> promise = executorService.submit(() -> {
            cleanPayload(remotePayload);
            prettyPrintLog(remotePayload, "Remote");

            EntityChangesProcessor<Note, NoteDTO, NotesDao> notesProcessor = new EntityChangesProcessor<>(appDatabase.notesDao());
            notesProcessor.processChanges(remotePayload.getNotes(), stream -> stream.toArray(Note[]::new));
            appDatabase.deleteLogDao().deleteAfterSync(EntityName.Note.toString());

            EntityChangesProcessor<ClipNote, ClipNoteDTO, ClipNotesDao> clipNotesProcessor = new EntityChangesProcessor<>(appDatabase.clipsDao());
            clipNotesProcessor.processChanges(remotePayload.getClipNotes(), stream -> stream.toArray(ClipNote[]::new));
            appDatabase.deleteLogDao().deleteAfterSync(EntityName.ClipNote.toString());

            return true;
        });
        try {
            return promise.get();
        } catch (Exception ex) {
            if (logger != null)
                logger.log(String.format("(InsertDB) Exception occurred: %s", ex.getMessage()));
            return false;
        }
    }
}
