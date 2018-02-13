package com.example.miguel.misnotas.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.VolleySingleton;


/**
 * Created by Miguel on 17/08/2017.
 */
public class SyncNotesService extends BroadcastReceiver implements VolleySingleton.NotesResponseListener {
    private SharedPreferences ShPrSync;

    @Override
    public void onReceive(Context context, Intent intent) {
        ShPrSync = context.getSharedPreferences("Sync", Context.MODE_PRIVATE);
        String syncedNotes = Database.getInstance(context).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String notSyncedNotes = Database.getInstance(context).crearJSON("SELECT * FROM notas WHERE subida='S'");
        VolleySingleton.getInstance(context).syncDBLocal_Remota(notSyncedNotes, syncedNotes, ShPrSync.getInt("userID", 1), ShPrSync.getInt("UltimoIDSync", 0), false, this);
    }

    @Override
    public void onSyncSuccess(int UltimoIDSync, int TotalNumberOfNotes) {
        ShPrSync.edit().putInt("UltimoIDSync", UltimoIDSync).putInt("TotalNumberOfNotes", TotalNumberOfNotes).apply();
    }

    @Override
    public void onSyncError(String error) {
    }
}

