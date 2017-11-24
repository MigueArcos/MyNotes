package com.example.miguel.misnotas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miguel.misnotas.Clases_Lista.DeletedNotesAdapter;
import com.example.miguel.misnotas.Clases_Lista.Elemento_Nota;

import java.util.ArrayList;

import static com.example.miguel.misnotas.SearchActivity.DELETED_NOTES;


/**
 * Created by Miguel on 19/07/2017.
 */
public class fragmento_notas_eliminadas extends Fragment implements DeletedNotesAdapter.AdapterActions {
    private RecyclerView list;
    private DeletedNotesAdapter adapter;
    private ArrayList<Elemento_Nota> data;
    private AlertDialog.Builder dialogDeleteNote;
    private AlertDialog.Builder dialogRecoverNote;
    private TextView emptyList;
    private boolean calledFromSearch;
    private String text = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se accede al layout (interfaz) de este fragmento
        View rootView = inflater.inflate(R.layout.fragmento_notas_eliminadas, container, false);
        if (this.getArguments() != null) {
            calledFromSearch = getArguments().getBoolean("calledFromSearch", false);
        }
        //Se accede a la lista
        list = (RecyclerView) rootView.findViewById(R.id.lista);
        //Se crea el adaptador de la lista que contendra todos los datos
        adapter = new DeletedNotesAdapter(data, this);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        list.setLayoutManager(llm);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                emptyList.setVisibility(data.size() > 0 ? View.GONE : View.VISIBLE);
                super.onChanged();
            }
        });
        //Se vincula dicho adaptador a la lista
        list.setAdapter(adapter);
        //Esta linea es para mejorar el desempeño de esta recyclerview (lista)
        //lista.setHasFixedSize(true);
        //ItemAnimator del recyclerView

        dialogDeleteNote = new AlertDialog.Builder(getActivity());
        dialogDeleteNote.setTitle(R.string.dialog_default_title).setMessage(R.string.delete_note_completely);

        dialogRecoverNote = new AlertDialog.Builder(getActivity());
        dialogRecoverNote.setTitle(R.string.dialog_default_title).setMessage(getString(R.string.fragment_deleted_notes_recover_note));
        dialogRecoverNote.setNegativeButton(R.string.negative_button_label, (dialog, which) -> {/*Empty lambda body*/});

        emptyList = (TextView) rootView.findViewById(R.id.empty_list_text);
        if (!calledFromSearch) {
            this.setHasOptionsMenu(true);
            emptyList.setText(R.string.empty_list_default_text);
        } else {
            emptyList.setText(R.string.empty_list_search_text);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("calledFromSearch", true);
        intent.putExtra("type", DELETED_NOTES);
        getActivity().startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        if (!calledFromSearch) {
            data = Database.getInstance(getActivity()).leer_notas("SELECT * FROM notas WHERE eliminado='S' ORDER BY fecha_modificacion_orden DESC");
            //Método personalizado para volver a cargar los datos :D
        } else {
            filterNotes(text);
        }
        adapter.setData(data);
        super.onResume();
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
    }


    private void DeleteNoteCompletely(int noteID) {
        Database.getInstance(getActivity()).eliminar_nota_completamente(noteID);
    }

    private void CancelDeleteNote(int position, final Elemento_Nota selectedNote) {
        data.add(position, selectedNote);
        adapter.notifyItemInserted(position);
        list.scrollToPosition(position);
    }

    private void RecoverNote(int position, Elemento_Nota selectedNote) {
        Database.getInstance(getActivity()).recuperar_nota(selectedNote.getID_Nota());
        data.remove(position);
        adapter.notifyItemRemoved(position);
        //It doesn't matter if the item was or not expanded, it will remove it from expandedItems (this will avoid further problems with positions)
        adapter.notifyExpandedItemDeleted(position);
    }

    @Override
    public void onClick(int position) {
        dialogRecoverNote.setPositiveButton(R.string.positive_button_label, (dialog, which) -> RecoverNote(position, data.get(position))).show();
    }

    @Override
    public void onSwipe(int position) {
        final Elemento_Nota selectedNote = data.get(position);
        dialogDeleteNote
                .setPositiveButton(R.string.positive_button_label, (dialog, which) -> DeleteNoteCompletely(selectedNote.getID_Nota()))
                .setNegativeButton(R.string.negative_button_label, (dialog, which) -> CancelDeleteNote(position, selectedNote))
                .setOnCancelListener(dialog -> CancelDeleteNote(position, selectedNote))
                .show();
        //Note: If you pass directly data.get(position) to methods like CancelDeleteNote(...) or DeleteNoteCompletely(...), it doesn't work properly, maybe because the callback pulls parameters until you are gonna call it (when the item has been already deleted)
        data.remove(position);
        adapter.notifyItemRemoved(position);
        //It doesn't matter if the item was or not expanded, it will remove it from expandedItems (this will avoid further problems with positions)
        adapter.notifyExpandedItemDeleted(position);
    }

    public void filterNotes(String text) {
        this.text = text;
        if (text.isEmpty()) {
            data = new ArrayList<>();
        } else {
            data = Database.getInstance(getActivity()).leer_notas("SELECT * FROM notas WHERE eliminado='S' AND (titulo || contenido) LIKE '%" + text + "%' ORDER BY fecha_modificacion_orden DESC");
        }
        adapter.setData(data);
    }


}


