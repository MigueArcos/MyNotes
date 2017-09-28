package com.example.miguel.misnotas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.miguel.misnotas.Clases_Lista.Elemento_Nota;
import com.example.miguel.misnotas.Clases_Lista.NotesAdapter;

import java.util.ArrayList;


/**
 * Created by Miguel on 20/06/2016.
 */
public class fragmento_notas extends Fragment implements View.OnClickListener, NotesAdapter.MyRecyclerViewActions{
    private RecyclerView list;
    private NotesAdapter adapter;
    private ArrayList<Elemento_Nota> data;
    private FloatingActionButton create;
    //private GestureDetectorCompat mDetector;
    private Snackbar snackbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se accede al layout (interfaz) de este fragmento
        View rootView = inflater.inflate(R.layout.fragmento_notas, container, false);
        //Se accede a la lista
        list =(RecyclerView)rootView.findViewById(R.id.lista);
        //Se crea el adaptador de la lista que contendra todos los datos
        adapter = new NotesAdapter(data, this);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        //Se vincula dicho adaptador a la lista
        list.setAdapter(adapter);
        //Esta linea es para mejorar el desempeño de esta recyclerview (lista)
        list.setHasFixedSize(true);
        //ItemAnimator del recyclerView
        create=(FloatingActionButton)rootView.findViewById(R.id.crear);
        //Se añade el evento para cuando se presiona el boton de crear
        create.setOnClickListener(this);
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



    public void dismissSnackbar(){
        if (snackbar!=null){
            snackbar.dismiss();
        }
    }


    private void CreateNewNote() {
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
        //Remove snackbar if exists
        CreateNewNote();
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
        data=Database.getInstance(getActivity()).leer_notas("SELECT * FROM notas WHERE eliminado='N' ORDER BY fecha_modificacion_orden DESC");
        //Método personalizado para volver a cargar los datos :D
        adapter.setData(data);
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    public void onStop(){
        dismissSnackbar();
        super.onStop();
    }

    @Override
    public void onSwipe(final int position) {
        final Elemento_Nota selectedNote=data.get(position);
        final int selectedNoteID=data.get(position).getID_Nota();

        snackbar = Snackbar.make(list, "Nota eliminada", 10000).
                setAction("Deshacer", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(recyclerView.getContext(),""+adapterPosition, Toast.LENGTH_SHORT).show();
                        data.add(position, selectedNote);
                        adapter.notifyItemInserted(position);
                        list.scrollToPosition(position);

                    }
                }).setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                /*if (event!=DISMISS_EVENT_ACTION) {
                    Database.getInstance(getActivity()).eliminar_nota(selectedNoteID);
                }*/
                if (event == DISMISS_EVENT_ACTION || event == DISMISS_EVENT_MANUAL){

                }
                else{
                    Database.getInstance(getActivity()).eliminar_nota(selectedNoteID);
                }
                //Si hubiera sido por DISMISS_EVENT_ACTION significa que el usuario presiono deshacer y por lo tanto no quiere que se
                //elimine de la base de datos
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }

        });

        data.remove(position);
        adapter.notifyItemRemoved(position);
        snackbar.show();
    }

    @Override
    public void onTouch(int position) {
        dismissSnackbar();
        Elemento_Nota note=data.get(position);
        Intent i = new Intent(getActivity(), Editor_Notas.class);
        Bundle pack = new Bundle();
        //Add your data from getFactualResults method to bundle
        pack.putBoolean("NuevaNota",false);
        pack.putString("contenido",note.getContenido());
        pack.putString("titulo",note.getTitulo());
        pack.putInt("id_nota_mod",note.getID_Nota());
        //Add the bundle to the intent
        i.putExtras(pack);
        getActivity().startActivity(i);
    }
}


