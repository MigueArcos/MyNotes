package com.zeus.migue.notes.infrastructure.services.implementations;

import android.content.Context;

import com.zeus.migue.notes.data.DTO.ClipItemDTO;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.DTO.sync.EntityChanges;
import com.zeus.migue.notes.data.DTO.sync.SyncPayload;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.data.room.entities.ClipItem;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.services.contracts.IDatabaseSynchronizer;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

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

    @Override
    public SyncPayload buildLocalPayload(String lastSyncDate) {
        Future<SyncPayload> promise = executorService.submit(() -> {
            SyncPayload syncPayload = new SyncPayload();
            EntityChanges<NoteDTO> notesChanges = new EntityChanges<>();
            notesChanges.setToAdd(appDatabase.notesDao().getNewNotes());
            notesChanges.setToModify(appDatabase.notesDao().getModifiedNotes(lastSyncDate));
            EntityChanges<ClipItemDTO> clipNotesChanges = new EntityChanges<>();
            clipNotesChanges.setToAdd(appDatabase.clipsDao().getNewClipNotes());
            clipNotesChanges.setToModify(appDatabase.clipsDao().getModifiedClipNotes(lastSyncDate));
            syncPayload.setLastSync(lastSyncDate);
            syncPayload.setNotes(notesChanges);
            syncPayload.setClipNotes(clipNotesChanges);
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
            EntityChanges<NoteDTO> notesChanges = remotePayload.getNotes();
            if (notesChanges != null) {
                List<NoteDTO> toAdd = notesChanges.getToAdd();
                List<NoteDTO> toModify = notesChanges.getToModify();
                if (toAdd != null) {
                    appDatabase.notesDao().deleteUnsynced();
                    Note[] notesToAdd = toAdd.stream().map(getMapper()).toArray(Note[]::new);
                    appDatabase.notesDao().insert(notesToAdd);
                }
                if (toModify != null) {
                    Note[] notesToModify = toModify.stream().map(getMapper()).toArray(Note[]::new);
                    appDatabase.notesDao().update(notesToModify);
                }
            }
            EntityChanges<ClipItemDTO> clipNotesChanges = remotePayload.getClipNotes();
            if (clipNotesChanges != null) {
                List<ClipItemDTO> toAdd = clipNotesChanges.getToAdd();
                List<ClipItemDTO> toModify = clipNotesChanges.getToModify();
                if (toAdd != null) {
                    appDatabase.clipsDao().deleteUnsynced();
                    ClipItem[] notesToAdd = toAdd.stream().map(getMapper()).toArray(ClipItem[]::new);
                    appDatabase.clipsDao().insert(notesToAdd);
                }
                if (toModify != null) {
                    ClipItem[] notesToModify = toModify.stream().map(getMapper()).toArray(ClipItem[]::new);
                    appDatabase.clipsDao().update(notesToModify);
                }
            }
            //String[] notesToDelete = remotePayload.getNotes().getToDelete().toArray(new String[0]);

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
