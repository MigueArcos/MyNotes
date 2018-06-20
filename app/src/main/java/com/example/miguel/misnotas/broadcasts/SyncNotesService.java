package com.example.miguel.misnotas.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.miguel.misnotas.Cache;
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
    private Cache cache;

    @Override
    public void onReceive(Context context, Intent intent) {
        cache = Cache.getInstance(context);
        SyncData localSyncData = Database.getInstance(context).createLocalSyncData(cache.createMinimalSyncInfo());
        VolleySingleton.getInstance(context).syncAzureDatabases(localSyncData,  this);
        MyTxtLogger.getInstance().writeToSD("Starting databases automatic sync...");
    }

    @Override
    public void onSyncSuccess(SyncData.SyncInfo syncInfo) {
        cache.getSyncInfo().edit().putInt(Cache.SYNC_LAST_SYNCED_ID, syncInfo.getLastSyncedId()).apply();
    }

    @Override
    public void onSyncError(String error) {
        MyTxtLogger.getInstance().writeToSD("There was an error with automatic sync: " +error);
    }
}

