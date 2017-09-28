package com.example.miguel.misnotas;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.miguel.misnotas.Clases_Lista.DeletedNotesAdapter;
import com.example.miguel.misnotas.Clases_Lista.Elemento_Nota;
import java.util.ArrayList;


/**
 * Created by Miguel on 19/07/2017.
 */
public class fragmento_notas_eliminadas extends Fragment implements DeletedNotesAdapter.AdapterActions{
    private RecyclerView list;
    private DeletedNotesAdapter adapter;
    private ArrayList<Elemento_Nota> data;
    private AlertDialog.Builder dialogDeleteNote;
    private AlertDialog.Builder dialogRecoverNote;
    private int position;
    private Elemento_Nota selectedNote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se accede al layout (interfaz) de este fragmento
        View rootView = inflater.inflate(R.layout.fragmento_notas_eliminadas, container, false);
        //Se accede a la lista
        list =(RecyclerView)rootView.findViewById(R.id.lista);
        //Se crea el adaptador de la lista que contendra todos los datos
        adapter = new DeletedNotesAdapter(data,this);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        //Se vincula dicho adaptador a la lista
        list.setAdapter(adapter);
        //Esta linea es para mejorar el desempeño de esta recyclerview (lista)
        //lista.setHasFixedSize(true);
        //ItemAnimator del recyclerView

        dialogDeleteNote = new AlertDialog.Builder(getActivity());
        dialogDeleteNote.setTitle("Notas de MigueLópez :D").setMessage("¿Te gustaría eliminar esta nota completamente?").setCancelable(false);
        dialogDeleteNote
                .setPositiveButton("Si", (dialog, which) -> DeleteNoteCompletely())
                .setNegativeButton("No", (dialog, which) -> CancelDeleteNote());

        dialogRecoverNote = new AlertDialog.Builder(getActivity());
        dialogRecoverNote.setTitle("Notas de MigueLópez :D").setMessage("¿Te gustaría recuperar esta nota?");
        dialogRecoverNote
                .setPositiveButton("Si", (dialog, which) -> RecoverNote())
                .setNegativeButton("No", (dialog, which) -> {/*Empty lambda body*/});
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
        data=Database.getInstance(getActivity()).leer_notas("SELECT * FROM notas WHERE eliminado='S' ORDER BY fecha_modificacion_orden DESC");
        //Método personalizado para volver a cargar los datos :D
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    private void DeleteNoteCompletely(){
        Database.getInstance(getActivity()).eliminar_nota_completamente(selectedNote.getID_Nota());
    }
    private void CancelDeleteNote(){
        data.add(position, selectedNote);
        adapter.notifyItemInserted(position);
        list.scrollToPosition(position);
    }
    private void RecoverNote(){
        Database.getInstance(getActivity()).recuperar_nota(selectedNote.getID_Nota());
        data.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyExpandedItemDeleted(position);
    }

    @Override
    public void onClick(int position) {
        this.position = position;
        selectedNote = data.get(position);
        dialogRecoverNote.show();
    }

    @Override
    public void onSwipe(int position) {
        selectedNote = data.get(position);
        this.position = position;
        dialogDeleteNote.show();
        data.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyExpandedItemDeleted(position);
    }
}


