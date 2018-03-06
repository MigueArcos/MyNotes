package com.example.miguel.misnotas.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.VolleySingleton;
import com.example.miguel.misnotas.models.SyncData;


/**
 * Created by Miguel on 17/08/2017.
 */
public class SyncNotesService extends BroadcastReceiver implements VolleySingleton.NotesResponseListener {
    private SharedPreferences ShPrSync;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*ShPrSync = context.getSharedPreferences("Sync", Context.MODE_PRIVATE);
        String syncedNotes = Database.getInstance(context).createJSON(false);
        String notSyncedNotes = Database.getInstance(context).createJSON(true);
        VolleySingleton.getInstance(context).syncDatabases(notSyncedNotes, syncedNotes, ShPrSync.getInt("userId", 1), ShPrSync.getInt("lastSyncedId", 0), false, this);*/
    }

    @Override
    public void onSyncSuccess(SyncData.SyncInfo syncInfo) {
        //ShPrSync.edit().putInt("lastSyncedId", UltimoIDSync).putInt("TotalNumberOfNotes", TotalNumberOfNotes).apply();
    }

    @Override
    public void onSyncError(String error) {
    }
}

