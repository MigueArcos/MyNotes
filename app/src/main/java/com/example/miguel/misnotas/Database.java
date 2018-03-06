package com.example.miguel.misnotas;

/**
 * Created by Miguel on 13/06/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.content.SyncInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import com.example.miguel.misnotas.adapters.FinancesAdapter;
import com.example.miguel.misnotas.models.Finance;
import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.models.SyncData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Miguel on 01/09/2015.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 26;
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
    public static final String NOTE_DELETED = "deleted";
    public static final String NOTE_UPLOADED = "uploaded";
    public static final String NOTE_MODIFIED = "modified";
    //Index of the columns
    private Map<String, Integer> columnsIndexes = new HashMap<String, Integer>() {{
        put(NOTE_ID, 0);
        put(NOTE_TITLE, 1);
        put(NOTE_CONTENT, 2);
        put(NOTE_CREATION_DATE, 3);
        put(NOTE_MODIFICATION_DATE, 4);
        put(NOTE_DELETED, 5);
        put(NOTE_UPLOADED, 6);
        put(NOTE_MODIFIED, 7);
    }};

    private Database(Context context) {
        super(context, PATH, null, DATABASE_VERSION);
        AppContext = context;
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

        db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (" + NOTE_ID + " INTEGER PRIMARY KEY, " + NOTE_TITLE + " VARCHAR(100), " + NOTE_CONTENT + " TEXT, " + NOTE_CREATION_DATE + " INTEGER, " + NOTE_MODIFICATION_DATE + " INTEGER, " + NOTE_DELETED + " INTEGER, " + NOTE_UPLOADED + " INTEGER, " + NOTE_MODIFIED +" INTEGER)");

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
        //db.execSQL("DROP TABLE notas");
        //db.execSQL("ALTER TABLE Notes RENAME TO Notes_Backup");
        //db.execSQL("ALTER TABLE notes RENAME TO notes_backup");
        //db.execSQL("DROP TABLE notes");
        //db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (" + NOTE_ID + " INTEGER PRIMARY KEY, " + NOTE_TITLE + " VARCHAR(100), " + NOTE_CONTENT + " TEXT, " + NOTE_CREATION_DATE + " INTEGER, " + NOTE_MODIFICATION_DATE + " INTEGER, " + NOTE_DELETED + " INTEGER, " + NOTE_UPLOADED + " INTEGER, " + NOTE_MODIFIED +" INTEGER)");
        //db.execSQL("ALTER TABLE notes ADD COLUMN modified INTEGER");
        //db.execSQL("UPDATE notes SET modified = 0");
        db.execSQL("UPDATE notes SET uploaded = 0 WHERE note_id < 30");
        //String SQL = "SELECT " + NOTE_ID + ", " + NOTE_TITLE + ", " + NOTE_CONTENT + ", " + NOTE_CREATION_DATE + ", CAST(" + NOTE_ORDER_MODIFICATION_DATE + " AS INT), " + NOTE_DELETED + ", " + NOTE_UPLOADED + " FROM notes_backup";
       /*Cursor cursor = db.rawQuery(SQL, null);
        while (cursor.moveToNext()) {
            int     note_id = cursor.getInt(columnsIndexes.get(NOTE_ID));
            String  title = cursor.getString(columnsIndexes.get(NOTE_TITLE)),
                    content = cursor.getString(columnsIndexes.get(NOTE_CONTENT));
            long    order_modification_date = cursor.getLong(4) * 1000,
                    creation_date = timeFormatToTimeStamp(cursor.getString(columnsIndexes.get(NOTE_CREATION_DATE)));

            int     deleted = charToIntBoolean(cursor.getString(5)),
                    uploaded = charToIntBoolean(cursor.getString(6));
            db.execSQL(String.format(Locale.US, "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', %d, %d, %d, %d)", NOTES_TABLE_NAME, NOTE_ID, NOTE_TITLE, NOTE_CONTENT, NOTE_CREATION_DATE, NOTE_MODIFICATION_DATE, NOTE_DELETED, NOTE_UPLOADED, note_id, title.replace("'", "\'\'"), content.replace("'", "\'\'"), creation_date, order_modification_date, deleted, uploaded));
        }
        //db.execSQL("DROP TABLE notas"
        */
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


    public List<Note> getNotes(boolean deletedNotes) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String SQL = String.format(Locale.US, "SELECT * FROM %s WHERE %s = %d ORDER BY %s DESC", NOTES_TABLE_NAME, NOTE_DELETED, deletedNotes ? 1 : 0, NOTE_MODIFICATION_DATE);
        Cursor cursor = db.rawQuery(SQL, null);
        while (cursor.moveToNext()) {
            notes.add(new Note(
                    cursor.getInt(columnsIndexes.get(NOTE_ID)),
                    cursor.getString(columnsIndexes.get(NOTE_TITLE)),
                    cursor.getString(columnsIndexes.get(NOTE_CONTENT)),
                    cursor.getLong(columnsIndexes.get(NOTE_CREATION_DATE)),
                    cursor.getLong(columnsIndexes.get(NOTE_MODIFICATION_DATE))
            ));
        }
        cursor.close();
        db.close();
        return notes;
    }


    //Unable to use ContentValues in this method because the column name of id must be named _id (Too Many changes)
    public int saveNote(String title, String content) {
        title = title.isEmpty() ? AppContext.getString(R.string.activity_editor_note_placeholder) : title;
        title = title.replace("'", "\'\'");
        content = content.replace("'", "\'\'");
        String SQL = String.format(Locale.US, "INSERT INTO %s VALUES ((SELECT MAX(%s) FROM %s)+1, '%s', '%s', %d, %d, 0, 0, 0)", NOTES_TABLE_NAME, NOTE_ID, NOTES_TABLE_NAME, title, content, System.currentTimeMillis(), System.currentTimeMillis());
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL);
        int noteId;
        Cursor cursor = db.rawQuery("SELECT MAX(" + NOTE_ID + ") FROM " + NOTES_TABLE_NAME, null);
        cursor.moveToFirst();
        noteId = cursor.getInt(0);
        cursor.close();
        db.close();
        return noteId;
    }


    public void modifyNote(String title, String content, int noteId) {
        title = title.isEmpty() ? AppContext.getString(R.string.activity_editor_note_placeholder) : title;
        //Not necessary since ContentValues scapes automatically the strings
         /*
        title = title.replace("'", "\'\'");
        content = content.replace("'", "\'\'");
        */
        ContentValues note = new ContentValues();
        note.put(NOTE_TITLE, title);
        note.put(NOTE_CONTENT, content);
        note.put(NOTE_MODIFICATION_DATE, System.currentTimeMillis());
        note.put(NOTE_MODIFIED, 1);
        SQLiteDatabase db = getWritableDatabase();
        db.update(NOTES_TABLE_NAME, note, NOTE_ID + " = " + noteId, null);
        db.close();
    }


    public void deleteNote(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues note = new ContentValues();
        note.put(NOTE_MODIFICATION_DATE, System.currentTimeMillis());
        note.put(NOTE_DELETED, 1);
        note.put(NOTE_MODIFIED, 1);
        db.update(NOTES_TABLE_NAME, note, NOTE_ID + " = " + noteId, null);
        db.close();
    }


    public void recoverNote(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues note = new ContentValues();
        note.put(NOTE_MODIFICATION_DATE, System.currentTimeMillis());
        note.put(NOTE_DELETED, 0);
        note.put(NOTE_MODIFIED, 1);
        db.update(NOTES_TABLE_NAME, note, NOTE_ID + " = " + noteId, null);
        db.close();
    }


    public void deleteNoteCompletely(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOTES_TABLE_NAME, NOTE_ID + " = " + noteId, null);
        db.close();
    }

    //Este método se usara cuando se cierre la sesión y las notas del usuario que la cerro seran borradas
    public void emptySyncedNotes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOTES_TABLE_NAME, NOTE_UPLOADED + " = 1", null);
        db.close();
    }

    public void deleteUnsyncedNotes(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOTES_TABLE_NAME, NOTE_UPLOADED + " = 0", null);

    }

    public void deleteAllNotes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOTES_TABLE_NAME, null, null);
        db.close();
    }

    /***************************************

     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     |                              Métodos pare envio/recepción de datos a host remoto (tabla notas)                          |
     +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

     **************************************/

    public SyncData createLocalSyncData(final SyncData.SyncInfo syncInfo) {
        SyncData syncData = new SyncData();
        List<Note> newNotes = new ArrayList<>();
        List<Note> modifiedNotes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String SQL = "SELECT * FROM " + NOTES_TABLE_NAME;
        Cursor cursor = db.rawQuery(SQL, null);

        while (cursor.moveToNext()) {
            Note note = new Note(
                    cursor.getInt(columnsIndexes.get(NOTE_ID)),
                    cursor.getString(columnsIndexes.get(NOTE_TITLE)),
                    cursor.getString(columnsIndexes.get(NOTE_CONTENT)),
                    cursor.getLong(columnsIndexes.get(NOTE_CREATION_DATE)),
                    cursor.getLong(columnsIndexes.get(NOTE_MODIFICATION_DATE)),
                    cursor.getInt(columnsIndexes.get(NOTE_DELETED)),
                    cursor.getInt(columnsIndexes.get(NOTE_UPLOADED)),
                    cursor.getInt(columnsIndexes.get(NOTE_MODIFIED))
            );
            if (note.getUploaded() == 0){
                newNotes.add(note);
            }
            if (note.getUploaded() == 1 && note.getModified() == 1){
                modifiedNotes.add(note);
            }
        }
        db.close();
        syncData.setSyncInfo(syncInfo);
        syncData.setModifiedNotes(modifiedNotes);
        syncData.setNewNotes(newNotes);
        return syncData;
    }

    /*@Deprecated
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
    }*/

    public SyncData.SyncInfo updateLocalDatabase(SyncData localSyncData, SyncData remoteSyncData) {

        int lastSyncedId = remoteSyncData.getSyncInfo().getLastSyncedId();
        SQLiteDatabase db = getWritableDatabase();
        //Local NewNotes and ModifiedNotes List will always gonna exist because in the method of createLocalSyncData we initialized them
        if (localSyncData.getNewNotes().size() > 0){
            deleteUnsyncedNotes();
            for (Note localNote : localSyncData.getNewNotes()) {
                String insertSQL = String.format(Locale.US, "INSERT INTO %s VALUES (%d, '%s', '%s', %d, %d, %d, 1, 0)", NOTES_TABLE_NAME, lastSyncedId +1, localNote.getTitle(), localNote.getContent(), localNote.getCreationDate(), localNote.getModificationDate(), localNote.getDeleted());
                db.execSQL(insertSQL);
                lastSyncedId++;
            }
        }
        //All server notes must be inserted in the same order as they arrived
        if (remoteSyncData.getNewNotes() != null){
            for (Note serverNote : remoteSyncData.getNewNotes()) {
                String insertSQL = String.format(Locale.US, "INSERT INTO %s VALUES (%d, '%s', '%s', %d, %d, %d, 1, 0)", NOTES_TABLE_NAME, serverNote.getNoteId(), serverNote.getTitle(), serverNote.getContent(), serverNote.getCreationDate(), serverNote.getModificationDate(), serverNote.getDeleted());
                db.execSQL(insertSQL);
            }
        }

        db.close();

        remoteSyncData.getSyncInfo().setLastSyncedId(lastSyncedId);
        return remoteSyncData.getSyncInfo();
    }


}