package com.zeus.migue.notes.ui.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;

public class BasicViewModel extends AndroidViewModel {
    protected ILogger logger;
    protected MutableLiveData<LiveDataEvent<Event>> eventData;

    public BasicViewModel(@NonNull Application application) {
        super(application);
        eventData = new MutableLiveData<>();
        logger = Logger.getInstance(application);
    }

    public LiveData<LiveDataEvent<Event>> getEvent() {
        return eventData;
    }
}
