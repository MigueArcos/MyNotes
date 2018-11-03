package com.example.miguel.misnotas.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.broadcasts.WeeklyExpensesNotification;
import com.example.miguel.misnotas.broadcasts.bootservices.TurnOnWeeklyExpensesNotification;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Locale;

public class SchedulerActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener {
    private TextView horario, ampm;
    private NotificationManager notificaciones;
    private AlarmManager alarmas;
    //base1 ob;
    private PendingIntent intentopendiente;
    private SharedPreferences opciones;
    private SharedPreferences.Editor editor;
    private CheckBox[] lista;
    private int HOUR, MINUTE;
    private ComponentName receiver;
    private PackageManager pm;

    public static String MY_NOTIFICATION_CHANNEL_ID = "chanel_id";// The id of the channel.
    public static String MY_NOTIFICATION_CHANNEL_NAME = "My Channel Name";// The user-visible name of the channel.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        // ob = new base1(this);
        opciones = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = opciones.edit();
        receiver = new ComponentName(this, TurnOnWeeklyExpensesNotification.class);
        pm = this.getPackageManager();
        lista = new CheckBox[7];
        lista[0] = (CheckBox) findViewById(R.id.domingo);
        lista[1] = (CheckBox) findViewById(R.id.lunes);
        lista[2] = (CheckBox) findViewById(R.id.martes);
        lista[3] = (CheckBox) findViewById(R.id.miercoles);
        lista[4] = (CheckBox) findViewById(R.id.jueves);
        lista[5] = (CheckBox) findViewById(R.id.viernes);
        lista[6] = (CheckBox) findViewById(R.id.sabado);
        //Se crea el gestor de notificaciones
        notificaciones = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Se crea el gestor de alarmas//
        alarmas = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Estas 3 lineas son para referenciar los componentes del xml y agregar el evento que se necesita
        horario = (TextView) findViewById(R.id.hora);
        ampm = (TextView) findViewById(R.id.ampm);
        horario.setOnClickListener(this);
        horario.setOnLongClickListener(this);
        leer();
        for (int i = 0; i < 7; i++) {
            lista[i].setOnCheckedChangeListener(this);
        }
    }

    void leer() {
        //Se lee la hora y se le aplica la operacion de conversion de 24 horas a 12 horas
        HOUR = opciones.getInt("Hour", 12);
        //Se lee el minuto
        MINUTE = opciones.getInt("Minute", 0);
        horario.setText(String.format(Locale.US, "%d:%s", (HOUR + 11) % 12 + 1, MINUTE < 10 ? "0" + MINUTE :  MINUTE));
        ampm.setText((HOUR >= 12) ? R.string.pm_format : R.string.am_format);
        for (int i = 0; i < lista.length; i++) {
            lista[i].setChecked(opciones.getBoolean("Day" + i, false));
        }
    }

    void update_dias() {
        boolean activar_alarmas = false;
        for (int i = 0; i < 7; i++) {
            if (lista[i].isChecked()) {
                activar_alarmas = true;
            }
            editor.putBoolean("Day" + i, lista[i].isChecked());
        }
        editor.commit();
        if (activar_alarmas) {
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } else {
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            //Se cancelan alarmas ya que no hay dias para sonar
            //alarmas.cancel(intentopendiente);
        }
        //ob.actualizar_dias(arreglo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_horario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //Se crea el constructor del dialog (TimePicker) con todo y su evento de cambios de fecha
        TimePickerDialog time = TimePickerDialog.newInstance(SchedulerActivity.this, HOUR, MINUTE, false);
        time.setThemeDark(true);
        time.setTitle(getString(R.string.activity_schedule_time_picker_title));
        //Se muestra el timepicker
        time.show(getFragmentManager(), TimePickerDialog.class.getSimpleName());
    }

    @Override
    public boolean onLongClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle paquete = new Bundle();
        paquete.putBoolean("CalledFromNotification", true);
        intent.putExtras(paquete);
        PendingIntent activitynoti = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    MY_NOTIFICATION_CHANNEL_ID,
                    MY_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificaciones.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder minoti = new NotificationCompat.Builder(this, MY_NOTIFICATION_CHANNEL_NAME)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_message_fuck_up))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(activitynoti)
                .setAutoCancel(true)
                .setChannelId(MY_NOTIFICATION_CHANNEL_ID)
                .setSound(alarmSound);

        notificaciones.notify(0, minoti.build());
        return true;
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hora, int minuto, int segundo) {
        editor.putInt("Hour", hora);
        editor.putInt("Minute", minuto);
        HOUR = hora;
        MINUTE = minuto;
        editor.commit();
        horario.setText(String.format(Locale.US, "%d:%s", (hora + 11) % 12 + 1, minuto < 10 ? "0" + minuto :  minuto));
        ampm.setText((hora >= 12) ? " p.m" : " a.m");
        //Toast.makeText(getBaseContext(),""+HOUR+"-"+MINUTE+"", Toast.LENGTH_SHORT).show();
        definir_alarmas();

    }

    void definir_alarmas() {
        //Se genera un intent para acceder a la clase del servicio
        Intent intent = new Intent(SchedulerActivity.this, WeeklyExpensesNotification.class);
        //Se crea el pendingintent que se necesita para el alarmmanager
        intentopendiente = PendingIntent.getBroadcast(SchedulerActivity.this, 0, intent, 0);
        //Se genera una instancia del calendario a una hora determinada
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, HOUR);
        calendar.set(Calendar.MINUTE, MINUTE);
        //Se definen las alarmas
        alarmas.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, intentopendiente);
    }

    //Por defecto al rotar el TPD se pierde el  ontimesetlistener , por lo que en este metodo (onResume) se vuelve a generar
    //para que vuelva a obedecer el ontimeseet
    //O sea que al crear el TPD se le asigna automaticamente el ontimesetlistener que tiene la actividad
    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog time = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if (time != null) time.setOnTimeSetListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        update_dias();
    }
}


