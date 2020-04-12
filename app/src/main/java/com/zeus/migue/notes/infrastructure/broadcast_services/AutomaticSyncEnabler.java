package com.zeus.migue.notes.infrastructure.broadcast_services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.zeus.migue.notes.infrastructure.broadcast_services.boot_services.TurnOnSyncService;
import com.zeus.migue.notes.infrastructure.network.HttpClient;

import java.util.Calendar;

public class AutomaticSyncEnabler {
    public static final int INTENT_SYNC_SERVICE = 0;
    private static AutomaticSyncEnabler instance;
    private Context appContext;
    private AutomaticSyncEnabler(Context context) {
        this.appContext = context;
    }
    public static synchronized AutomaticSyncEnabler getInstance(Context context){
        if (instance == null){
            instance = new AutomaticSyncEnabler(context.getApplicationContext());
        }
        return instance;
    }
    //Enables SyncService every certain time, however, if device gets restarted this service must be set up again
    public void enableAutomaticSync() {
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        Intent syncService = new Intent(appContext, SyncReceiverService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, INTENT_SYNC_SERVICE, syncService, 0);
        Calendar calendar = Calendar.getInstance();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), HttpClient.SYNC_TIME, pendingIntent);
    }

    public void disableAutomaticSync() {
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        Intent syncService = new Intent(appContext, SyncReceiverService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, INTENT_SYNC_SERVICE, syncService, 0);
        alarmManager.cancel(pendingIntent);
    }

    //Enables sync service after the device reboots
    public void registerSyncServiceOnBoot(boolean isEnabled) {
        PackageManager packageManager = appContext.getPackageManager();
        ComponentName receiver = new ComponentName(appContext, TurnOnSyncService.class);
        packageManager.setComponentEnabledSetting(receiver, isEnabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
