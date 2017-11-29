package com.example.miguel.misnotas.clases_alarma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.Volley_Singleton;



/**
 * Created by Miguel on 17/08/2017.
 */
public class Servicio_Sincronizar_Notas extends BroadcastReceiver implements Volley_Singleton.NotesResponseListener{
    private SharedPreferences ShPrSync;
    @Override
    public void onReceive(Context context, Intent intent) {
        ShPrSync= context.getSharedPreferences("Sync", Context.MODE_PRIVATE);
        String NotasNoSync= Database.getInstance(context).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync=Database.getInstance(context).crearJSON("SELECT * FROM notas WHERE subida='S'");
        Volley_Singleton.getInstance(context).syncDBLocal_Remota(NotasSync,NotasNoSync,ShPrSync.getInt("id_usuario", 1),ShPrSync.getInt("UltimoIDSync", 0), false, this);
    }

    @Override
    public void onSyncSuccess(int UltimoIDSync, int TotalNumberOfNotes) {
        ShPrSync.edit().putInt("UltimoIDSync", UltimoIDSync).putInt("TotalNumberOfNotes", TotalNumberOfNotes).apply();
    }

    @Override
    public void onSyncError(String error) {
    }
}

