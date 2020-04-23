package com.zeus.migue.notes.ui.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.sync.SyncPayload;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;
import com.zeus.migue.notes.infrastructure.network.services.AuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.IAuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.ISynchronizer;
import com.zeus.migue.notes.infrastructure.network.services.Synchronizer;
import com.zeus.migue.notes.infrastructure.repositories.GenericRepository;
import com.zeus.migue.notes.infrastructure.services.contracts.IConnectivityChecker;
import com.zeus.migue.notes.infrastructure.services.implementations.ConnectivityChecker;
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
    private MutableLiveData<SyncPayload> networkResponse;
    private ISynchronizer synchronizer;
    private UserPreferences userPreferences;
    private IAuthorizationService authorizationService;
    private IConnectivityChecker connectivityChecker;
    public BaseListViewModel(@NonNull Application application) {
        super(application);
        repository = getRepository(application);
        filterLiveData = new MutableLiveData<>();
        networkResponse = new MutableLiveData<>();
        itemsLiveData = initItemsLiveData();
        filterLiveData.setValue(Utils.EMPTY_STRING);
        synchronizer = new Synchronizer(application);
        userPreferences = UserPreferences.getInstance(application);
        authorizationService = new AuthorizationService(application);
        connectivityChecker = new ConnectivityChecker(application);
    }

    public abstract LiveData<List<DTO>> initItemsLiveData();

    public abstract GenericRepository<Entity, DTO> getRepository(Application application);

    public LiveData<List<DTO>> getItems() {
        return itemsLiveData;
    }

    public LiveData<SyncPayload> getNetworkResponse() {
        return networkResponse;
    }

    public void filterNotes(String filter) {
        filterLiveData.setValue(filter);
    }

    public void startSynchronization() {
        if (!connectivityChecker.isConnectedToInternet()){
            networkResponse.setValue(null);
            eventData.setValue(new LiveDataEvent<>(Event.NO_INTERNET));
            return;
        }
        IResponseListener<SyncPayload> syncSuccessListener = syncPayload -> {
            userPreferences.setLastSyncDate(syncPayload.getLastSync());
            networkResponse.setValue(syncPayload);
        };
        IResponseListener<ErrorCode> syncErrorListener = errorCode -> {
            eventData.setValue(new LiveDataEvent<>(errorCode.toEvent(Event.MessageType.SHOW_IN_DIALOG)));
            networkResponse.setValue(null);
        };
        if (userPreferences.tokenHasExpired()){
            authorizationService.refreshToken(userPreferences.getRefreshToken(), signInResponse -> {
                userPreferences.setAuthInfo(signInResponse, false);
                synchronizer.syncDatabases(userPreferences.getAuthorizationToken(), null, userPreferences.getLastSyncDate(), syncSuccessListener, syncErrorListener);
            }, syncErrorListener);
        }else{
            synchronizer.syncDatabases(userPreferences.getAuthorizationToken(), null, userPreferences.getLastSyncDate(), syncSuccessListener, syncErrorListener);
        }
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
