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
    private String creationDate;
    @SerializedName("fecha_modificacion")
    private String modificationDate;
    @SerializedName("fecha_modificacion_orden")
    private String orderModificationDate;
    @SerializedName("eliminado")
    private char deleted;
    @SerializedName("subida")
    private char uploaded;

    public Note(int noteId, String title, String content, String creationDate, String modificationDate) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public Note(int noteId, String title, String content, String creationDate, String modificationDate, String orderModificationDate, char deleted, char uploaded) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.orderModificationDate = orderModificationDate;
        this.deleted = deleted;
        this.uploaded = uploaded;
    }

    public Note(int noteId, String title, String content, String modificationDate) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.modificationDate = modificationDate;
    }

    protected Note(Parcel in) {
        noteId = in.readInt();
        title = in.readString();
        content = in.readString();
        creationDate = in.readString();
        modificationDate = in.readString();
        orderModificationDate = in.readString();
        deleted = (char) in.readInt();
        uploaded = (char) in.readInt();
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

    public String getCreationDate() {
        return creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public String getOrderModificationDate() {
        return orderModificationDate;
    }

    public char getDeleted() {
        return deleted;
    }

    public char getUploaded() {
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
        dest.writeString(creationDate);
        dest.writeString(modificationDate);
        dest.writeString(orderModificationDate);
        dest.writeInt((int) deleted);
        dest.writeInt((int) uploaded);
    }
}