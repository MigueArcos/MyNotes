package com.zeus.migue.notes.infrastructure.broadcast_services.boot_services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zeus.migue.notes.infrastructure.broadcast_services.AutomaticSyncEnabler;
import com.zeus.migue.notes.infrastructure.services.implementations.UserPreferences;

public class TurnOnSyncService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        UserPreferences cache = UserPreferences.getInstance(context.getApplicationContext());
        if (cache.userIsAuthenticated()) {
            AutomaticSyncEnabler.getInstance(context).enableAutomaticSync();
        }
    }
}
