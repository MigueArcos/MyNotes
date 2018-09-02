package com.example.miguel.misnotas.broadcasts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.miguel.misnotas.activities.MainActivity;
import com.example.miguel.misnotas.R;

import java.util.Calendar;

import static com.example.miguel.misnotas.activities.SchedulerActivity.MY_NOTIFICATION_CHANNEL_ID;
import static com.example.miguel.misnotas.activities.SchedulerActivity.MY_NOTIFICATION_CHANNEL_NAME;

/**
 * Created by Miguel on 10/02/2016.
 */
public class WeeklyExpensesNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Cuando se inicia el servicio (cada dia) se leen las settings para ver los dias que estan marcados para sonar.
        SharedPreferences settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean array[] = new boolean[7];
        for (int i = 0; i < 7; i++) {
            array[i] = settings.getBoolean("Day" + i, false);
        }
        //Toast.makeText(context,"¡Saved!",Toast.LENGTH_SHORT).show();
        //Se genera una instancia del calendario a la hora y fecha actuales
        Calendar c = Calendar.getInstance();
        //Se obtiene el dia de la semana actual 1-Domingo.....7-Sábado
        //int dia=c.get(Calendar.DAY_OF_WEEK);
        //Esta formula me ajusta el dia para que corresponda con el del arreglo obtenio de la base, por ejemplo el arr[7]=dom
        //arr[1]=lun,arr[2]=lun (si el arr[posx]==1 esta marcado para sonar si no, no lo hará)
        //dia=(dia+5)%7+1;
        //Se obtiene el tiempo del sistema y se le resta al tiempo de la alarma
        long timeToFire = c.get(Calendar.HOUR_OF_DAY) * 3600000 + c.get(Calendar.MINUTE) * 60000 -
                (settings.getInt("Hour", 12) * 3600000 + settings.getInt("Minute", 0) * 60000);
        //Debe ser menor a 100000 la tolerancia para que el sistema acepte la entrega de la alarma (Tolerancia= 00:03:00.000)
        boolean isInTolerance = timeToFire < 180000;
        /*
        *Nota: El hecho de que se establezca esta tolerancia se debe a que si activas una alarma en el pasado va a sonar, es decir,
        * si por ejemplo activaste una alarma a las 3:00 p.m y ya son las 7:00 p.m igual va a sonar
         */
        if (array[c.get(Calendar.DAY_OF_WEEK) - 1] && isInTolerance) {
            //Se crea el servicio para generar notificationManager
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //Toast.makeText(context, "¡Guardado!", Toast.LENGTH_SHORT).show();
            //Se hace un intent para que la notificacion vaya a la actividad principal (gasto)
            Intent goToMainIntent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("CalledFromNotification", true);
            goToMainIntent.putExtras(bundle);
            //Se crea el pendingintent para ponerselo a la notificacion
            PendingIntent goToMainPendingIntent = PendingIntent.getActivity(context, 0, goToMainIntent, 0);
            //Esto solo obtiene la notificacion predeterminada del sistema
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        MY_NOTIFICATION_CHANNEL_ID,
                        MY_NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, MY_NOTIFICATION_CHANNEL_NAME)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(context.getString(R.string.notification_message))
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(goToMainPendingIntent)
                    .setAutoCancel(true)
                    .setChannelId(MY_NOTIFICATION_CHANNEL_ID)
                    .setSound(alarmSound)
                    .setLights(Color.WHITE, 5000, 5000);
            //Se muestra la notificacion (Se le notifica al sistema)
            notificationManager.notify(0, notification.build());
        }
    }
}

