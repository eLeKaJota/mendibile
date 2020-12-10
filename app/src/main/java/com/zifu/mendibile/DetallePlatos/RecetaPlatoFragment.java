package com.zifu.mendibile.DetallePlatos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaPlato;

/**
 * A simple {@link Fragment} subclass.

 */
public class RecetaPlatoFragment extends Fragment {

    TextInputLayout tlEditarReceta;
    TextInputEditText txtEditarReceta;
    Button btnEditarReceta;
    TextView receta;
    DetallePlatos detalle;
    int estadoReceta = 0;


    public RecetaPlatoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receta_plato, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        detalle = (DetallePlatos) getActivity();


        //---------BINDEO
        tlEditarReceta = (TextInputLayout) view.findViewById(R.id.tlEditarReceta);
        txtEditarReceta = (TextInputEditText) view.findViewById(R.id.txtEditarReceta);
        btnEditarReceta = (Button) view.findViewById(R.id.btnEditarReceta);
        receta = (TextView) view.findViewById(R.id.tvDetallePltElaboracion);

        //-----------MOSTRAR/GUARDAR EDITAR RECETA
        btnEditarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estadoReceta ==0) {
                    receta.setVisibility(View.GONE);
                    tlEditarReceta.setVisibility(View.VISIBLE);
                    txtEditarReceta.setText(receta.getText());
                    btnEditarReceta.setText("Guardar receta");
                    estadoReceta = 1;
                }else{
                    receta.setVisibility(View.VISIBLE);
                    tlEditarReceta.setVisibility(View.GONE);
                    receta.setText(txtEditarReceta.getText());
                    editaReceta(txtEditarReceta.getText().toString());
                    btnEditarReceta.setText("Editar receta");
                    estadoReceta = 0;
                }
            }
        });

        Bundle args = getArguments();
        receta.setText("" + detalle.plato.getElaboracion());
    }

    public void editaReceta(String receta){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TablaPlato.NOMBRE_COLUMNA_6,receta);

        String selection = TablaPlato.NOMBRE_COLUMNA_1 + " LIKE ?";
        String[] selectionArgs = { String.valueOf(detalle.plato.getId()) };

        db.update(TablaPlato.NOMBRE_TABLA,values,selection,selectionArgs);
    }
}