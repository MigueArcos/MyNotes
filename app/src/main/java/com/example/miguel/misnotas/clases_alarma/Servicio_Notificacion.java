package com.example.miguel.misnotas.clases_alarma;

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


import com.example.miguel.misnotas.Principal;
import com.example.miguel.misnotas.R;

import java.util.Calendar;

/**
 * Created by Miguel on 10/02/2016.
 */
public class Servicio_Notificacion extends BroadcastReceiver {
    NotificationManager notificaciones;
    SharedPreferences opciones;
    @Override
    public void onReceive(Context context, Intent intent) {

        //Cuando se inicia el servicio (cada dia) se leen las opciones para ver los dias que estan marcados para sonar.

        opciones= context.getSharedPreferences("Opciones", Context.MODE_PRIVATE);
        boolean array[]=new boolean [7];
        for (int i=0; i<7; i++){
            array[i]=opciones.getBoolean("dia"+i,false);
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
        long Tiempo_programado=c.get(Calendar.HOUR_OF_DAY)*3600000+c.get(Calendar.MINUTE)*60000-
                (opciones.getInt("Hora",12)*3600000+opciones.getInt("Minuto",0)*60000);
        //Debe ser menor a 100000 la tolerancia para que el sistema acepte la entrega de la alarma (Tolerancia= 00:03:00.000)
        boolean Esta_En_Tolerancia=(Tiempo_programado<180000)? true : false;
        /*
        *Nota: El hecho de que se establezca esta tolerancia se debe a que si activas una alarma en el pasado va a sonar, es decir,
        * si por ejemplo activaste una alarma a las 3:00 p.m y ya son las 7:00 p.m igual va a sonar
         */
        if (array[c.get(Calendar.DAY_OF_WEEK)-1] && Esta_En_Tolerancia){
            //Se crea el servicio para generar notificaciones
            notificaciones = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            //Toast.makeText(context, "¡Guardado!", Toast.LENGTH_SHORT).show();
            //Se hace un intent para que la notificacion vaya a la actividad principal (gasto)
            Intent intento = new Intent(context, Principal.class);
            Bundle paquete=new Bundle();
            paquete.putBoolean("LlamadaDesdeNotificacion", true);
            intento.putExtras(paquete);
            //Se crea el pendingintent para ponerselo a la notificacion
            PendingIntent activitynoti = PendingIntent.getActivity(context,0, intento,0);
            //Esto solo obtiene la notificacion predeterminada del sistema
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //Se crea la notificacion y se le define su icono
            NotificationCompat.Builder minoti = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.app_logo);
            //Se define el titulo de la notifiacion
            minoti.setContentTitle("Control de tu dinero");
            //Se define el contenido de la notificacion
            minoti.setContentText("Es hora de escribir tus gastos diarios.");
            //Se le pone a la notificacion la hora actual
            minoti.setWhen(System.currentTimeMillis());
            //Se define la actividad a la cual nos llevara la notificacion cuando la toquemos
            minoti.setContentIntent(activitynoti);
            //Esta linea sirve para que la notificacion desaparezca una vez clickeada
            minoti.setAutoCancel(true);
            //Se define el sonido de la notificacion obtenido con Uri
            minoti.setSound(alarmSound);

            minoti.setLights(Color.WHITE, 5000, 5000);
            //Se muestra la notificacion (Se le notifica al sistema)
            notificaciones.notify(0,minoti.build());
        }
    }
}

