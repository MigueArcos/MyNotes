package com.zeus.migue.notes.ui.activity_main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.infrastructure.services.implementations.UserPreferences;
import com.zeus.migue.notes.infrastructure.utils.Utils;

public class MainActivityViewModel extends AndroidViewModel {
    private UserPreferences userPreferences;
    private MutableLiveData<MinimalUserInfo> userIsLoggedIn;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        userPreferences = UserPreferences.getInstance(application);
        userIsLoggedIn = new MutableLiveData<>();
        userIsLoggedIn.setValue(new MinimalUserInfo(userPreferences.userIsAuthenticated(), userPreferences.getName(), userPreferences.getEmail()));
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public LiveData<MinimalUserInfo> getUserInformation() {
        return userIsLoggedIn;
    }

    public void logout(){
        userPreferences.deleteAll();
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
