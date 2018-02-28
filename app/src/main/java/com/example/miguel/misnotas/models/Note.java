package com.example.miguel.misnotas.models;

import com.example.miguel.misnotas.R;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Miguel on 20/06/2016.
 */
public class Note {
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

}