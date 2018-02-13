package com.example.miguel.misnotas.viewmodels;

import android.arch.lifecycle.ViewModel;

/**
 * Created by Miguel Ángel López Arcos on 06/02/2018.
 * Copyright © 2018 Mezcal Development. All rights reserved.
 */

public class LoginActivityViewModel extends ViewModel {

    private LoginFragmentViewModel loginFragmentViewModel;
    private SignUpFragmentViewModel signUpFragmentViewModel;

    public LoginActivityViewModel(){
        loginFragmentViewModel = new LoginFragmentViewModel();
        signUpFragmentViewModel = new SignUpFragmentViewModel();
    }

    public LoginFragmentViewModel getLoginFragmentViewModel() {
        return loginFragmentViewModel;
    }

    public SignUpFragmentViewModel getSignUpFragmentViewModel() {
        return signUpFragmentViewModel;
    }

    public class LoginFragmentViewModel {
        private String userName;
        private String password;

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    public class SignUpFragmentViewModel{

    }
}
