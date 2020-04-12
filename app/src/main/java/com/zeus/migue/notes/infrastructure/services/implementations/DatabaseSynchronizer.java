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
import com.zeus.migue.notes.data.room.composite_entities.EntityIDs;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.data.room.entities.ClipNote;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.services.contracts.IDatabaseSynchronizer;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DatabaseSynchronizer implements IDatabaseSynchronizer {
    private AppDatabase appDatabase;
    private ILogger logger;
    private ExecutorService executorService;

    public DatabaseSynchronizer(Context context) {
        appDatabase = AppDatabase.getInstance(context);
        logger = Logger.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }

    private <Entity extends BaseEntity, DTO extends IEntityConverter<Entity>> Function<DTO, Entity> getMapper() {
        return dto -> {
            Entity entity = dto.toEntity();
            entity.setIsUploaded(true);
            return entity;
        };
    }
    @SuppressWarnings("ConstantConditions")
    private <Entity extends BaseEntity, DTO extends IEntityConverter<Entity>> Function<DTO, Entity> getMapperModifications(Map<String, Long> idsMap) {
        return dto -> {
            Entity entity = dto.toEntity();
            entity.setIsUploaded(true);
            if (Utils.stringIsNullOrEmpty(entity.getRemoteId()) && idsMap.containsKey(entity.getRemoteId())){
                entity.setId(idsMap.get(entity.getRemoteId()));
            }
            return entity;
        };
    }
    public static class ChangeToLog{
        public String source;
        public EntityLog notes;
        public EntityLog clipNotes;
        public static class EntityLog{
            public List<String> toDelete;
            public int toAdd;
            public int toModify;
        }
    }
    public void prettyPrintLog(SyncPayload syncPayload, String source){
        ChangeToLog changeToLog = new ChangeToLog();
        changeToLog.source = source;
        if (syncPayload.getNotes() != null){
            changeToLog.notes = new ChangeToLog.EntityLog();
            if (syncPayload.getNotes().getToAdd() != null) changeToLog.notes.toAdd = syncPayload.getNotes().getToAdd().size();
            if (syncPayload.getNotes().getToModify() != null) changeToLog.notes.toModify = syncPayload.getNotes().getToModify().size();
            if (syncPayload.getNotes().getToDelete() != null) changeToLog.notes.toDelete = syncPayload.getNotes().getToDelete();
        }
        if (syncPayload.getClipNotes() != null){
            changeToLog.clipNotes = new ChangeToLog.EntityLog();
            if (syncPayload.getClipNotes().getToAdd() != null) changeToLog.clipNotes.toAdd = syncPayload.getClipNotes().getToAdd().size();
            if (syncPayload.getClipNotes().getToModify() != null) changeToLog.clipNotes.toModify = syncPayload.getClipNotes().getToModify().size();
            if (syncPayload.getClipNotes().getToDelete() != null) changeToLog.clipNotes.toDelete = syncPayload.getClipNotes().getToDelete();
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
    /*
    private void addSafeNavigation(SyncPayload syncPayload){
        if (syncPayload.getNotes() == null) syncPayload.setNotes(new EntityChanges<>());
        if (Utils.listIsNullOrEmpty(syncPayload.getNotes().getToAdd())) syncPayload.getNotes().setToAdd(new ArrayList<>());
        if (Utils.listIsNullOrEmpty(syncPayload.getNotes().getToModify())) syncPayload.getNotes().setToModify(new ArrayList<>());
        if (Utils.listIsNullOrEmpty(syncPayload.getNotes().getToDelete())) syncPayload.getNotes().setToDelete(new ArrayList<>());
        if (syncPayload.getClipNotes() == null) syncPayload.setClipNotes(new EntityChanges<>());
        if (Utils.listIsNullOrEmpty(syncPayload.getClipNotes().getToAdd())) syncPayload.getClipNotes().setToAdd(new ArrayList<>());
        if (Utils.listIsNullOrEmpty(syncPayload.getClipNotes().getToModify())) syncPayload.getClipNotes().setToModify(new ArrayList<>());
        if (Utils.listIsNullOrEmpty(syncPayload.getClipNotes().getToDelete())) syncPayload.getClipNotes().setToDelete(new ArrayList<>());
    }
    */
    @Override
    public boolean synchronize(SyncPayload remotePayload) {
        Future<Boolean> promise = executorService.submit(() -> {
            prettyPrintLog(remotePayload, "Remote");
            EntityChanges<NoteDTO> notesChanges = remotePayload.getNotes();
            if (notesChanges != null) {
                List<NoteDTO> toAdd = notesChanges.getToAdd();
                List<NoteDTO> toModify = notesChanges.getToModify();
                if (toAdd != null) {
                    appDatabase.notesDao().deleteUploaded(false);
                    Note[] notesToAdd = toAdd.stream().map(getMapper()).toArray(Note[]::new);
                    appDatabase.notesDao().insert(notesToAdd);
                }
                if (toModify != null) {
                    Map<String, Long> idsMap = appDatabase.notesDao().getIDs().stream().collect(Collectors.toMap(EntityIDs::getRemoteId, EntityIDs::getId));
                    Note[] notesToModify = toModify.stream().map(getMapperModifications(idsMap)).toArray(Note[]::new);
                    appDatabase.notesDao().update(notesToModify);
                }
            }
            appDatabase.deleteLogDao().deleteAfterSync(EntityName.Note.toString());

            EntityChanges<ClipNoteDTO> clipNotesChanges = remotePayload.getClipNotes();
            if (clipNotesChanges != null) {
                List<ClipNoteDTO> toAdd = clipNotesChanges.getToAdd();
                List<ClipNoteDTO> toModify = clipNotesChanges.getToModify();
                if (toAdd != null) {
                    appDatabase.clipsDao().deleteUploaded(false);
                    ClipNote[] notesToAdd = toAdd.stream().map(getMapper()).toArray(ClipNote[]::new);
                    appDatabase.clipsDao().insert(notesToAdd);
                }
                if (toModify != null) {
                    Map<String, Long> idsMap = appDatabase.notesDao().getIDs().stream().collect(Collectors.toMap(EntityIDs::getRemoteId, EntityIDs::getId));
                    ClipNote[] notesToModify = toModify.stream().map(getMapperModifications(idsMap)).toArray(ClipNote[]::new);
                    appDatabase.clipsDao().update(notesToModify);
                }
            }
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
