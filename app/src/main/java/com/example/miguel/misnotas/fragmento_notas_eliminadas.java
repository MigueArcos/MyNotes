package com.example.miguel.misnotas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.miguel.misnotas.Clases_Lista.Adaptador_Notas_Eliminadas;
import com.example.miguel.misnotas.Clases_Lista.Elemento_Nota;
import java.util.ArrayList;


/**
 * Created by Miguel on 19/07/2017.
 */
public class fragmento_notas_eliminadas extends Fragment{
    private RecyclerView lista;
    private Adaptador_Notas_Eliminadas adaptador;
    private ArrayList<Elemento_Nota> items;
    private Base_Datos ob;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se accede al layout (interfaz) de este fragmento
        View rootView = inflater.inflate(R.layout.fragmento_notas_eliminadas, container, false);
        //Se crea un objeto de la base de datos
        ob = new Base_Datos(this.getActivity());
        //Se accede a la lista
        lista =(RecyclerView)rootView.findViewById(R.id.lista);
        //Se crea el adaptador de la lista que contendra todos los datos
        adaptador = new Adaptador_Notas_Eliminadas(items, this.getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        lista.setLayoutManager(llm);
        //Se vincula dicho adaptador a la lista
        lista.setAdapter(adaptador);
        //Esta linea es para mejorar el desempeño de esta recyclerview (lista)
        //lista.setHasFixedSize(true);
        //ItemAnimator del recyclerView
        return rootView;
    }


    /*Para hacer esto, es indispensable saber que el ciclo de vida de la actividad fragmento va asi:
    En la segunda actividad (Editor) cuando se presiona la tecla atras, se ejecutan asi los metodos:
    1.- onBackPressed (De actividad Editor)
    2.- onResume (Este fragmento de la primera actividad)
    3.- onStop (De actividad Editor)
    4.- onDestroy (De acividad Editor)
     */
    @Override
    public void onResume() {
        items=ob.leer_notas("SELECT * FROM notas WHERE eliminado='S' ORDER BY fecha_modificacion_orden DESC");
        //Método personalizado para volver a cargar los datos :D
        adaptador.setData(items);
        adaptador.notifyDataSetChanged();
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
        super.onResume();
    }
    @Override
    public void onStop(){
        super.onStop();
        adaptador.QuitarAlertDialog();
    }


}


