package com.zeus.migue.notes.ui.shared.recyclerview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.data.DTO.sync.SyncPayload;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.network.services.ISynchronizer;
import com.zeus.migue.notes.infrastructure.network.services.Synchronizer;
import com.zeus.migue.notes.infrastructure.repositories.GenericRepository;
import com.zeus.migue.notes.infrastructure.services.implementations.UserPreferences;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.BasicViewModel;

import java.util.List;

public abstract class BaseListViewModel<Entity extends BaseEntity, DTO extends IFilterable & IEntityConverter<Entity>> extends BasicViewModel {
    protected GenericRepository<Entity, DTO> repository;
    protected MutableLiveData<String> filterLiveData;
    private LiveData<List<DTO>> itemsLiveData;
    private MutableLiveData<SyncPayload> syncResponse;
    private ISynchronizer synchronizer;
    private UserPreferences userPreferences;

    public BaseListViewModel(@NonNull Application application) {
        super(application);
        repository = getRepository(application);
        filterLiveData = new MutableLiveData<>();
        syncResponse = new MutableLiveData<>();
        itemsLiveData = initItemsLiveData();
        filterLiveData.setValue(Utils.EMPTY_STRING);
        synchronizer = new Synchronizer(application);
        userPreferences = UserPreferences.getInstance(application);
    }

    public abstract LiveData<List<DTO>> initItemsLiveData();

    public abstract GenericRepository<Entity, DTO> getRepository(Application application);

    public LiveData<List<DTO>> getItems() {
        return itemsLiveData;
    }

    public LiveData<SyncPayload> getSyncResponse() {
        return syncResponse;
    }

    public void filterNotes(String filter) {
        filterLiveData.setValue(filter);
    }

    public void startSynchronization() {
        synchronizer.syncDatabases(userPreferences.getAuthorizationToken(), userPreferences.getRefreshToken(), userPreferences.getLastSyncDate(), syncPayload -> {
            userPreferences.setLastSyncDate(syncPayload.getLastSync());
            syncResponse.setValue(syncPayload);
        }, errorCode -> {
            eventData.setValue(new LiveDataEvent<>(errorCode.toEvent(Event.MessageType.SHOW_IN_DIALOG)));
            syncResponse.setValue(null);
        });
    }

    public void deleteItem(DTO dto) {
        try {
            repository.delete(dto);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }

    public void updateItem(DTO dto) {
        try {
            repository.update(dto);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }

    public long insertItem(DTO dto) {
        try {
            return repository.insert(dto);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
            return -1;
        }
    }
}
