package com.zeus.migue.notes.ui.activity_login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.SignInResponse;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.infrastructure.network.services.AuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.IAuthorizationService;
import com.zeus.migue.notes.infrastructure.network.services.INotesSynchronizer;
import com.zeus.migue.notes.infrastructure.network.services.NotesSynchronizer;
import com.zeus.migue.notes.infrastructure.repositories.NotesRepository;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.services.implementations.UserPreferences;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;

public class LoginActivityViewModel extends AndroidViewModel {
    private SignInFragmentState signInFragmentState;
    private SignUpFragmentState signUpFragmentState;
    private boolean isSignInFragment;
    private MutableLiveData<LiveDataEvent<Event>> eventData;
    private MutableLiveData<SignInResponse> loginResponse;

    private IAuthorizationService authorizationService;
    private NotesRepository notesRepository;
    private UserPreferences userPreferences;
    public LoginActivityViewModel(@NonNull Application application){
        super(application);
        eventData = new MutableLiveData<>();
        loginResponse = new MutableLiveData<>();
        isSignInFragment = true;
        signInFragmentState = new SignInFragmentState();
        signUpFragmentState = new SignUpFragmentState();

        INotesSynchronizer notesSynchronizer = new NotesSynchronizer(application);
        authorizationService = new AuthorizationService(application);
        ILogger logger = Logger.getInstance(application);
        notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application).notesDao(), logger);
        userPreferences = UserPreferences.getInstance(application);
    }
    public LiveData<LiveDataEvent<Event>> getEvent() {
        return eventData;
    }

    public LiveData<SignInResponse> getLoginResponse() {
        return loginResponse;
    }

    public void signIn(String email, String password){
        authorizationService.signIn(email, password, signInResponse -> {
            userPreferences.setAuthInfo(signInResponse, true);
            loginResponse.setValue(signInResponse);
        }, errorCode -> {
            eventData.setValue(new LiveDataEvent<>(errorCode.toEvent(Event.MessageType.SHOW_IN_DIALOG)));
        });
    }

    public void signUp(String email, String userName, String password, String passwordConfirm){
        if (!passwordConfirm.equals(password)){
            eventData.setValue(new LiveDataEvent<>(Event.PASSWORDS_DO_NOT_MATCH));
            return;
        }
        authorizationService.signUp(email, userName, password, signInResponse -> {
            userPreferences.setAuthInfo(signInResponse, true);
            loginResponse.setValue(signInResponse);
        }, errorCode -> {
            eventData.setValue(new LiveDataEvent<>(errorCode.toEvent(Event.MessageType.SHOW_IN_DIALOG)));
        });
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
