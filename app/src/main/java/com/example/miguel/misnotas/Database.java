package com.example.miguel.misnotas;

/**
 * Created by Miguel on 13/06/2016.
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.miguel.misnotas.adapters.FinancesAdapter;
import com.example.miguel.misnotas.models.Finance;
import com.example.miguel.misnotas.models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Miguel on 01/09/2015.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DataBase_Version=15;
    private static final String Ruta= Environment.getExternalStorageDirectory().getPath()+"/Mis_Notas.db";
    private static Database Instance;
    private static Context AppContext;
    private Database(Context context){
        super(context, Ruta, null, DataBase_Version);
        this.AppContext = context;
    }
    public static Database getInstance(Context context) {
        if (Instance == null) {
            Instance = new Database(context.getApplicationContext());
        }
        return Instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la tabla activos
        db.execSQL("CREATE TABLE activos(id INTEGER PRIMARY KEY AUTOINCREMENT, recurso TEXT NOT NULL, valor INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE bitacora(fecha DATETIME PRIMARY KEY, total_en_fecha INTEGER)");
        db.execSQL("CREATE TABLE notas (id_nota INTEGER PRIMARY KEY, titulo VARCHAR(100), contenido TEXT, fecha_creacion VARCHAR(25), fecha_modificacion VARCHAR(25),fecha_modificacion_orden VARCHAR(20), eliminado CHAR(1), subida CHAR(1))");
        db.execSQL("CREATE  TRIGGER actualizar AFTER UPDATE  \n" +
                "ON activos\n" +
                "BEGIN\n" +
                "INSERT INTO bitacora VALUES (DATETIME('now','localtime'), (SELECT SUM(valor) FROM activos));\n" +
                "END;");
        db.execSQL("CREATE  TRIGGER actualizar2 AFTER INSERT  \n" +
                "ON activos\n" +
                "BEGIN\n" +
                "INSERT INTO bitacora VALUES (DATETIME('now','localtime'), (SELECT SUM(valor) FROM activos));\n" +
                "END;");
        db.execSQL("CREATE  TRIGGER actualizar3 AFTER DELETE  \n" +
                "ON activos\n" +
                "BEGIN\n" +
                "INSERT INTO bitacora VALUES (DATETIME('now','localtime'), (SELECT SUM(valor) FROM activos));\n" +
                "END;");
        db.execSQL("INSERT INTO bitacora VALUES ('1970-01-01 12:00:00',0)");
        //db.execSQL("INSERT INTO notas VALUES(0,'Nota Fantasma', 'Phantom Note', '1975-01-01', '1975-01-01', '0000000000', 'S','N')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE notas");
        //db.execSQL("CREATE TABLE notas (id integer primary key autoincrement, titulo varchar(100), " +
        //"contenido text, fecha_creacion varchar(25), fecha_modificacion varchar(25))");
        //db.execSQL("DELETE FROM notas");
        //db.execSQL("ALTER TABLE notas ADD COLUMN fecha_modificacion_orden VARCHAR(20)");
        //db.execSQL("UPDATE notas SET fecha_modificacion_orden=STRFTIME('%d/%m/%Y %H:%M:%S','now','localtime')");
		/*for (int i=0; i<20; i++){
		db.execSQL("INSERT INTO notas VALUES (null, 'Nota de prueba "+i+"','Este es el contenido de la note de prueba "+i+":D'," +
		"datetime('now','localtime'),datetime('now','localtime'),STRFTIME('%d/%m/%Y %H:%M:%S','now','localtime'))");
		}*/
        //db.execSQL("ALTER TABLE notas ADD COLUMN eliminado CHAR(1)");
        //db.execSQL("UPDATE notas SET eliminado='N', fecha_modificacion_orden=STRFTIME('%s','now','localtime')");
        /*db.execSQL("CREATE TABLE notas2 (id_nota INTEGER PRIMARY KEY, titulo VARCHAR(100), contenido TEXT, fecha_creacion VARCHAR(25), fecha_modificacion VARCHAR(25),fecha_modificacion_orden VARCHAR(20), eliminado CHAR(1), subida CHAR(1))");
        db.execSQL("INSERT INTO notas2 (id_nota, titulo, contenido, fecha_creacion, fecha_modificacion,fecha_modificacion_orden, eliminado) SELECT id, titulo, contenido, fecha_creacion, fecha_modificacion,fecha_modificacion_orden, eliminado FROM notas");
        db.execSQL("ALTER TABLE notas RENAME TO notas_old");
        db.execSQL("ALTER TABLE notas2 RENAME TO notas;");
        db.execSQL("INSERT INTO notas VALUES(0,'Nota Fantasma', 'Phantom Note', '1975-01-01', '1975-01-01', '0000000000', 'N','N')");*/
        db.execSQL("DELETE FROM notas WHERE id_nota=0");
    }

    String Tiempo_12_Horas(String Tiempo_24_Horas, int Longitud_Hora){
        int Hora_F24=Integer.parseInt(Tiempo_24_Horas.substring(0,2));
        return (Hora_F24>=12)? ((Hora_F24+11)%12+1)+Tiempo_24_Horas.substring(2,Longitud_Hora)+AppContext.getString(R.string.pm_format) :
                ((Hora_F24+11)%12+1)+Tiempo_24_Horas.substring(2,Longitud_Hora)+AppContext.getString(R.string.am_format);
    }

    /***************************************

     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     |                                  Métodos CRUD para las tablas activos y bitacora                                        |
     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

     **************************************/
    public FinancesAdapter leer(FinancesAdapter result) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, recurso, valor FROM activos", null);
