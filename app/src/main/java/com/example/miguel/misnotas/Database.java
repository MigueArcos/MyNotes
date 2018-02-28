package com.example.miguel.misnotas;

/**
 * Created by Miguel on 13/06/2016.
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.miguel.misnotas.adapters.FinancesAdapter;
import com.example.miguel.misnotas.models.Finance;
import com.example.miguel.misnotas.models.Note;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Miguel on 01/09/2015.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 21;
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/Mis_Notas.db";
    private static Database Instance;
    private static Context AppContext;


    //Notes Table -> Columns
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTE_ID = "note_id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_CONTENT = "content";
    public static final String NOTE_CREATION_DATE = "creation_date";
    public static final String NOTE_MODIFICATION_DATE = "modification_date";
    public static final String NOTE_ORDER_MODIFICATION_DATE = "order_modification_date";
    public static final String NOTE_DELETED = "deleted";
    public static final String NOTE_UPLOADED = "uploaded";

    private Database(Context context) {
        super(context, PATH, null, DATABASE_VERSION);
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

        db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (" + NOTE_ID + " INTEGER PRIMARY KEY, " + NOTE_TITLE + " VARCHAR(100), " + NOTE_CONTENT + " TEXT, " + NOTE_CREATION_DATE + " VARCHAR(25), " + NOTE_MODIFICATION_DATE + " VARCHAR(25), " + NOTE_ORDER_MODIFICATION_DATE + " VARCHAR(20), " + NOTE_DELETED + " CHAR(1), " + NOTE_UPLOADED + " CHAR(1))");

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
        //db.execSQL("INSERT INTO Notes VALUES(0,'Nota Fantasma', 'Phantom Note', '1975-01-01', '1975-01-01', '0000000000', 'S','N')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(MyUtils.GLOBAL_LOG_TAG, "Trying to upgrade database");
        db.execSQL("DROP TABLE notes");
        //db.execSQL("ALTER TABLE Notes RENAME TO Notes_Backup");
        db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (" + NOTE_ID + " INTEGER PRIMARY KEY, " + NOTE_TITLE + " VARCHAR(100), " + NOTE_CONTENT + " TEXT, " + NOTE_CREATION_DATE + " VARCHAR(25), " + NOTE_MODIFICATION_DATE + " VARCHAR(25), " + NOTE_ORDER_MODIFICATION_DATE + " VARCHAR(20), " + NOTE_DELETED + " CHAR(1), " + NOTE_UPLOADED + " CHAR(1))");
        db.execSQL("INSERT INTO " + NOTES_TABLE_NAME + " (" + NOTE_ID + ", " + NOTE_TITLE + ", " + NOTE_CONTENT + ", " + NOTE_CREATION_DATE + ", " + NOTE_MODIFICATION_DATE + ", " + NOTE_ORDER_MODIFICATION_DATE + ", " + NOTE_DELETED + ", " + NOTE_UPLOADED + ") SELECT * FROM Notes_Backup");
        db.execSQL("DROP TABLE Notes_Backup");

    }

    String Tiempo_12_Horas(String Tiempo_24_Horas, int Longitud_Hora) {
        int Hora_F24 = Integer.parseInt(Tiempo_24_Horas.substring(0, 2));
        return (Hora_F24 >= 12) ? ((Hora_F24 + 11) % 12 + 1) + Tiempo_24_Horas.substring(2, Longitud_Hora) + AppContext.getString(R.string.pm_format) :
                ((Hora_F24 + 11) % 12 + 1) + Tiempo_24_Horas.substring(2, Longitud_Hora) + AppContext.getString(R.string.am_format);
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

        while (cursor.moveToNext()) {
            int id, valor;
            String recurso;
            id = cursor.getInt(0);
            recurso = cursor.getString(1);
            valor = cursor.getInt(2);
            result.add(new Finance(recurso, valor, R.drawable.mercedes_benz_logo, id));
        }

        cursor.close();
        db.close();
        return result;
    }

    public int guardar_y_mandar_id(String recurso, int valor) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO activos VALUES (null, '" + recurso + "', " + valor + ")");
        int id = 0;
        Cursor cursor = db.rawQuery("SELECT max(id) from activos", null);
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public void eliminarrecurso(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from activos where id=" + id);

        db.close();
    }

    public void modificar(String rec, int value, int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update activos set recurso='" + rec + "',valor=" + value + " where id=" + id);
        db.close();
    }

    public String sumatoria() {
        String suma = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sum(valor) from activos", null);
        while (cursor.moveToNext()) {
            suma = "" + cursor.getInt(0);
        }
        return suma;
    }

    public String[] mod(int id_base) {
        String arr[] = new String[2];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT recurso, valor from activos where id=" + id_base + "", null);
        while (cursor.moveToNext()) {
            arr[0] = cursor.getString(0);
            arr[1] = "" + cursor.getInt(1);
        }
        cursor.close();
        db.close();
        return arr;
    }

    public String LastUpdate() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT max(fecha) from bitacora", null);
        String fecha = AppContext.getString(R.string.last_update_label);
        while (cursor.moveToNext()) {
            String d = cursor.getString(0);
            fecha += d.substring(8, 10) + "/" + d.substring(5, 7) + "/" + d.substring(0, 4) + " a las " + Tiempo_12_Horas(d.substring(11, 16), 5);
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


    public List<Note> getNotes(boolean deletedNotes){
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String SQL = String.format("SELECT * FROM %s WHERE deleted='%c' ORDER BY %s DESC", NOTES_TABLE_NAME, deletedNotes ? 'S' : 'N', NOTE_ORDER_MODIFICATION_DATE);
        Cursor cursor = db.rawQuery(SQL, null);
        while (cursor.moveToNext()) {
            notes.add(new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4)));
        }
        cursor.close();
        db.close();
        return notes;
    }

    public int guardar_nota(String title, String content) {
        String titu = (title.equals("")) ? AppContext.getString(R.string.activity_editor_note_placeholder) : title;
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec = df.format(dateobj);
        titu = titu.replace("'", "\'\'");
        content = content.replace("'", "\'\'");
        SQLiteDatabase db = getWritableDatabase();
        String SQL = "INSERT INTO Notes VALUES ((SELECT MAX(noteId) FROM Notes)+1, '" + titu + "','" + content + "','" + fec + "','" + fec + "','" + (dateobj.getTime() / 1000) + "','N', 'N')";
        db.execSQL(SQL);
        int noteId;
        Cursor cursor = db.rawQuery("SELECT MAX(noteId) FROM Notes", null);
        cursor.moveToFirst();
        noteId = cursor.getInt(0);
        cursor.close();
        db.close();
        return noteId;
    }

    public void modificar_nota(String title, String content, int noteId) {
        String titu = (title.equals("")) ? AppContext.getString(R.string.activity_editor_note_placeholder) : title;
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec = df.format(dateobj);
        SQLiteDatabase db = getWritableDatabase();
        titu = titu.replace("'", "\'\'");
        content = content.replace("'", "\'\'");
        String SQL = "UPDATE Notes SET title='" + titu + "', content='" + content + "', modificationDate='" + fec + "',orderModificationDate='" + (dateobj.getTime() / 1000) + "' WHERE noteId=" + noteId;
        db.execSQL(SQL);
        db.close();
    }

    public void eliminar_nota(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM Notes WHERE id="+noteId);
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec = df.format(dateobj);
        db.execSQL("UPDATE Notes SET deleted='S', modificationDate='" + fec + "',orderModificationDate='" + (dateobj.getTime() / 1000) + "' WHERE noteId=" + noteId);
        //db.execSQL("DELETE FROM Notes WHERE noteId="+noteId);
        db.close();
    }

    public void recuperar_nota(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM Notes WHERE id="+noteId);
        DateFormat df = new SimpleDateFormat(AppContext.getString(R.string.date_format), Locale.US);
        Date dateobj = new Date();
        String fec = df.format(dateobj);
        db.execSQL("UPDATE Notes SET deleted='N', modificationDate='" + fec + "',orderModificationDate='" + (dateobj.getTime() / 1000) + "' WHERE noteId=" + noteId);
        //db.execSQL("DELETE FROM Notes WHERE noteId="+noteId);
        db.close();
    }

    public void eliminar_nota_completamente(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM Notes WHERE id="+noteId);
        db.execSQL("DELETE FROM Notes WHERE noteId=" + noteId);
        //db.execSQL("DELETE FROM Notes WHERE noteId="+noteId);
        db.close();
    }

    //Este método se usara cuando se cierre la sesión y las notas del usuario que la cerro seran borradas
    public void emptySyncedNotes() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Notes WHERE uploaded='S'");
        db.close();
    }

    /***************************************

     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     |                              Métodos pare envio/recepción de datos a host remoto (tabla notas)                          |
     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

     **************************************/

    public String crearJSON(String SQL) {
        ArrayList<Note> notas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL, null);
        if (cursor.getCount() == 0) {
            return "";
        }
        while (cursor.moveToNext()) {
            Note nota_actual = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6).charAt(0), cursor.getString(7).charAt(0));
            notas.add(nota_actual);
        }
        db.close();
        Gson gson = new GsonBuilder().create();
        /*Use GSON to serialize Array List to JSON
          Por defecto GSON serializara el ArrayList con los nombres de los campos que esta en la clase seleccionada (Note)
          Si se quieren utilizar otros nombre se ha de añadir la anotación @SerializedName("Nombre") antes de cada campo
          Si se quieren omitir algunos campos se han de usar la anotación @Expose y luego se usara el método .excludeFieldsWithoutExposeAnnotation() de GsonBuilder()  [Esto es solo una estrategia de exclusión] o añadir la palabra transient o static antes del tipo de variable
        */
        Log.d(MyUtils.GLOBAL_LOG_TAG, gson.toJson(notas));
        return gson.toJson(notas);
    }

    public void NotasServidorALocalDB(JSONArray array, boolean isLogin) {
        //ArrayList<Note> list = new ArrayList<>();
        try {
            if (array.length() > 0) {
                Gson gson = new Gson();
                int i = 0;
                SQLiteDatabase db = getWritableDatabase();
                if (isLogin) {
                    //This line is needed when is a login to guarantee that won´t have been duplicate notes
                    db.execSQL("DELETE FROM Notes");
                }
                while (i < array.length() - 1) {
                    Note actual = gson.fromJson(array.getJSONObject(i).toString(), Note.class);
                    String SQL = "REPLACE INTO Notes (noteId, title, content, creationDate, modificationDate, orderModificationDate,deleted, uploaded) VALUES (" + actual.getNoteId() + ",'" + actual.getTitle().replace("'", "\'\'") + "','" + actual.getContent().replace("'", "\'\'") + "','" + actual.getCreationDate() + "','" + actual.getModificationDate() + "','" + actual.getOrderModificationDate() + "','" + actual.getDeleted() + "','S')";
                    db.execSQL(SQL);
                    i++;
                }
                db.close();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
