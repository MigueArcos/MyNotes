package com.zeus.migue.notes.ui.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.BaseEntity;
import com.zeus.migue.notes.infrastructure.contracts.IEntityConverter;
import com.zeus.migue.notes.infrastructure.contracts.IFilterable;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.repositories.GenericRepository;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.repositories.NotesRepository;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.List;

public abstract class BaseListViewModel<Entity extends BaseEntity, DTO extends IFilterable & IEntityConverter<Entity>> extends AndroidViewModel {
    protected GenericRepository<Entity, DTO> repository;
    protected MutableLiveData<LiveDataEvent<Event>> eventData;
    protected MutableLiveData<String> filterLiveData;
    protected LiveData<List<DTO>> itemsLiveData;
    public BaseListViewModel(@NonNull Application application) {
        super(application);
        repository = getRepository(application);
        eventData = new MutableLiveData<>();
        filterLiveData = new MutableLiveData<>();
        itemsLiveData = initItemsLiveData();
        filterLiveData.setValue(Utils.EMPTY_STRING);
    }

    public abstract LiveData<List<DTO>> initItemsLiveData();
    public abstract GenericRepository<Entity, DTO> getRepository(Application application);

    public LiveData<List<DTO>> getItems() {
        return itemsLiveData;
    }

    public LiveData<LiveDataEvent<Event>> getEventData() {
        return eventData;
    }

    public void filterNotes(String filter){
        filterLiveData.setValue(filter);
    }

    public void updateItem(DTO dto){
        try {
            repository.update(dto);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }

    public void deleteItem(DTO dto){
        try {
            repository.delete(dto);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }
}
