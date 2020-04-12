package com.zeus.migue.notes.infrastructure.broadcast_services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zeus.migue.notes.data.DTO.ErrorCode;
import com.zeus.migue.notes.data.DTO.sync.SyncPayload;
import com.zeus.migue.notes.infrastructure.network.IResponseListener;
import com.zeus.migue.notes.infrastructure.network.services.ISynchronizer;
import com.zeus.migue.notes.infrastructure.network.services.Synchronizer;
import com.zeus.migue.notes.infrastructure.services.contracts.IConnectivityChecker;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.services.implementations.ConnectivityChecker;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.services.implementations.UserPreferences;
import com.zeus.migue.notes.infrastructure.utils.Utils;

public class SyncReceiverService extends BroadcastReceiver {
    private UserPreferences userPreferences;
    private ISynchronizer synchronizer;
    private IConnectivityChecker connectivityChecker;
    private ILogger logger;

    @Override
    public void onReceive(Context context, Intent intent) {
        initializeServices(context.getApplicationContext());
        if (connectivityChecker.isConnectedToInternet() && userPreferences.userIsAuthenticated()) {
            IResponseListener<SyncPayload> syncSuccessListener = syncPayload -> {
                userPreferences.setLastSyncDate(syncPayload.getLastSync());
            };
            IResponseListener<ErrorCode> syncErrorListener = errorCode -> logger.log(Utils.stringIsNullOrEmpty(errorCode.getMessage()) ? "Sync Error" : errorCode.getMessage());
            synchronizer.syncDatabases(userPreferences.getAuthorizationToken(), userPreferences.getRefreshToken(), userPreferences.getLastSyncDate(), syncSuccessListener, syncErrorListener);
        }
    }

    private void initializeServices(Context appContext) {
        connectivityChecker = new ConnectivityChecker(appContext);
        synchronizer = new Synchronizer(appContext);
        userPreferences = UserPreferences.getInstance(appContext);
        logger = Logger.getInstance(appContext);
    }
}
