package com.example.miguel.misnotas.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.Cache;
import com.example.miguel.misnotas.VolleySingleton;
import com.example.miguel.misnotas.broadcasts.SyncNotesService;
import com.example.miguel.misnotas.broadcasts.bootservices.TurnOnDatabaseSync;
import com.example.miguel.misnotas.notes.deleted_notes.DeletedNotesFragment;
import com.example.miguel.misnotas.fragments.FinancesFragment;
import com.example.miguel.misnotas.notes.current_notes.NotesFragment;
import com.example.miguel.misnotas.fragments.WeeklyExpensesFragment;
import com.example.miguel.misnotas.models.SyncData;
import com.example.miguel.misnotas.mymoney.MyMoneyFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, VolleySingleton.NotesResponseListener {

    public static final int WRITE_PERMISSION = 1;

    private int currentFragmentId;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private ProgressDialog progressDialog;
    private AlertDialog.Builder message;

    private Cache cache;

    private AlarmManager alarmManager;
    private PackageManager packageManager;
    private ComponentName receiver;

    private FinancesFragment financesFragment;
    private NotesFragment notesFragment;
    private DeletedNotesFragment deletedNotesFragment;
    private WeeklyExpensesFragment weeklyExpensesFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cache = Cache.getInstance(this);
        /*Este paquete sirve para que si la llamada a esta actividad es desde la notificacion, siempre inicie en el
        fragmento de gastos */
        if (getIntent().hasExtra("CalledFromNotification")) {
            cache.getSettings().edit().putInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 1).apply();
        }


        setTheme(R.style.AppTheme_NoActionBar);


        //All the lines above were added to try delaying the app boot as much as it can
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }

        MyUtils.changeStatusBarColor(this, R.color.colorPrimaryDark);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        LoadUserData();

        initializeFragments();

        //Initialize Progress Dialog properties
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        message = new AlertDialog.Builder(this);
        message.setTitle(R.string.dialog_default_title);
        packageManager = getPackageManager();
        receiver = new ComponentName(this, TurnOnDatabaseSync.class);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    private void LoadUserData() {
        View header = navigationView.getHeaderView(0);
        TextView username = header.findViewById(R.id.header_username);
        TextView email = header.findViewById(R.id.header_email);
        username.setText(cache.getSyncInfo().getString(Cache.SYNC_USERNAME, ""));
        email.setText(cache.getSyncInfo().getString(Cache.SYNC_EMAIL, getString(R.string.activity_login_sign_in_label)));
        if (!cache.getSyncInfo().contains(Cache.SYNC_USER_ID)) {
            email.setTextSize(25);
            navigationView.getMenu().findItem(R.id.sync).setVisible(false);
            navigationView.getMenu().findItem(R.id.close_session).setVisible(false);
            LinearLayout container = header.findViewById(R.id.header_linear_layout);
            container.setOnClickListener(v -> {
                //It is necessary to repeat this is because when the LoginActivity activity redirects to this activity, this event will be fired because it was already set (This is because this activity is SingleTask)
                if (cache.getSyncInfo().getInt(Cache.SYNC_USER_ID, 0) == 0) {
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            });
        } else {
            email.setTextSize(15);
            navigationView.getMenu().findItem(R.id.sync).setVisible(true);
            navigationView.getMenu().findItem(R.id.close_session).setVisible(true);
        }
    }

    /*La razon de ser de este metodo es debido a que esta actividad fue definida como singleTask, eso implica que cuando llega la notificación de escribir gastos y esta actividad sigue en la pila de procesos, Android no la volvera a crear y por lo tanto los datos del paquete que envia el intent que manda la notificacion ("CalledFromNotification" que sirve para usar el WeeklyExpensesFragment en esta actividad) nunca seran recuperados.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(MyUtils.GLOBAL_LOG_TAG, "Executing newIntent");
        drawer.closeDrawer(GravityCompat.START);
        if (intent.hasExtra("dataShouldBeLoaded")){
            LoadUserData();
            updateFragments();
        }
        if (intent.hasExtra("CalledFromNotification")) {
            cache.getSettings().edit().putInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 1).apply();
            initializeFragments();
        }
        //now getIntent() should always return the last received intent
    }

    private void initializeFragments(){
        MenuItem item;
        Fragment fragment = null;
        weeklyExpensesFragment = new WeeklyExpensesFragment();
        financesFragment = new FinancesFragment();
        notesFragment = new NotesFragment();
        deletedNotesFragment = new DeletedNotesFragment();

        switch (cache.getSettings().getInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 1)) {
            case 1:
                item = navigationView.getMenu().findItem(R.id.it1);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fragment = weeklyExpensesFragment;
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 2:
                item = navigationView.getMenu().findItem(R.id.it2);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fragment = financesFragment;
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 3:
                item = navigationView.getMenu().findItem(R.id.it3);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fragment = notesFragment;
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 4:
                item = navigationView.getMenu().findItem(R.id.it4);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fragment = deletedNotesFragment;
                getSupportActionBar().setTitle(item.getTitle());
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted
                } else {
                    // permission denied, boo!
                    Toast.makeText(this.getBaseContext(), R.string.main_activity_request_permission, Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                return;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        MyUtils.hideKeyboard(this);
        if (item.getItemId() == currentFragmentId) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        switch (item.getItemId()) {
            case R.id.it1:
                fragment = weeklyExpensesFragment;
                cache.getSettings().edit().putInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 1).apply();
                currentFragmentId = item.getItemId();
                break;
            case R.id.it2:
                fragment = financesFragment;
                cache.getSettings().edit().putInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 2).apply();
                currentFragmentId = item.getItemId();
                break;
            case R.id.test:
                fragment = new MyMoneyFragment();
                cache.getSettings().edit().putInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 2).apply();
                currentFragmentId = item.getItemId();
                break;
            case R.id.it3:
                fragment = notesFragment;
                cache.getSettings().edit().putInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 3).apply();
                currentFragmentId = item.getItemId();
                break;
            case R.id.it4:
                fragment = deletedNotesFragment;
                cache.getSettings().edit().putInt(Cache.SETTINGS_LAST_SELECTED_FRAGMENT, 4).apply();
                currentFragmentId = item.getItemId();
                break;
            case R.id.schedule:
                Intent i = new Intent(this, SchedulerActivity.class);
                startActivity(i);
                return false;
            case R.id.about:
                message = new AlertDialog.Builder(this);
                message.setTitle(R.string.dialog_default_title);
                message.setMessage(getString(R.string.about_app));
                message.show();
                return false;
            case R.id.sync:
                startDatabaseSync();
                drawer.closeDrawer(GravityCompat.START);
                return false;
            case R.id.close_session:
                message = new AlertDialog.Builder(this);
                message.setMessage(getString(R.string.main_activity_close_session_confirmation))
                        .setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                closeSession();
                                drawer.closeDrawer(GravityCompat.START);
                                LoadUserData();
                            }
                        })
                        .setNegativeButton(R.string.negative_button_label, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                message.show();
                return false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void closeSession() {
        //If you're not gonna use an editor object (Editor=ShPrSync.edit()) then you must use apply or commit in the same line, if you don't make it, changes will not affect the SharedPreferences. {I don't know why}
        cache.getSyncInfo().edit().clear().apply();
        Database.getInstance(this).emptySyncedNotes();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, new Intent(this, SyncNotesService.class), 0));
        updateFragments();
    }

    void startDatabaseSync() {
        progressDialog.setMessage(getString(R.string.syncing_label));
        progressDialog.show();
        SyncData localSyncData = Database.getInstance(this).createLocalSyncData(cache.createMinimalSyncInfo());

        VolleySingleton.getInstance(this).syncAzureDatabases(localSyncData, this);
    }
    private void updateFragments(){
        notesFragment.updateFromDatabase();
        deletedNotesFragment.updateFromDatabase();
    }

    @Override
    public void onSyncSuccess(SyncData.SyncInfo syncInfo) {
        progressDialog.dismiss();
        cache.getSyncInfo().edit().putInt(Cache.SYNC_LAST_SYNCED_ID, syncInfo.getLastSyncedId()).apply();
        updateFragments();
    }

    @Override
    public void onSyncError(String error) {
        progressDialog.dismiss();
        message.setMessage(error);
        message.show();
        //Log.d(MyUtils.GLOBAL_LOG_TAG, error);
    }
}
