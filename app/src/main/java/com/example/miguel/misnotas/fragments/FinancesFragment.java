package com.example.miguel.misnotas.fragments;

/**
 * Created by Miguel on 13/06/2016.
 */
import android.app.AlertDialog;

import android.content.DialogInterface;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.miguel.misnotas.Database;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.adapters.FinancesAdapter;
import com.example.miguel.misnotas.models.Finance;

import java.util.ArrayList;
public class FinancesFragment extends Fragment implements MenuItem.OnMenuItemClickListener,AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener {
    private ListView lista;
    private FinancesAdapter adaptador;
    private ArrayList<Finance> items;
    private TextView total, last;
    private String tot;
    private int contar_cambios = 0;
    private AlertDialog.Builder builder;
    private AlertDialog mensaje;
    public FinancesFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finances, container, false);
        total=(TextView)rootView.findViewById(R.id.total);
        items=new ArrayList<>();
        lista =(ListView)rootView.findViewById(R.id.list);
        builder=new AlertDialog.Builder(this.getActivity());
        adaptador = new FinancesAdapter(this.getActivity(),items);
        Database.getInstance(getActivity()).leer(adaptador);
        update_total();
        last=(TextView)rootView.findViewById(R.id.last);
        last.setText(Database.getInstance(getActivity()).LastUpdate());
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(this);
        lista.setOnItemLongClickListener(this);
        setHasOptionsMenu(true);
        return rootView;
    }
    void update_total() {
        tot = "$ " + Database.getInstance(getActivity()).sumatoria();
        total.setText(tot);
        contar_cambios++;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_finances, menu);
        MenuItem boton_barra=menu.findItem(R.id.add);
        boton_barra.setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.fragment_my_money_dialog_title);
        //Contenedor
        LinearLayout layout = new LinearLayout(this.getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input1 = new EditText(this.getActivity());
        input1.setHint(R.string.fragment_my_money_dialog_placeholder_resource_name);
        input1.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        final EditText input2 = new EditText(this.getActivity());
        input2.setHint(R.string.fragment_my_money_dialog_placeholder_resource_value);
        input2.setInputType(InputType.TYPE_CLASS_NUMBER);
        input2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});
        layout.addView(input1);
        layout.addView(input2);
        // fin contenedor
        // añadir contenedor al alertdialog
        builder.setView(layout);
        /*
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);
*/
// Set up the buttons
        builder.setPositiveButton(getString(R.string.positive_button_finish_action), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //adaptador.add(input1.getText().toString() + "\n" + texto);
                int id=Database.getInstance(getActivity()).guardar_y_mandar_id(input1.getText().toString(), Integer.parseInt(input2.getText().toString()));
                adaptador.add(new Finance(input1.getText().toString(),Integer.parseInt(input2.getText().toString()),
                        R.drawable.mercedes_benz_logo, id));
                update_total();
                last.setText(Database.getInstance(getActivity()).LastUpdate());

            }
        });
        builder.setNegativeButton(R.string.negative_button_cancel_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //dialog es una variable auxiliar que contiene el builder del mensaje
        //textwatcher es para que el input(1,2) active/desactive el boton de listo
        final AlertDialog dialog = builder.create();
        TextWatcher escucha=new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input1.getText().toString().length()==0 || input2.getText().toString().length() ==0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        };
        //añadiendo los escuchadores de texto cambiado a los inputs que estan en el layout del mensaje
        input1.addTextChangedListener(escucha);
        input2.addTextChangedListener(escucha);
        dialog.show();
        //añadir esta linea para desactivar el boton listo (siempre se debe añadir despues de que el alertdialog este visible,
        //si se hace cuando el alertdialog aun no esta visible explota todo porque no hay nada que desactivar)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        return false;
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        builder.setMessage(R.string.delete_item_confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.positive_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Database.getInstance(getActivity()).eliminarrecurso(adaptador.getItem(position).getidbase());

                        adaptador.remove(adaptador.getItem(position));
                        update_total();
                        last.setText(Database.getInstance(getActivity()).LastUpdate());
                    }
                })
                .setNegativeButton(R.string.negative_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        mensaje=builder.create();
        mensaje.show();
        builder=new AlertDialog.Builder(this.getActivity());
        //se añade true en lugar de false para que no se ejecuten a la vez el OnItemClick y el OnItemLongClick listeners
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        builder.setTitle(R.string.fragment_my_money_modify_label);
        //Contenedor
        final int i = adaptador.getItem(position).getidbase();
        String array[] = Database.getInstance(getActivity()).mod(i);
        LinearLayout layout = new LinearLayout(this.getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input1 = new EditText(this.getActivity());
        input1.setText(array[0]);
        input1.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        final EditText input2 = new EditText(this.getActivity());
        input2.setText(array[1]);
        input2.setInputType(InputType.TYPE_CLASS_NUMBER);
        input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        layout.addView(input1);
        layout.addView(input2);
        // fin contenedor
        // añadir contenedor al alertdialog
        builder.setView(layout);
        /*
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);
*/
// Set up the buttons
        builder.setPositiveButton(R.string.positive_button_finish_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String texto = input1.getText().toString() + "\n$ " + input2.getText().toString();
                /*adaptador.remove(adaptador.getItem(position));
                adaptador.insert(new Finance(input1.getText().toString(), Integer.parseInt(input2.getText().toString()),
                        R.drawable.mercedes_benz_logo, i), position);*/
                items.get(position).setNombre(input1.getText().toString());
                items.get(position).setValor(Integer.parseInt(input2.getText().toString()));
                adaptador.notifyDataSetChanged();
                Database.getInstance(getActivity()).modificar(input1.getText().toString(), Integer.parseInt(input2.getText().toString()), i);
                update_total();
                last.setText(Database.getInstance(getActivity()).LastUpdate());
                //Toast.makeText(FinancesFragment.this.getActivity(),input2.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.negative_button_cancel_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //dialog es una variable auxiliar que contiene el builder del mensaje
        //textwatcher es para que el input(1,2) active/desactive el boton de listo
        mensaje= builder.create();
        TextWatcher escucha = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input1.getText().toString().length() == 0 || input2.getText().toString().length() == 0) {
                    mensaje.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    mensaje.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
        //añadiendo los escuchadores de texto cambiado a los inputs que estan en el layout del mensaje
        input1.addTextChangedListener(escucha);
        input2.addTextChangedListener(escucha);
        mensaje.show();
        //añadir esta linea para desactivar el boton listo (siempre se debe añadir despues de que el alertdialog este visible,
        //si se hace cuando el alertdialog aun no esta visible explota todo porque no hay nada que desactivar)
        mensaje.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        builder=new AlertDialog.Builder(this.getActivity());
    }

}

