package com.example.miguel.misnotas;

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

import com.example.miguel.misnotas.clases_alarma.Reactivar_Sync;
import com.example.miguel.misnotas.clases_alarma.Servicio_Sincronizar_Notas;
//Funciones lambda de alto orden

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Volley_Singleton.NotesResponseListener {
    //boolean Actualizar_notas=false;
    private NavigationView navigationView;
    public static final int Permiso_De_Escritura = 1;
    public static final int RECORD_AUDIO_PERMISSION = 2;
    private AlertDialog mensaje;
    private AlertDialog.Builder builder;
    private int CurrentFragment;
    private SharedPreferences ShPrFragments;
    private SharedPreferences.Editor Editor;
    private SharedPreferences ShPrSync;
    private DrawerLayout drawer;
    private View header;
    private ProgressDialog progressDialog;
    private AlarmManager alarmManager;
    private PackageManager packageManager;
    private ComponentName receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checarPermisos();
        }
        setContentView(R.layout.activity_principal);
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
        if (getIntent().hasExtra("LlamadaDesdeNotificacion")) {
            Editor.putInt("FragmentoSeleccionado", 1);
            Editor.commit();
        }
        LoadUserData();
        Fragment fragment = SelectLastFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        //Initialize Progress Dialog properties
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        mensaje = new AlertDialog.Builder(this).create();
        mensaje.setTitle(R.string.dialog_default_title);
        packageManager = getPackageManager();
        receiver = new ComponentName(this, Reactivar_Sync.class);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    private void LoadUserData() {
        header = navigationView.getHeaderView(0);
        TextView header_username = (TextView) header.findViewById(R.id.header_username);
        TextView header_email = (TextView) header.findViewById(R.id.header_email);
        header_username.setText(ShPrSync.getString("username", ""));
        header_email.setText(ShPrSync.getString("email", "Iniciar sesión"));
        if (ShPrSync.getInt("id_usuario", 0) == 0) {
            header_email.setTextSize(25);
            navigationView.getMenu().findItem(R.id.sync).setVisible(false);
            navigationView.getMenu().findItem(R.id.close_session).setVisible(false);
            LinearLayout header_linearlayout = (LinearLayout) header.findViewById(R.id.header_linearlayout);
            header_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //It is neccesary to repeat this is because when the Login activity redirects to this activity, this event will be fired because it was already set (This is because this activity is SingleTask)
                    if (ShPrSync.getInt("id_usuario", 0) == 0) {
                        Intent login = new Intent(Principal.this, Login.class);
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

    /*La razon de ser de este metodo es debido a que esta actividad fue definida como singleTask, eso implica que cuando llega la notificación de escribir gastos y esta actividad sigue en la pila de procesos, Android no la volvera a crear y por lo tanto los datos del paquete que envia el intent que manda la notificacion ("LlamadaDesdeNotificacion" que sirve para usar el fragmento_gastos en esta actividad) nunca seran recuperados.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        drawer.closeDrawer(GravityCompat.START);
        LoadUserData();
        if (intent.hasExtra("LlamadaDesdeNotificacion")) {
            Editor.putInt("FragmentoSeleccionado", 1);
            Editor.commit();
            Fragment fragment = SelectLastFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        //now getIntent() should always return the last received intent
    }

    Fragment SelectLastFragment() {
        MenuItem item;
        Fragment fr = null;
        switch (ShPrFragments.getInt("FragmentoSeleccionado", 1)) {
            case 1:
                item = navigationView.getMenu().findItem(R.id.it1);
                item.setChecked(true);
                CurrentFragment = item.getItemId();
                fr = new fragmento_gastos();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 2:
                item = navigationView.getMenu().findItem(R.id.it2);
                item.setChecked(true);
                CurrentFragment = item.getItemId();
                fr = new fragmento_finanzas();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 3:
                item = navigationView.getMenu().findItem(R.id.it3);
                item.setChecked(true);
                CurrentFragment = item.getItemId();
                fr = new fragmento_notas();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 4:
                item = navigationView.getMenu().findItem(R.id.it4);
                item.setChecked(true);
                CurrentFragment = item.getItemId();
                fr = new fragmento_notas_eliminadas();
                getSupportActionBar().setTitle(item.getTitle());
            case 5:
                item = navigationView.getMenu().findItem(R.id.it5);
                item.setChecked(true);
                CurrentFragment = item.getItemId();
                fr = new FragmentVoiceNotes();
                getSupportActionBar().setTitle(item.getTitle());
        }
        return fr;
    }

    void checarPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permiso_De_Escritura);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Permiso_De_Escritura:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El permiso ha sido otorgado :D

                } else {
                    // permission denied, boo!
                    Toast.makeText(this, R.string.main_activity_request_permission, Toast.LENGTH_SHORT).show();
                    this.finish();

                }
                break;

            case RECORD_AUDIO_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El permiso ha sido otorgado :D

                } else {
                    // permission denied, boo!
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                        Toast.makeText(this, R.string.error_record_audio_permission, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, R.string.permission_record_audio, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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

    /*
        @Override
        protected void onPostResume() {
            super.onPostResume();
            if (Actualizar_notas){
                Fragment fragment=new fragmento_notas();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                Log.d("Hi","aqui ando");
            }
            //Fragment fragment=new fragmento_notas();
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        MyUtils.hideKeyboard(this);
        if (item.getItemId() == CurrentFragment) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        switch (item.getItemId()) {
            case R.id.it1:
                fragment = new fragmento_gastos();
                Editor.putInt("FragmentoSeleccionado", 1);
                CurrentFragment = item.getItemId();
                break;
            case R.id.it2:
                fragment = new fragmento_finanzas();
                Editor.putInt("FragmentoSeleccionado", 2);
                CurrentFragment = item.getItemId();
                break;
            case R.id.it3:
                fragment = new fragmento_notas();
                Editor.putInt("FragmentoSeleccionado", 3);
                CurrentFragment = item.getItemId();
                break;
            case R.id.it4:
                fragment = new fragmento_notas_eliminadas();
                Editor.putInt("FragmentoSeleccionado", 4);
                CurrentFragment = item.getItemId();
                break;
            case R.id.it5:
                fragment = new FragmentVoiceNotes();
                Editor.putInt("FragmentoSeleccionado", 5);
                CurrentFragment = item.getItemId();
                break;
            case R.id.schedule:
                Intent i = new Intent(this, Programacion_horarios.class);
                startActivity(i);
                return false;
            case R.id.about:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dialog_default_title);
                builder.setMessage(getString(R.string.about_app));
                mensaje = builder.create();
                mensaje.show();
                return false;
            case R.id.sync:
                /***
                 * Se envia la instancia del framento que actualmente se esta mostrando a la clase Sincronizacion para que alla se ejecute el metodo onResume() de cualquier fragment y se actualicen las notas en tiempo real (se debe hacer forzosamente en el success de la clase sync ya que de no ser asi, al ser una peticion aincrona el codigo de actualizar BD se ejecutaria inmediatamente despues y no se mostrarianb los cambios
                 */
                //Fragment fragmento=getSupportFragmentManager().findFragmentById(R.id.content_frame);
                //Volley_Singleton.getInstance(this).syncDBLocal_Remota(fragmento);
                StartDatabaseSync();
                drawer.closeDrawer(GravityCompat.START);
                return false;
            case R.id.close_session:
                builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.main_activity_close_session_confirmation))
                        .setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CerrarSesion();
                                getSupportFragmentManager().findFragmentById(R.id.content_frame).onResume();
                                drawer.closeDrawer(GravityCompat.START);
                                LoadUserData();
                            }
                        })
                        .setNegativeButton(R.string.negative_button_label, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                mensaje = builder.create();
                mensaje.show();
                return false;
        }
        Editor.commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CerrarSesion() {
        //If you're not gonna use an editor object (Editor=ShPrSync.edit()) then you must use apply or commit in the same line, if you don't make it, changes will not affect the SharedPreferences. {I don't know why}
        ShPrSync.edit().clear().apply();
        Database.getInstance(this).emptySyncedNotes();
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, new Intent(this, Servicio_Sincronizar_Notas.class), 0));
    }

    void StartDatabaseSync() {
        String NotasNoSync = Database.getInstance(this).crearJSON("SELECT * FROM notas WHERE subida='N'");
        String NotasSync = Database.getInstance(this).crearJSON("SELECT * FROM notas WHERE subida='S'");
        Log.d("NotesSync", NotasSync);
        Log.d("NotesUnSync", NotasNoSync);
        progressDialog.setMessage(getString(R.string.syncing_label));
        progressDialog.show();
        Volley_Singleton.getInstance(this).syncDBLocal_Remota(NotasSync, NotasNoSync, ShPrSync.getInt("id_usuario", 1), ShPrSync.getInt("UltimoIDSync", 0), false, this);
    }

    @Override
    public void onSyncSuccess(int UltimoIDSync, int TotalNumberOfNotes) {
        progressDialog.dismiss();
        ShPrSync.edit().putInt("UltimoIDSync", UltimoIDSync).putInt("TotalNumberOfNotes", TotalNumberOfNotes).apply();
        getSupportFragmentManager().findFragmentById(R.id.content_frame).onResume();
        MyTxtLogger.getInstance().writeToSD("" + TotalNumberOfNotes);
    }

    @Override
    public void onSyncError(String error) {
        progressDialog.dismiss();
        mensaje.setMessage(error);
        mensaje.show();
    }
}
