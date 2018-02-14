package com.example.miguel.misnotas.viewmodels;

import android.arch.lifecycle.ViewModel;

/**
 * Created by Miguel Ángel López Arcos on 06/02/2018.
 * Copyright © 2018 Mezcal Development. All rights reserved.
 */

public class LoginActivityViewModel extends ViewModel {

    private signInFragmentViewModel signInFragmentViewModel;
    private SignUpFragmentViewModel signUpFragmentViewModel;
    private boolean isSignInFragment;

    public boolean isSignInFragment() {
        return isSignInFragment;
    }

    public void toggleFragments(){
        isSignInFragment = !isSignInFragment;
    }

    public LoginActivityViewModel(){
        isSignInFragment = true;
        signInFragmentViewModel = new signInFragmentViewModel();
        signUpFragmentViewModel = new SignUpFragmentViewModel();
    }

    public signInFragmentViewModel getSignInFragmentViewModel() {
        return signInFragmentViewModel;
    }

    public SignUpFragmentViewModel getSignUpFragmentViewModel() {
        return signUpFragmentViewModel;
    }

    public class signInFragmentViewModel {
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
    public class SignUpFragmentViewModel{
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
