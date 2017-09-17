package com.example.miguel.misnotas.Clases_Lista;

/**
 * Created by Miguel on 13/06/2016.
 */
public class Elemento_Lista {
    private String nombre;
    private int drawableImageID, id, value, idaux;
    public boolean sel=false;
    //idaux siempre va a tener el valor de la imgen origianl
    //private String descripcion;
    public Elemento_Lista(String nombre,int value, int drawableImageID, int id) {
        this.nombre = nombre;
        this.drawableImageID = drawableImageID;
        this.value=value;
        this.id=id;
    }

    public String getNombre() {
        return nombre;
    }
    public int getvalor() {
        return value;
    }
    public int getidbase(){
        return id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setValor(int Valor) {
        this.value = Valor;
    }
    public boolean isticked(){
        return sel;
    }
    public int getDrawableImageID() {
        return drawableImageID;
    }

    public void setDrawableImageID(int drawableImageID) {
        this.drawableImageID = drawableImageID;
    }
    public void setTicked(int id_tick){
        sel=true;
        //recibe parametro para no perder las referencias de las imagenes
        idaux=this.drawableImageID;
        this.drawableImageID=id_tick;
    }
    public void untick(){
        sel=false;
        this.drawableImageID=idaux;
    }

}