package com.example.miguel.misnotas;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //boolean Actualizar_notas=false;
    private NavigationView navigationView;
    private final int Permiso_De_Escritura=1;
    private AlertDialog mensaje;
    private AlertDialog.Builder builder;
    private int CurrentFragment;
    private SharedPreferences ShPrFragments;
    private SharedPreferences.Editor Editor;
    private SharedPreferences ShPrSync;
    private DrawerLayout drawer;
    private View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        ShPrFragments= getSharedPreferences("fragmentos", Context.MODE_PRIVATE);
        Editor=ShPrFragments.edit();
        ShPrSync= getSharedPreferences("Sync", Context.MODE_PRIVATE);
        /*Este paquete sirve para que si la llamada a esta actividad es desde la notificacion, siempre inicie en el
        fragmento de gastos */
        if (getIntent().hasExtra("LlamadaDesdeNotificacion")){
            Editor.putInt("FragmentoSeleccionado",1);
            Editor.commit();
        }
        DatosUsuario();
        EventoSesion();
        Fragment fragment=SelectLastFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
    private void DatosUsuario(){
        header=navigationView.getHeaderView(0);
        TextView header_username=(TextView)header.findViewById(R.id.header_username);
        TextView header_email=(TextView)header.findViewById(R.id.header_email);
        header_username.setText(ShPrSync.getString("username",""));
        header_email.setText(ShPrSync.getString("email", "Iniciar sesión"));
        if (ShPrSync.getInt("id_usuario",0)==0){
            header_email.setTextSize(25);
            navigationView.getMenu().findItem(R.id.sync).setVisible(false);
            navigationView.getMenu().findItem(R.id.close_session).setVisible(false);
        }
        else{
            header_email.setTextSize(15);
            navigationView.getMenu().findItem(R.id.sync).setVisible(true);
            navigationView.getMenu().findItem(R.id.close_session).setVisible(true);
        }
    }
    private void EventoSesion(){
        LinearLayout header_linearlayout=(LinearLayout) header.findViewById(R.id.header_linearlayout);
        header_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShPrSync.getInt("id_usuario",0)==0){
                    Intent login=new Intent(Principal.this, Login.class);
                    startActivity(login);
                }
            }
        });
    }
    /*La razon de ser de este metodo es debido a que esta actividad fue definida como singleTask, eso implica que cuando llega la notificación de escribir gastos y esta actividad sigue en la pila de procesos, Android no la volvera a crear y por lo tanto los datos del paquete que envia el intent que manda la notificacion ("LlamadaDesdeNotificacion" que sirve para usar el fragmento_gastos en esta actividad) nunca seran recuperados.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        drawer.closeDrawer(GravityCompat.START);
        DatosUsuario();
        if (intent.hasExtra("LlamadaDesdeNotificacion")){
            Editor.putInt("FragmentoSeleccionado",1);
            Editor.commit();
            Fragment fragment=SelectLastFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        //now getIntent() should always return the last received intent
    }
   Fragment SelectLastFragment(){
       MenuItem item;
       Fragment fr=null;
        switch (ShPrFragments.getInt("FragmentoSeleccionado",1)){
            case 1:
                item = navigationView.getMenu().findItem(R.id.it1);
                item.setChecked(true);
                CurrentFragment=item.getItemId();
                fr=new fragmento_gastos();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 2:
                item = navigationView.getMenu().findItem(R.id.it2);
                item.setChecked(true);
                CurrentFragment=item.getItemId();
                fr=new fragmento_finanzas();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 3:
                item = navigationView.getMenu().findItem(R.id.it3);
                item.setChecked(true);
                CurrentFragment=item.getItemId();
                fr=new fragmento_notas();
                getSupportActionBar().setTitle(item.getTitle());
                break;
            case 4:
                item = navigationView.getMenu().findItem(R.id.it4);
                item.setChecked(true);
                CurrentFragment=item.getItemId();
                fr=new fragmento_notas_eliminadas();
                getSupportActionBar().setTitle(item.getTitle());
        }
       return fr;
    }
    void checarPermisos(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Permiso_De_Escritura);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Permiso_De_Escritura:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El permiso ha sido otorgado :D

                }
                else {
                    // permission denied, boo!
                    Toast.makeText(this.getBaseContext(),"Por favor activa el permiso, es indispensable", Toast.LENGTH_SHORT).show();
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
        if (item.getItemId()==CurrentFragment){
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        switch (item.getItemId()) {
            case R.id.it1:
                fragment = new fragmento_gastos();
                Editor.putInt("FragmentoSeleccionado",1);
                CurrentFragment=item.getItemId();
                break;
            case R.id.it2:
                fragment = new fragmento_finanzas();
                Editor.putInt("FragmentoSeleccionado",2);
                CurrentFragment=item.getItemId();
                break;
            case R.id.it3:
                fragment = new fragmento_notas();
                Editor.putInt("FragmentoSeleccionado",3);
                CurrentFragment=item.getItemId();
                break;
            case R.id.it4:
                fragment = new fragmento_notas_eliminadas();
                Editor.putInt("FragmentoSeleccionado",4);
                CurrentFragment=item.getItemId();
                break;
            case R.id.schedule:
                Intent i = new Intent(this, Programacion_horarios.class);
                startActivity(i);
                return false;
            case R.id.about:
                builder=new AlertDialog.Builder(this);
                builder.setTitle("Notas de MigueLopez :D");
                builder.setMessage("Esta App fue programada por Miguel Ángel López Arcos x'D");
                mensaje=builder.create();
                mensaje.show();
                return false;
            case R.id.sync:
                /***
                 * Se envia la instancia del framento que actualmente se esta mostrando a la clase Sincronizacion para que alla se ejecute el metodo onResume() de cualquier fragment y se actualicen las notas en tiempo real (se debe hacer forzosamente en el success de la clase sync ya que de no ser asi, al ser una peticion aincrona el codigo de actualizar BD se ejecutaria inmediatamente despues y no se mostrarianb los cambios
                 */
                Fragment fragmento=getSupportFragmentManager().findFragmentById(R.id.content_frame);
                //Volley_Singleton.getInstance(this).syncDBLocal_Remota(fragmento);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            case R.id.close_session:
                builder=new AlertDialog.Builder(this);
                builder.setMessage("¿Estás seguro de que quieres cerrar tu sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CerrarSesion();
                                getSupportFragmentManager().findFragmentById(R.id.content_frame).onResume();
                                drawer.closeDrawer(GravityCompat.START);
                                DatosUsuario();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                mensaje=builder.create();
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
    public void CerrarSesion(){
        ShPrSync.edit().clear();
        ShPrSync.edit().commit();
        Database.getInstance(this).VaciarNotas();
        //packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

}