//result.add("hijueputa");

        while (cursor.moveToNext()){
            int id, valor;
            String recurso;
            id=cursor.getInt(0);
            recurso=cursor.getString(1);
            valor=cursor.getInt(2);
            result.add(new Finance(recurso, valor, R.drawable.mercedes_benz_logo, id));
        }

        cursor.close();
        db.close();
        return result;
    }
    public int guardar_y_mandar_id(String recurso, int valor) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO activos VALUES (null, '" + recurso + "', " + valor + ")");
        int id=0;
        Cursor cursor = db.rawQuery("SELECT max(id) from activos", null);
        while (cursor.moveToNext()){
            id=cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }
    public void eliminarrecurso(int id){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from activos where id=" + id);

        db.close();
    }
    public void modificar (String rec, int value, int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update activos set recurso='" + rec + "',valor="+value+" where id="+id);
        db.close();
    }
    public String sumatoria(){
        String suma="";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sum(valor) from activos", null);
        while (cursor.moveToNext()){
            suma=""+cursor.getInt(0);
        }
        return suma;
    }

    public String[]  mod(int id_base){
        String arr[]=new String[2];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT recurso, valor from activos where id="+id_base+"", null);
        while (cursor.moveToNext()){
            arr[0]=cursor.getString(0);
            arr[1]=""+cursor.getInt(1);
        }
        cursor.close();
        db.close();
        return arr;
    }
    public String LastUpdate(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT max(fecha) from bitacora", null);
        String fecha=AppContext.getString(R.string.last_update_label);
        while (cursor.moveToNext()){
            String d=cursor.getString(0);
            fecha+=d.substring(8,10)+"/"+d.substring(5,7)+"/"+d.substring(0,4)+" a las "+Tiempo_12_Horas(d.substring(11,16), 5);
        }
        cursor.close();
        db.close();
        return fecha;
    }
    /***************************************

     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     |                                               Métodos CRUD para las tabla notas                                         |
     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

     **************************************/


    public ArrayList<Note> leer_notas(String SQL) {
        ArrayList<Note> notas=new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL, null);
        while (cursor.moveToNext()){
            notas.add(new Note(cursor.getInt(0),cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4)));
        }
        cursor.close();
        db.close();
        return notas;
    }
    public int guardar_nota(String titulo, String contenido) {
        String titu=(titulo.equals(""))? AppContext.getString(R.string.activity_editor_note_placeholder): titulo;
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec=df.format(dateobj);
        titu=titu.replace("'","\'\'");
        contenido=contenido.replace("'","\'\'");
        SQLiteDatabase db = getWritableDatabase();
        String SQL = "INSERT INTO notas VALUES ((SELECT MAX(id_nota) FROM notas)+1, '"+titu+"','"+contenido+"','"+fec+"','"+fec+"','"+(dateobj.getTime()/1000)+"','N', 'N')";
        db.execSQL(SQL);
        int id_nota;
        Cursor cursor = db.rawQuery("SELECT MAX(id_nota) FROM notas", null);
        cursor.moveToFirst();
        id_nota=cursor.getInt(0);
        cursor.close();
        db.close();
        return id_nota;
    }
    public void modificar_nota(String titulo, String contenido, int id_nota) {
        String titu = (titulo.equals(""))? AppContext.getString(R.string.activity_editor_note_placeholder): titulo;
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec=df.format(dateobj);
        SQLiteDatabase db = getWritableDatabase();
        titu=titu.replace("'","\'\'");
        contenido=contenido.replace("'","\'\'");
        String SQL = "UPDATE notas SET titulo='"+titu+"', contenido='"+contenido+"', fecha_modificacion='"+fec+"',fecha_modificacion_orden='"+(dateobj.getTime()/1000)+"' WHERE id_nota="+id_nota;
        db.execSQL(SQL);
        db.close();
    }
    public void eliminar_nota(int id_nota) {
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM notas WHERE id="+id_nota);
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec=df.format(dateobj);
        db.execSQL("UPDATE notas SET eliminado='S', fecha_modificacion='"+fec+"',fecha_modificacion_orden='"+(dateobj.getTime()/1000)+"' WHERE id_nota="+id_nota);
        //db.execSQL("DELETE FROM notas WHERE id_nota="+id_nota);
        db.close();
    }
    public void recuperar_nota(int id_nota) {
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM notas WHERE id="+id_nota);
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec=df.format(dateobj);
        db.execSQL("UPDATE notas SET eliminado='N', fecha_modificacion='"+fec+"',fecha_modificacion_orden='"+(dateobj.getTime()/1000)+"' WHERE id_nota="+id_nota);
        //db.execSQL("DELETE FROM notas WHERE id_nota="+id_nota);
        db.close();
    }
    public void eliminar_nota_completamente(int id_nota) {
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM notas WHERE id="+id_nota);
        db.execSQL("DELETE FROM notas WHERE id_nota="+id_nota);
        //db.execSQL("DELETE FROM notas WHERE id_nota="+id_nota);
        db.close();
    }
    //Este método se usara cuando se cierre la sesión y las notas del usuario que la cerro seran borradas
    public void emptySyncedNotes(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM notas WHERE subida='S'");
        db.close();
    }
    /***************************************

     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     |                              Métodos pare envio/recepción de datos a host remoto (tabla notas)                          |
     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

     **************************************/

    public String crearJSON(String SQL){
        ArrayList<Note> notas=new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL, null);
        if (cursor.getCount()==0){
            return "";
        }
        while (cursor.moveToNext()){
            Note nota_actual=new Note(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6).charAt(0), cursor.getString(7).charAt(0));
            notas.add(nota_actual);
        }
        db.close();
        Gson gson = new GsonBuilder().create();
        /**Use GSON to serialize Array List to JSON
         * Por defecto GSON serializara el ArrayList con los nombres de los campos que esta en la clase seleccionada (Note)
         * Si se quieren utilizar otros nombre se ha de añadir la anotación @SerializedName("Nombre") antes de cada campo
         * Si se quieren omitir algunos campos se han de usar la anotación @Expose y luego se usara el método .excludeFieldsWithoutExposeAnnotation() de GsonBuilder()  [Esto es solo una estrategia de exclusión] o añadir la palabra transient o static antes del tipo de variable
        **/
        return gson.toJson(notas);
    }
    public void NotasServidorALocalDB(JSONArray array, boolean isLogin){
        //ArrayList<Note> list = new ArrayList<>();
        try {
            if (array.length() > 0) {
                Gson gson = new Gson();
                int i = 0;
                SQLiteDatabase db = getWritableDatabase();
                if (isLogin){
                    //This line is needed when is a login to guarantee that won´t have been duplicate notes
                    db.execSQL("DELETE FROM notas");
                }
                while (i < array.length()-1) {
                    Note actual=gson.fromJson(array.getJSONObject(i).toString(), Note.class);
                    String SQL = "REPLACE INTO notas (id_nota, titulo, contenido, fecha_creacion, fecha_modificacion, fecha_modificacion_orden,eliminado, subida) VALUES ("+actual.getID_Nota()+",'"+actual.getTitulo().replace("'","\'\'")+"','"+actual.getContenido().replace("'","\'\'")+"','"+actual.getFecha_creacion()+"','"+actual.getFecha_modificacion()+"','"+actual.getFecha_modificacion_orden()+"','"+actual.getEliminado()+"','S')";
                    db.execSQL(SQL);
                    i++;
                }
                db.close();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
