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
import com.example.miguel.misnotas.Clases_Lista.Adaptador_Notas;
import com.example.miguel.misnotas.Clases_Lista.Elemento_Nota;
import java.util.ArrayList;


/**
 * Created by Miguel on 20/06/2016.
 */
public class fragmento_notas extends Fragment implements View.OnClickListener{
    private RecyclerView lista;
    private Adaptador_Notas adaptador;
    private ArrayList<Elemento_Nota> items;
    private FloatingActionButton crear;
    private GestureDetectorCompat mDetector;
    public fragmento_notas(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se accede al layout (interfaz) de este fragmento
        View rootView = inflater.inflate(R.layout.fragmento_notas, container, false);
        //Se accede a la lista
        lista =(RecyclerView)rootView.findViewById(R.id.lista);
        //Se crea el adaptador de la lista que contendra todos los datos
        adaptador = new Adaptador_Notas(items, this.getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        lista.setLayoutManager(llm);
        //Se vincula dicho adaptador a la lista
        lista.setAdapter(adaptador);
        //Esta linea es para mejorar el desempeño de esta recyclerview (lista)
        lista.setHasFixedSize(true);
        //ItemAnimator del recyclerView
        crear=(FloatingActionButton)rootView.findViewById(R.id.crear);
        //Se añade el evento para cuando se presiona el boton de crear
        crear.setOnClickListener(this);
        //Toast.makeText(this.getActivity().getBaseContext(), fec, Toast.LENGTH_SHORT).show();
        /*lista.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                //Toast.makeText(fragmento_notas.this.getActivity(),"holis tronco", Toast.LENGTH_SHORT).show();
                adaptador.quitar_snack();
            }
        });
        */
        return rootView;
    }
    public void Ir_Editor() {
        Intent i = new Intent(getActivity(), Editor_Notas.class);
        Bundle paquete = new Bundle();
        //Add your data from getFactualResults method to bundle
        paquete.putBoolean("NuevaNota",true);
        //Add the bundle to the intent
        i.putExtras(paquete);
        startActivity(i);
    }
    @Override
    public void onClick(View v) {
        Log.d("Holis","holas");
        adaptador.quitar_snack();
        Ir_Editor();
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
        items=Database.getInstance(getActivity()).leer_notas("SELECT * FROM notas WHERE eliminado='N' ORDER BY fecha_modificacion_orden DESC");
        //Método personalizado para volver a cargar los datos :D
        adaptador.setData(items);
        adaptador.notifyDataSetChanged();
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
        super.onResume();
    }
   /* @Override
    public void onDestroyView(){
        super.onDestroyView();
        adaptador.quitar_snack();
    }*/


}


