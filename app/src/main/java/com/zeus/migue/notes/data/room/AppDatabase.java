package com.zeus.migue.notes.data.room;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zeus.migue.notes.data.room.entities.ClipItem;
import com.zeus.migue.notes.data.room.entities.Note;
import com.zeus.migue.notes.infrastructure.dao.ClipsDao;
import com.zeus.migue.notes.infrastructure.dao.NotesDao;

@Database(
        entities = {Note.class, ClipItem.class},
        version = 3,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String PATH = "Database.db";
    private static String externalStoragePath;
    private static AppDatabase Instance;

    public abstract NotesDao notesDao();
    public abstract ClipsDao clipsDao();

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
                .addMigrations(new Migration(2, 3) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {

                    }
                })
                .build();
    }

}
