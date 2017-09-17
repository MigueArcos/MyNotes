package com.example.miguel.misnotas.clases_alarma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.miguel.misnotas.Sincronizacion;
import com.example.miguel.misnotas.Volley_Singleton;


/**
 * Created by Miguel on 17/08/2017.
 */
public class Servicio_Sincronizar_Notas extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("si","si");
        Volley_Singleton.getInstance(context).syncDBLocal_Remota();
    }
}

