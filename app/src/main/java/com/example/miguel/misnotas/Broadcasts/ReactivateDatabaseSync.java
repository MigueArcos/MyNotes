package com.example.miguel.misnotas.Broadcasts;

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
public class ReactivateDatabaseSync extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences shPrSync = context.getSharedPreferences("Sync", Context.MODE_PRIVATE);
        if (shPrSync.getInt("userID", 0) != 0) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //Se genera un intent para acceder a la clase del servicio
            Intent sync_service = new Intent(context, SyncNotesService.class);
            //Se crea el pendingintent que se necesita para el alarmmanager
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, sync_service, 0);
            //Se genera una instancia del calendario a una hora determinada
            Calendar calendar = Calendar.getInstance();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), shPrSync.getInt("sync_time", 3600000), pendingIntent);
        }
    }
}

