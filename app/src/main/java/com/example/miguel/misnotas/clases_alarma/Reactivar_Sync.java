package com.example.miguel.misnotas.clases_alarma;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;


/**
 * Created by Miguel on 17/08/2017.
 */
public class Reactivar_Sync extends BroadcastReceiver {
    private SharedPreferences ShPrSync;
    private AlarmManager alarmas;
    private PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        ShPrSync= context.getSharedPreferences("Sync", Context.MODE_PRIVATE);
        if (ShPrSync.getInt("id_usuario", 0)!=0){
            alarmas = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //Se genera un intent para acceder a la clase del servicio
            Intent sync_service = new Intent(context, Servicio_Sincronizar_Notas.class);
            //Se crea el pendingintent que se necesita para el alarmmanager
            pendingIntent= PendingIntent.getBroadcast(context, 0,sync_service,0);
            //Se genera una instancia del calendario a una hora determinada
            Calendar calendar = Calendar.getInstance();
            alarmas.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
        }
    }
}

