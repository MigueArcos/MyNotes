package com.zeus.migue.notes.data.room;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zeus.migue.notes.data.room.entities.Account;
import com.zeus.migue.notes.data.room.entities.ClipNote;
import com.zeus.migue.notes.data.room.entities.DeleteLog;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.dao.AccountsDao;
import com.zeus.migue.notes.infrastructure.dao.ClipNotesDao;
import com.zeus.migue.notes.infrastructure.dao.DeleteLogDao;
import com.zeus.migue.notes.infrastructure.dao.NotesDao;

@Database(
        entities = {Note.class, ClipNote.class, Account.class, DeleteLog.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String PATH = "Database.db";
    private static String externalStoragePath;
    private static AppDatabase Instance;

    public abstract NotesDao notesDao();
    public abstract ClipNotesDao clipsDao();
    public abstract AccountsDao accountsDao();
    public abstract DeleteLogDao deleteLogDao();

    public synchronized static AppDatabase getInstance(Context context) {
        if (Instance == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                externalStoragePath = context.getExternalFilesDir(null).getAbsolutePath();
            } else {
                externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            Instance = buildDatabase(context.getApplicationContext());
        }
        return Instance;
    }

    /*It is possible to use a single trigger to manage account entries details modifications in this way:
    value = value - old.value*(IsExpense or Income) + new.value*(IsExpense or Income)*/
    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                externalStoragePath + "/" + PATH)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        db.execSQL("CREATE TRIGGER AddToLogOnNoteDeletion\n" +
                                "BEFORE DELETE ON Notes\n" +
                                "WHEN old.IsUploaded = 1\n" +
                                "   BEGIN\n" +
                                "       INSERT INTO DeleteLog (CreationDate, RemoteId, EntityName) VALUES (DATETIME('now'), old.RemoteId, 'Note');" +
                                "   END;");
                        db.execSQL("CREATE TRIGGER AddToLogOnClipNoteDeletion\n" +
                                "BEFORE DELETE ON ClipNotes\n" +
                                "WHEN old.IsUploaded = 1\n" +
                                "   BEGIN\n" +
                                "       INSERT INTO DeleteLog (CreationDate, RemoteId, EntityName) VALUES (DATETIME('now'), old.RemoteId, 'ClipNote');" +
                                "   END;");
                    }
                })
                .build();
    }

}
