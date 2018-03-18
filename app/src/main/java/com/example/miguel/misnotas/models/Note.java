package com.example.miguel.misnotas.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.R;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Miguel on 20/06/2016.
 */
public class Note implements Parcelable{
    //Por el momento noteId es estático y por lo tanto no tiene sentido serializarlo para crear el objeto JSON
    /*Check this link to see properties of serialization wit Gson (First answer)
    https://stackoverflow.com/questions/14644860/why-static-fields-not-serialized-using-google-gson-gsonbuilder-json-parser*/
    public static final int imageId = R.drawable.note;
    @SerializedName(Database.NOTE_ID)
    private int noteId;
    @SerializedName(Database.NOTE_TITLE)
    private String title;
    @SerializedName(Database.NOTE_CONTENT)
    private String content;
    @SerializedName(Database.NOTE_CREATION_DATE)
    private long creationDate;
    @SerializedName(Database.NOTE_MODIFICATION_DATE)
    private long modificationDate;
    @SerializedName(Database.NOTE_DELETED)
    private int deleted;
    @SerializedName(Database.NOTE_UPLOADED)
    private int uploaded;
    @SerializedName(Database.NOTE_PENDING_CHANGES)
    private int modified;

    public Note(int noteId, String title, String content, long creationDate, long modificationDate) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public Note(int noteId, String title, String content, long creationDate, long modificationDate, int deleted, int uploaded, int modified) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.deleted = deleted;
        this.uploaded = uploaded;
        this.modified = modified;
    }

    public Note(int noteId, String title, String content, long modificationDate) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.modificationDate = modificationDate;
    }


    protected Note(Parcel in) {
        noteId = in.readInt();
        title = in.readString();
        content = in.readString();
        creationDate = in.readLong();
        modificationDate = in.readLong();
        deleted = in.readInt();
        uploaded = in.readInt();
        modified = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public static int getImageId() {
        return imageId;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getModificationDate() {
        return modificationDate;
    }

    public int getDeleted() {
        return deleted;
    }

    public int getUploaded() {
        return uploaded;
    }

    public int getModified() {
        return modified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(noteId);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeLong(creationDate);
        parcel.writeLong(modificationDate);
        parcel.writeInt(deleted);
        parcel.writeInt(uploaded);
        parcel.writeInt(modified);
    }
}