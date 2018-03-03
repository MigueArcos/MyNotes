package com.example.miguel.misnotas.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.activities.NotesEditorActivity;
import com.example.miguel.misnotas.activities.SearchNotesActivity;
import com.example.miguel.misnotas.adapters.FilterableRecyclerViewAdapter;
import com.example.miguel.misnotas.adapters.NotesAdapter;
import com.example.miguel.misnotas.models.Note;

import java.util.List;

import static com.example.miguel.misnotas.activities.SearchNotesActivity.NOTES;


/**
 * Created by Miguel on 20/06/2016.
 */
public class NotesFragment extends Fragment implements View.OnClickListener, FilterableRecyclerViewAdapter.NotesAdapterActions {
    private RecyclerView list;
    private NotesAdapter adapter;
    private List<Note> data;
    private FloatingActionButton create;
    //private GestureDetectorCompat mDetector;
    private Snackbar snackbar;
    private TextView emptyList;
    private boolean calledFromSearch;
    private String text = "";
    private AlertDialog.Builder dialogDeleteNoteCompletely;
    public static final int CALL_EDITOR_ACTIVITY = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se accede al layout (interfaz) de este fragmento
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        if (getArguments() != null) {
            calledFromSearch = getArguments().getBoolean("calledFromSearch", false);
        }
        emptyList = (TextView) rootView.findViewById(R.id.emptyList);
        //Se accede a la lista
        list = (RecyclerView) rootView.findViewById(R.id.lista);
        //Se crea el adaptador de la lista que contendra todos los datos
        data = Database.getInstance(getActivity()).getNotes(false);
        adapter = new NotesAdapter(data, this);
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
        list.setHasFixedSize(true);
        create = (FloatingActionButton) rootView.findViewById(R.id.crear);

        if (!calledFromSearch) {
            //Se añade el evento para cuando se presiona el boton de crear
            create.setOnClickListener(this);
            this.setHasOptionsMenu(true);
            emptyList.setText(R.string.empty_list_default_text);
        } else {
            create.setVisibility(View.GONE);
            emptyList.setText(R.string.empty_list_search_text);
        }
        dialogDeleteNoteCompletely = new AlertDialog.Builder(getActivity());
        dialogDeleteNoteCompletely.setTitle(R.string.dialog_default_title).setMessage(getString(R.string.delete_note_completely));
        dialogDeleteNoteCompletely.setNegativeButton(R.string.negative_button_label, (dialog, which) -> {
        });
        return rootView;
    }


    public void dismissSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(getActivity(), SearchNotesActivity.class);
                intent.putExtra("calledFromSearch", true);
                intent.putExtra("type", NOTES);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivityForResult(intent, -1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void CreateNewNote() {
        Intent i = new Intent(getActivity(), NotesEditorActivity.class);
        Bundle packageData = new Bundle();
        //Add your data from getFactualResults method to bundle
        packageData.putBoolean("isNewNote", true);
        //Add the bundle to the intent
        i.putExtras(packageData);
        startActivityForResult(i, CALL_EDITOR_ACTIVITY);
    }

    @Override
    public void onClick(View v) {
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

        //adapter.filterResults(text);
        //Toast.makeText(this.getActivity(), "Se ejecuto onResume de fragmento", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    public void onStop() {
        dismissSnackBar();
        super.onStop();
    }

    @Override
    public void onSwipe(final int position) {
        final Note selectedNote = adapter.getCurrentData().get(position);
        final int selectedNoteID = adapter.getCurrentData().get(position).getNoteId();

        snackbar = Snackbar.make(list, R.string.fragment_notes_snackbar_label, 10000).
                setAction(R.string.fragment_notes_snackbar_action_label, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(recyclerView.getContext(),""+adapterPosition, Toast.LENGTH_SHORT).show();
                        adapter.insertItem(position, selectedNote);
                        list.scrollToPosition(position);

                    }
                }).
                setCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != DISMISS_EVENT_ACTION) {
                            Database.getInstance(getActivity()).deleteNote(selectedNoteID);
                        }
                        //Si hubiera sido por DISMISS_EVENT_ACTION significa que el usuario presiono deshacer y por lo tanto no quiere que se
                        //elimine de la base de datos
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                    }

                });

        adapter.deleteItem(position);
        snackbar.show();
    }

    @Override
    public void onClick(int position) {
        dismissSnackBar();
        Note note = adapter.getCurrentData().get(position);
        Intent i = new Intent(getActivity(), NotesEditorActivity.class);
        Bundle packageData = new Bundle();
        //Add your data from getFactualResults method to bundle
        packageData.putBoolean("isNewNote", false);
        packageData.putString("content", note.getContent());
        packageData.putString("title", note.getTitle());
        packageData.putInt("noteToModifyId", note.getNoteId());
        packageData.putInt("position", position);
        //Add the bundle to the intent
        i.putExtras(packageData);

        startActivityForResult(i, CALL_EDITOR_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(MyUtils.GLOBAL_LOG_TAG, "Returnig to this framet");
        if (requestCode == CALL_EDITOR_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Note resultNote = data.getParcelableExtra("resultNote");
                if (data.getBooleanExtra("isNewNote", true)) {
                    adapter.insertItem(resultNote);
                    list.scrollToPosition(0);
                } else {
                    adapter.modifyItem(data.getIntExtra("position", -1), resultNote);
                    list.scrollToPosition(0);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onLongClick(int position) {
        dismissSnackBar();
        dialogDeleteNoteCompletely.setPositiveButton(R.string.positive_button_label, (dialog, which) -> DeleteNoteCompletely(position)).show();
    }

    private void DeleteNoteCompletely(int position) {
        Database.getInstance(getActivity()).deleteNoteCompletely(adapter.getCurrentData().get(position).getNoteId());
        adapter.deleteItem(position);
    }

    public void filterNotes(String text) {
        this.text = text;
        adapter.filterResults(text);
    }

}


