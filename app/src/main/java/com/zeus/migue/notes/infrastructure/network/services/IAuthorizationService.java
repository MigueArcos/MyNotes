package com.zeus.migue.notes.infrastructure.network.services;

import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.SignInResponse;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;

public interface IAuthorizationService {
    void signIn(String email, String password, IResponseListener<SignInResponse> successListener, IResponseListener<ErrorCode> errorListener);
    void signUp(String email, String userName, String password, IResponseListener<SignInResponse> successListener, IResponseListener<ErrorCode> errorListener);
    void refreshToken(String refreshToke, IResponseListener<SignInResponse> successListener, IResponseListener<ErrorCode> errorListener);
}
