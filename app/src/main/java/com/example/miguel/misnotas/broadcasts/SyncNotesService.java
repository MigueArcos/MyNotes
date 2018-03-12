package com.example.miguel.misnotas.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.MyTxtLogger;
import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.VolleySingleton;
import com.example.miguel.misnotas.models.SyncData;


/**
 * Created by Miguel on 17/08/2017.
 */
public class SyncNotesService extends BroadcastReceiver implements VolleySingleton.NotesResponseListener {
    private SharedPreferences ShPrSync;

    @Override
    public void onReceive(Context context, Intent intent) {
        ShPrSync = context.getSharedPreferences("sync", Context.MODE_PRIVATE);

        SyncData localSyncData = Database.getInstance(context).createLocalSyncData(new SyncData.SyncInfo(ShPrSync.getInt("userId", 1), ShPrSync.getInt("lastSyncedId", 0)));
        VolleySingleton.getInstance(context).syncDatabases(localSyncData, false, this);
    }

    @Override
    public void onSyncSuccess(SyncData.SyncInfo syncInfo) {
        ShPrSync.edit().putInt("lastSyncedId", syncInfo.getLastSyncedId()).apply();
    }

    @Override
    public void onSyncError(String error) {
        MyTxtLogger.getInstance().writeToSD("There was an error with automatic sync: " +error);
    }
}

