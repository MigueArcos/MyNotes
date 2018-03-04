package com.example.miguel.misnotas.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.miguel.misnotas.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Miguel on 20/06/2016.
 */
public class Note implements Parcelable{
    //Por el momento noteId es estático y por lo tanto no tiene sentido serializarlo para crear el objeto JSON
    /*Check this link to see properties of serialization wit Gson (First answer)
    https://stackoverflow.com/questions/14644860/why-static-fields-not-serialized-using-google-gson-gsonbuilder-json-parser*/
    public static final int imageId = R.drawable.note;
    @SerializedName("id_nota")
    private int noteId;
    @SerializedName("titulo")
    private String title;
    @SerializedName("contenido")
    private String content;
    @SerializedName("fecha_creacion")
    private long creationDate;
    @SerializedName("fecha_modificacion")
    private long modificationDate;
    @SerializedName("eliminado")
    private int deleted;
    @SerializedName("subida")
    private int uploaded;

    public Note(int noteId, String title, String content, long creationDate, long modificationDate) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public Note(int noteId, String title, String content, long creationDate, long modificationDate, int deleted, int uploaded) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.deleted = deleted;
        this.uploaded = uploaded;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(noteId);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(creationDate);
        dest.writeLong(modificationDate);
        dest.writeInt(deleted);
        dest.writeInt(uploaded);
    }
}