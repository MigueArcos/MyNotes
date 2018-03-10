package com.example.miguel.misnotas.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.example.miguel.misnotas.SharedPreferencesManager;
import com.example.miguel.misnotas.VolleySingleton;
import com.example.miguel.misnotas.broadcasts.SyncNotesService;
import com.example.miguel.misnotas.broadcasts.bootservices.TurnOnDatabaseSync;
import com.example.miguel.misnotas.fragments.DeletedNotesFragment;
import com.example.miguel.misnotas.fragments.FinancesFragment;
import com.example.miguel.misnotas.fragments.NotesFragment;
import com.example.miguel.misnotas.fragments.WeeklyExpensesFragment;
import com.example.miguel.misnotas.models.SyncData;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, VolleySingleton.NotesResponseListener {

    public static final int WRITE_PERMISSION = 1;

    private NavigationView navigationView;
    private AlertDialog.Builder message;
    private int currentFragmentId;
    private SharedPreferences ShPrFragments;
    private SharedPreferences.Editor Editor;
    private SharedPreferences ShPrSync;
    private SharedPreferencesManager preferencesManager;
    private DrawerLayout drawer;
    private ProgressDialog progressDialog;
    private AlarmManager alarmManager;
    private PackageManager packageManager;
    private ComponentName receiver;
    private FinancesFragment financesFragment;
    private NotesFragment notesFragment;
    private DeletedNotesFragment deletedNotesFragment;
    private WeeklyExpensesFragment weeklyExpensesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*Este editor de SharedPrefs sirve para obtener el ultimo fragmento que se quedo seleccionado, ya que cuando la app se cierra
        con el boton de atras entonces esta se vuelve a construir y siempre volveria al primer fragmento si estos no se guardan*/
        ShPrFragments = getSharedPreferences("fragmentos", Context.MODE_PRIVATE);
        Editor = ShPrFragments.edit();
        ShPrSync = getSharedPreferences("Sync", Context.MODE_PRIVATE);
        /*Este paquete sirve para que si la llamada a esta actividad es desde la notificacion, siempre inicie en el
        fragmento de gastos */
        if (getIntent().hasExtra("CalledFromNotification")) {
            Editor.putInt("lastSelectedFragment", 1);
            Editor.commit();
        }
        LoadUserData();

        initiliazeFragments();

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
        TextView header_username = header.findViewById(R.id.header_username);
        TextView header_email = header.findViewById(R.id.header_email);
        header_username.setText(ShPrSync.getString("username", ""));
        header_email.setText(ShPrSync.getString("email", getString(R.string.activity_login_sign_in_label)));
        if (ShPrSync.getInt("userId", 0) == 0) {
            header_email.setTextSize(25);
            navigationView.getMenu().findItem(R.id.sync).setVisible(false);
            navigationView.getMenu().findItem(R.id.close_session).setVisible(false);
            LinearLayout header_linearlayout = (LinearLayout) header.findViewById(R.id.header_linearlayout);
            header_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //It is neccesary to repeat this if because when the LoginActivity activity redirects to this activity, this event will be fired because it was already set (This is because this activity is SingleTask)
                    if (ShPrSync.getInt("userId", 0) == 0) {
                        Intent login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(login);
                    }
                }
            });
        } else {
            header_email.setTextSize(15);
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
        LoadUserData();
        if (intent.hasExtra("CalledFromNotification")) {
            Editor.putInt("lastSelectedFragment", 1);
            Editor.commit();
            Fragment fragment = SelectLastFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        //now getIntent() should always return the last received intent
    }

    private Fragment SelectLastFragment() {
        MenuItem item;
        Fragment fr = null;
        switch (ShPrFragments.getInt("lastSelectedFragment", 1)) {
            case 1:
                item = navigationView.getMenu().findItem(R.id.it1);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = new WeeklyExpensesFragment();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 2:
                item = navigationView.getMenu().findItem(R.id.it2);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = new FinancesFragment();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 3:
                item = navigationView.getMenu().findItem(R.id.it3);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = new NotesFragment();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 4:
                item = navigationView.getMenu().findItem(R.id.it4);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = new DeletedNotesFragment();
                getSupportActionBar().setTitle(item.getTitle());
        }
        return fr;
    }

    private void initiliazeFragments(){
        MenuItem item;
        Fragment fr = null;
        weeklyExpensesFragment = new WeeklyExpensesFragment();
        financesFragment = new FinancesFragment();
        notesFragment = new NotesFragment();
        deletedNotesFragment = new DeletedNotesFragment();

        switch (ShPrFragments.getInt("lastSelectedFragment", 1)) {
            case 1:
                item = navigationView.getMenu().findItem(R.id.it1);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = weeklyExpensesFragment;
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 2:
                item = navigationView.getMenu().findItem(R.id.it2);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = financesFragment;
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 3:
                item = navigationView.getMenu().findItem(R.id.it3);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = notesFragment;
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 4:
                item = navigationView.getMenu().findItem(R.id.it4);
                item.setChecked(true);
                currentFragmentId = item.getItemId();
                fr = deletedNotesFragment;
                getSupportActionBar().setTitle(item.getTitle());
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fr, fr.getClass().getSimpleName());
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El permiso ha sido otorgado :D

                } else {
                    // permission denied, boo!
                    Toast.makeText(this.getBaseContext(), R.string.main_activity_request_permission, Toast.LENGTH_SHORT).show();
                    this.finish();

                }
                return;
        }

        // other 'case' lines to check for other
        // permissions this app might request

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
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        MyUtils.hideKeyboard(this);
        if (item.getItemId() == currentFragmentId) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        switch (item.getItemId()) {
            case R.id.it1:
                fragment = new WeeklyExpensesFragment();
                Editor.putInt("lastSelectedFragment", 1);
                currentFragmentId = item.getItemId();
                break;
            case R.id.it2:
                fragment = new FinancesFragment();
                Editor.putInt("lastSelectedFragment", 2);
                currentFragmentId = item.getItemId();
                break;
            case R.id.it3:
                fragment = new NotesFragment();
                Editor.putInt("lastSelectedFragment", 3);
                currentFragmentId = item.getItemId();
                break;
            case R.id.it4:
                fragment = new DeletedNotesFragment();
                Editor.putInt("lastSelectedFragment", 4);
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
                /***
                 * Se envia la instancia del framento que actualmente se esta mostrando a la clase Sincronizacion para que alla se ejecute el metodo onResume() de cualquier fragment y se actualicen las notas en tiempo real (se debe hacer forzosamente en el success de la clase sync ya que de no ser asi, al ser una peticion aincrona el codigo de actualizar BD se ejecutaria inmediatamente despues y no se mostrarianb los cambios
                 */
                //Fragment fragmento=getSupportFragmentManager().findFragmentById(R.id.content_frame);
                //VolleySingleton.getInstance(this).syncDBLocal_Remota(fragmento);
                StartDatabaseSync();
                drawer.closeDrawer(GravityCompat.START);
                return false;
            case R.id.close_session:
                message = new AlertDialog.Builder(this);
                message.setMessage(getString(R.string.main_activity_close_session_confirmation))
                        .setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                closeSession();
                                getSupportFragmentManager().findFragmentById(R.id.content_frame).onResume();
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
        Editor.commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void closeSession() {
        //If you're not gonna use an editor object (Editor=ShPrSync.edit()) then you must use apply or commit in the same line, if you don't make it, changes will not affect the SharedPreferences. {I don't know why}
        ShPrSync.edit().clear().apply();
        Database.getInstance(this).emptySyncedNotes();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, new Intent(this, SyncNotesService.class), 0));
    }

    void StartDatabaseSync() {
        /*
        String NotasNoSync = Database.getInstance(this).createJSON(false);
        String NotasSync = Database.getInstance(this).createJSON(true);
        */
        progressDialog.setMessage(getString(R.string.syncing_label));
        progressDialog.show();
        SyncData localSyncData = Database.getInstance(this).createLocalSyncData(new SyncData.SyncInfo(ShPrSync.getInt("userId", 1), ShPrSync.getInt("lastSyncedId", 0)));
        //mensaje.setMessage();
        //mensaje.show();
        //Log.d(MyUtils.GLOBAL_LOG_TAG, syncDataJson);
        VolleySingleton.getInstance(this).syncDatabases(localSyncData, false, this);
        //VolleySingleton.getInstance(this).syncDatabases(NotasSync, NotasNoSync, ShPrSync.getInt("userId", 1), ShPrSync.getInt("lastSyncedId", 0), false, this);
    }

    @Override
    public void onSyncSuccess(SyncData.SyncInfo syncInfo) {
        progressDialog.dismiss();
        ShPrSync.edit().putInt("lastSyncedId", syncInfo.getLastSyncedId()).apply();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (currentFragment instanceof NotesFragment){
            ((NotesFragment) currentFragment).updateFromDatabase();
        }
        else if (currentFragment instanceof DeletedNotesFragment){
            ((DeletedNotesFragment) currentFragment).updateFromDatabase();
        }
        //MyTxtLogger.getInstance().writeToSD("" + TotalNumberOfNotes);
    }

    @Override
    public void onSyncError(String error) {
        progressDialog.dismiss();
        message.setMessage(error);
        message.show();
        Log.d(MyUtils.GLOBAL_LOG_TAG, error);
    }
}
