package com.example.miguel.misnotas.clases_alarma;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;

/**
 * Created by Miguel on 06/02/2016.
 */
public class Servicio_Inicio extends BroadcastReceiver {
    private AlarmManager alarmas;
    private PendingIntent alarmIntent;
    SharedPreferences opciones;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Este servicio solo se ejecuta al inicio de el sistema
        //Se crea una instancia del servicio de alarmas
        alarmas = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //Se crea una instancia de las opciones
        opciones= context.getSharedPreferences("Opciones", Context.MODE_PRIVATE);
        //Se genera un intent para pasar a al servicio de notificacion que sera el que defina si suena o no la notificacion
        Intent intent1 = new Intent(context, Servicio_Notificacion.class);
        //Con este pedingintent se pasa de aqui al servicio de notificaciones
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
        //Se crea una instancia del calendario y se leen la hora y minuto en que se debe pasar al servicio de notificacion
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, opciones.getInt("Hora",12));
        calendar.set(Calendar.MINUTE,opciones.getInt("Minuto",0));
        //Se definen las alarmas para pasar al servicio de notificacion en la hora y minuto definidos en el calendar
        alarmas.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),86400000,alarmIntent);
    }

}