package com.zeus.migue.notes.ui.activity_main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.infrastructure.broadcast_services.AutomaticSyncEnabler;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.services.implementations.UserPreferences;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.BasicViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivityViewModel extends BasicViewModel {
    private UserPreferences userPreferences;
    private MutableLiveData<MinimalUserInfo> userIsLoggedIn;
    private AppDatabase appDatabase;
    private AutomaticSyncEnabler syncEnabler;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        userPreferences = UserPreferences.getInstance(application);
        userIsLoggedIn = new MutableLiveData<>();
        userIsLoggedIn.setValue(new MinimalUserInfo(userPreferences.userIsAuthenticated(), userPreferences.getName(), userPreferences.getEmail()));
        appDatabase = AppDatabase.getInstance(application);
        syncEnabler = AutomaticSyncEnabler.getInstance(application);
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public LiveData<MinimalUserInfo> getUserInformation() {
        return userIsLoggedIn;
    }

    public void logout(){
        userPreferences.deleteAll();
        try {
            Future promise = Executors.newSingleThreadExecutor().submit(() -> {
                appDatabase.notesDao().deleteUploaded(true);
                appDatabase.clipsDao().deleteUploaded(true);
                appDatabase.deleteLogDao().deleteAll();
            });
            promise.get();
        }  catch (Exception e) {
            logger.log("(Close session) " + e.getMessage());
        }
        syncEnabler.disableAutomaticSync();
        userIsLoggedIn.setValue(new MinimalUserInfo(false, Utils.EMPTY_STRING, Utils.EMPTY_STRING));
    }

    public class MinimalUserInfo{
        private boolean isLoggedIn;
        private String userName;
        private String email;

        public MinimalUserInfo(boolean isLoggedIn, String userName, String email) {
            this.isLoggedIn = isLoggedIn;
            this.userName = userName;
            this.email = email;
        }

        public boolean isLoggedIn() {
            return isLoggedIn;
        }

        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }
    }
}
