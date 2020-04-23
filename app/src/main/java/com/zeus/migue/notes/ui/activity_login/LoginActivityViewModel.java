package com.zeus.migue.notes.ui.activity_login;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.SignInResponse;
import com.zeus.migue.notes.infrastructure.broadcast_services.AutomaticSyncEnabler;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;
import com.zeus.migue.notes.infrastructure.network.services.AuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.IAuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.ISynchronizer;
import com.zeus.migue.notes.infrastructure.network.services.Synchronizer;
import com.zeus.migue.notes.infrastructure.services.contracts.IConnectivityChecker;
import com.zeus.migue.notes.infrastructure.services.implementations.ConnectivityChecker;
import com.zeus.migue.notes.infrastructure.services.implementations.UserPreferences;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;
import com.zeus.migue.notes.ui.shared.BasicViewModel;

public class LoginActivityViewModel extends BasicViewModel {
    private SignInFragmentState signInFragmentState;
    private SignUpFragmentState signUpFragmentState;
    private boolean isSignInFragment;
    private MutableLiveData<Integer> networkResponse;
    private ISynchronizer synchronizer;
    private IAuthorizationService authorizationService;
    private UserPreferences userPreferences;
    private AutomaticSyncEnabler syncEnabler;
    private IConnectivityChecker connectivityChecker;
    public static final int RESPONSE_ERROR = 0;
    public static final int SYNC_SUCCESS = 1;
    public static final int LOGIN_SUCCESS = 2;

    public LoginActivityViewModel(@NonNull Application application){
        super(application);
        networkResponse = new MutableLiveData<>();
        isSignInFragment = true;
        signInFragmentState = new SignInFragmentState();
        signUpFragmentState = new SignUpFragmentState();
        synchronizer = new Synchronizer(application);
        authorizationService = new AuthorizationService(application);
        userPreferences = UserPreferences.getInstance(application);
        syncEnabler = AutomaticSyncEnabler.getInstance(application);
        connectivityChecker = new ConnectivityChecker(application);
    }
    public LiveData<LiveDataEvent<Event>> getEvent() {
        return eventData;
    }

    public LiveData<Integer> getNetworkResponse() {
        return networkResponse;
    }

    public void signIn(String email, String password){
        if (!connectivityChecker.isConnectedToInternet()){
            eventData.setValue(new LiveDataEvent<>(Event.NO_INTERNET));
            return;
        }
        IResponseListener<ErrorCode> errorListener = errorCode -> {
            eventData.setValue(new LiveDataEvent<>(errorCode.toEvent(Event.MessageType.SHOW_IN_DIALOG)));
            networkResponse.setValue(RESPONSE_ERROR);
        };
        authorizationService.signIn(email, password, signInResponse -> {
            userPreferences.setAuthInfo(signInResponse, true);
            networkResponse.setValue(LOGIN_SUCCESS);
            syncEnabler.enableAutomaticSync();
            syncEnabler.registerSyncServiceOnBoot(true);
            synchronizer.syncDatabases(userPreferences.getAuthorizationToken(), null, userPreferences.getLastSyncDate(), syncPayload ->  {
                userPreferences.setLastSyncDate(syncPayload.getLastSync());
                networkResponse.setValue(SYNC_SUCCESS);
            }, errorListener);
        }, errorListener);
    }

    public void signUp(String email, String userName, String password, String passwordConfirm){
        if (!connectivityChecker.isConnectedToInternet()){
            eventData.setValue(new LiveDataEvent<>(Event.NO_INTERNET));
            return;
        }
        if (!passwordConfirm.equals(password)){
            eventData.setValue(new LiveDataEvent<>(Event.PASSWORDS_DO_NOT_MATCH));
            return;
        }
        IResponseListener<ErrorCode> errorListener = errorCode -> {
            eventData.setValue(new LiveDataEvent<>(errorCode.toEvent(Event.MessageType.SHOW_IN_DIALOG)));
            networkResponse.setValue(RESPONSE_ERROR);
        };
        authorizationService.signUp(email, userName, password, signInResponse -> {
            userPreferences.setAuthInfo(signInResponse, true);
            networkResponse.setValue(LOGIN_SUCCESS);
            syncEnabler.enableAutomaticSync();
            syncEnabler.registerSyncServiceOnBoot(true);
            synchronizer.syncDatabases(userPreferences.getAuthorizationToken(), null, userPreferences.getLastSyncDate(), syncPayload ->  {
                userPreferences.setLastSyncDate(syncPayload.getLastSync());
                networkResponse.setValue(SYNC_SUCCESS);
            }, errorListener);
        }, errorListener);
    }

    public SignInFragmentState getSignInFragmentState() {
        return signInFragmentState;
    }

    public SignUpFragmentState getSignUpFragmentState() {
        return signUpFragmentState;
    }
    public boolean isSignInFragment() {
        return isSignInFragment;
    }

    public void toggleFragments(){
        isSignInFragment = !isSignInFragment;
    }

    public class SignInFragmentState {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    public class SignUpFragmentState {
        private String email;
        private String password;
        private String confirmedPassword;
        private String userName;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmedPassword() {
            return confirmedPassword;
        }

        public void setConfirmedPassword(String confirmedPassword) {
            this.confirmedPassword = confirmedPassword;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
