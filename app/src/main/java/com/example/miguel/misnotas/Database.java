package com.example.miguel.misnotas;

/**
 * Created by Miguel on 13/06/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.miguel.misnotas.adapters.FinancesAdapter;
import com.example.miguel.misnotas.models.Finance;
import com.example.miguel.misnotas.models.Note;
import com.example.miguel.misnotas.models.SyncData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Miguel on 01/09/2015.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
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
    public static final String NOTE_PENDING_CHANGES = "pending_changes";

    //Notes_Log Table -> Columns
    public static final String NOTES_LOG_TABLE_NAME = "notes_log";
    public static final String NOTES_LOG_ID = "note_id";
    //This column won't be used in this moment, but maybe later we will use it to manage the pending changes using this table
    public static final String NOTES_LOG_STATUS = "status";
    //Index of the columns
    private Map<String, Integer> columnsIndexes = new HashMap<String, Integer>() {{
        put(NOTE_ID, 0);
        put(NOTE_TITLE, 1);
        put(NOTE_CONTENT, 2);
        put(NOTE_CREATION_DATE, 3);
        put(NOTE_MODIFICATION_DATE, 4);
        put(NOTE_DELETED, 5);
        put(NOTE_UPLOADED, 6);
        put(NOTE_PENDING_CHANGES, 7);
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

    public static Database getInstance() {
        return Instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la tabla activos
        db.execSQL("CREATE TABLE activos(id INTEGER PRIMARY KEY AUTOINCREMENT, recurso TEXT NOT NULL, valor INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE bitacora(fecha DATETIME PRIMARY KEY, total_en_fecha INTEGER)");

        db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (" + NOTE_ID + " INTEGER PRIMARY KEY, " + NOTE_TITLE + " VARCHAR(100), " + NOTE_CONTENT + " TEXT, " + NOTE_CREATION_DATE + " INTEGER, " + NOTE_MODIFICATION_DATE + " INTEGER, " + NOTE_DELETED + " INTEGER, " + NOTE_UPLOADED + " INTEGER, " + NOTE_PENDING_CHANGES + " INTEGER)");

        db.execSQL("CREATE TABLE " + NOTES_LOG_TABLE_NAME + " (" + NOTES_LOG_ID + " INTEGER PRIMARY KEY, " + NOTES_LOG_STATUS + " INTEGER DEFAULT 0)");

        db.execSQL(
                "CREATE TRIGGER log_deleted_note BEFORE DELETE \n" +
                        "ON " + NOTES_TABLE_NAME + "\n" +
                        "WHEN old." + NOTE_UPLOADED + " = 1\n" +
                        "BEGIN\n" +
                        "INSERT INTO " + NOTES_LOG_TABLE_NAME + " (" + NOTES_LOG_ID + ") VALUES (old." + NOTE_ID + ");\n" +
                        "END;"
        );

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

    public List<Finance> getFinances() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, recurso, valor FROM activos", null);
//result.add("hijueputa");
        List<Finance> finances = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id, value;
            String name;
            id = cursor.getInt(0);
            name = cursor.getString(1);
            value = cursor.getInt(2);
            finances.add(new Finance(name, value, R.drawable.mercedes_benz_logo, id));
        }

        cursor.close();
        db.close();
        return finances;
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

    public Finance saveFinance(String name, int value) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO activos VALUES (null, '" + name + "', " + value + ")");
        Finance finance = new Finance();
        Cursor cursor = db.rawQuery("SELECT * FROM activos WHERE id = (SELECT max(id) from activos)", null);
        while (cursor.moveToNext()) {
            finance.setNombre(name);
            finance.setValor(value);
            finance.setDrawableImageID(R.drawable.mercedes_benz_logo);
        }
        cursor.close();
        db.close();
        return finance;
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
        note.put(NOTE_PENDING_CHANGES, 1);
        SQLiteDatabase db = getWritableDatabase();
        db.update(NOTES_TABLE_NAME, note, NOTE_ID + " = " + noteId, null);
        db.close();
    }


    public void deleteNote(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues note = new ContentValues();
        note.put(NOTE_MODIFICATION_DATE, System.currentTimeMillis());
        note.put(NOTE_DELETED, 1);
        note.put(NOTE_PENDING_CHANGES, 1);
        db.update(NOTES_TABLE_NAME, note, NOTE_ID + " = " + noteId, null);
        db.close();
    }


    public void recoverNote(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues note = new ContentValues();
        note.put(NOTE_MODIFICATION_DATE, System.currentTimeMillis());
        note.put(NOTE_DELETED, 0);
        note.put(NOTE_PENDING_CHANGES, 1);
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
        db.execSQL("DELETE FROM " + NOTES_LOG_TABLE_NAME);
        db.close();
    }

    public void deleteUnsyncedNotes() {
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
        List<Integer> idsToDelete = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursorNewNotes = db.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTE_UPLOADED + " = 0", null);

        if (cursorNewNotes.getCount() > 0) {
            while (cursorNewNotes.moveToNext()) {
                Note note = new Note(
                        cursorNewNotes.getInt(columnsIndexes.get(NOTE_ID)),
                        cursorNewNotes.getString(columnsIndexes.get(NOTE_TITLE)),
                        cursorNewNotes.getString(columnsIndexes.get(NOTE_CONTENT)),
                        cursorNewNotes.getLong(columnsIndexes.get(NOTE_CREATION_DATE)),
                        cursorNewNotes.getLong(columnsIndexes.get(NOTE_MODIFICATION_DATE)),
                        cursorNewNotes.getInt(columnsIndexes.get(NOTE_DELETED)),
                        cursorNewNotes.getInt(columnsIndexes.get(NOTE_UPLOADED)),
                        cursorNewNotes.getInt(columnsIndexes.get(NOTE_PENDING_CHANGES))
                );
                newNotes.add(note);
            }
            syncData.setNewNotes(newNotes);
        }
        cursorNewNotes.close();

        Cursor cursorModifiedNotes = db.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTE_UPLOADED + " = 1 AND " + NOTE_PENDING_CHANGES + " = 1", null);

        if (cursorModifiedNotes.getCount() > 0) {
            while (cursorModifiedNotes.moveToNext()) {
                Note note = new Note(
                        cursorModifiedNotes.getInt(columnsIndexes.get(NOTE_ID)),
                        cursorModifiedNotes.getString(columnsIndexes.get(NOTE_TITLE)),
                        cursorModifiedNotes.getString(columnsIndexes.get(NOTE_CONTENT)),
                        cursorModifiedNotes.getLong(columnsIndexes.get(NOTE_CREATION_DATE)),
                        cursorModifiedNotes.getLong(columnsIndexes.get(NOTE_MODIFICATION_DATE)),
                        cursorModifiedNotes.getInt(columnsIndexes.get(NOTE_DELETED)),
                        cursorModifiedNotes.getInt(columnsIndexes.get(NOTE_UPLOADED)),
                        cursorModifiedNotes.getInt(columnsIndexes.get(NOTE_PENDING_CHANGES))
                );
                modifiedNotes.add(note);
            }
            syncData.setModifiedNotes(modifiedNotes);
        }
        cursorModifiedNotes.close();

        Cursor cursorIdsToDelete = db.rawQuery("SELECT * FROM " + NOTES_LOG_TABLE_NAME, null);

        if (cursorIdsToDelete.getCount() > 0) {
            while (cursorIdsToDelete.moveToNext()) {
                idsToDelete.add(cursorIdsToDelete.getInt(0));
            }
            syncData.setIdsToDelete(idsToDelete);
        }
        cursorIdsToDelete.close();

        db.close();
        syncData.setSyncInfo(syncInfo);
        return syncData;
    }

    public SyncData.SyncInfo updateLocalDatabase(SyncData localSyncData, SyncData remoteSyncData) {

        int lastSyncedId = remoteSyncData.getSyncInfo().getLastSyncedId();
        SQLiteDatabase db = getWritableDatabase();

        if (localSyncData.getNewNotes() != null) {
            //We must delete all previous notes due to problems with primary key constraint
            deleteUnsyncedNotes();
            for (Note localNote : localSyncData.getNewNotes()) {
                String insertSQL = String.format(Locale.US, "INSERT INTO %s VALUES (%d, '%s', '%s', %d, %d, %d, 1, 0)", NOTES_TABLE_NAME, lastSyncedId + 1, localNote.getTitle().replace("'", "\'\'"), localNote.getContent().replace("'", "\'\'"), localNote.getCreationDate(), localNote.getModificationDate(), localNote.getDeleted());
                db.execSQL(insertSQL);
                lastSyncedId++;
            }
        }
        //All server notes must be inserted in the same order as they arrived
        if (remoteSyncData.getNewNotes() != null) {
            for (Note serverNote : remoteSyncData.getNewNotes()) {
                String insertSQL = String.format(Locale.US, "INSERT INTO %s VALUES (%d, '%s', '%s', %d, %d, %d, 1, 0)", NOTES_TABLE_NAME, serverNote.getNoteId(), serverNote.getTitle().replace("'", "\'\'"), serverNote.getContent().replace("'", "\'\'"), serverNote.getCreationDate(), serverNote.getModificationDate(), serverNote.getDeleted());
                db.execSQL(insertSQL);
            }
        }

        if (remoteSyncData.getModifiedNotes() != null) {
            for (Note modifiedNote : remoteSyncData.getModifiedNotes()) {
                ContentValues note = new ContentValues();

                note.put(NOTE_ID, modifiedNote.getNoteId());
                note.put(NOTE_TITLE, modifiedNote.getTitle());
                note.put(NOTE_CONTENT, modifiedNote.getContent());
                note.put(NOTE_CREATION_DATE, modifiedNote.getCreationDate());
                note.put(NOTE_MODIFICATION_DATE, modifiedNote.getModificationDate());
                note.put(NOTE_DELETED, modifiedNote.getDeleted());
                note.put(NOTE_UPLOADED, 1);
                note.put(NOTE_PENDING_CHANGES, 0);

                db.replace(NOTES_TABLE_NAME, null, note);
            }
        }

        if (remoteSyncData.getIdsToDelete() != null) {
            for (int id : remoteSyncData.getIdsToDelete()) {
                deleteNoteCompletely(id);
            }
        }
        //On successful response we must delete all local notes id's that have already been deleted on server side
        db.execSQL("DELETE FROM " + NOTES_LOG_TABLE_NAME);

        db.close();

        remoteSyncData.getSyncInfo().setLastSyncedId(lastSyncedId);
        return remoteSyncData.getSyncInfo();
    }


}