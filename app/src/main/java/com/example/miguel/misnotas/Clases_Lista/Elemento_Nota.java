package com.example.miguel.misnotas.Clases_Lista;

import com.example.miguel.misnotas.R;

/**
 * Created by Miguel on 20/06/2016.
 */
public class Elemento_Nota {
    //Por el momento id_nota es estático y por lo tanto no tiene sentido serializarlo para crear el objeto JSON
    /*Check this link to see properties of serialization wit Gson (First answer)
    https://stackoverflow.com/questions/14644860/why-static-fields-not-serialized-using-google-gson-gsonbuilder-json-parser*/
    private static int id_imagen= R.drawable.nota;
    private String  titulo;
    private String contenido;
    private String fecha_creacion;
    private String fecha_modificacion;
    private int id_nota;
    private String fecha_modificacion_orden;
    private char eliminado;
    private char subida;
    public Elemento_Nota(int id_nota,String titulo,String contenido, String fecha_creacion, String fecha_modificacion) {
        this.contenido=contenido;
        this.titulo=titulo;
        this.fecha_creacion=fecha_creacion;
        this.fecha_modificacion=fecha_modificacion;
        this.id_nota=id_nota;
    }
    public Elemento_Nota(int id_nota,String titulo,String contenido, String fecha_creacion, String fecha_modificacion, String fecha_modificacion_orden, char eliminado, char subida) {
        this.contenido=contenido;
        this.titulo=titulo;
        this.fecha_creacion=fecha_creacion;
        this.fecha_modificacion=fecha_modificacion;
        this.id_nota=id_nota;
        this.fecha_modificacion_orden=fecha_modificacion_orden;
        this.eliminado=eliminado;
        this.subida=subida;
    }
    public int getID_Imagen(){
        return id_imagen;
    }
    public String getContenido(){return contenido;}
    public int getID_Nota(){
        return id_nota;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getFecha_modificacion() {
        return fecha_modificacion;
    }
    public String getFecha_modificacion_orden(){return fecha_modificacion_orden;}
    public String getFecha_creacion(){return fecha_creacion;}
    public char getEliminado(){return eliminado;}
    public char getSubida(){return subida;}

}