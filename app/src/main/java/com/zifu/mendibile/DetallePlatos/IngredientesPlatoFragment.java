package com.zifu.mendibile.DetallePlatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.

 */
public class IngredientesPlatoFragment extends Fragment implements AdaptadorListaDetalleIng.ListItemClick {

    private RecyclerView listaIng;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Ingrediente> ings;
    DetallePlatos detalle;
    private Toast mToast;
    Context vista;
    BBDDHelper helper;


    public IngredientesPlatoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //actualizaLista();
        return inflater.inflate(R.layout.fragment_ingredientes_plato, container, false);


    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        detalle = (DetallePlatos) getActivity();
        listaIng = view.findViewById(R.id.listaDetalleIng);
        listaIng.setHasFixedSize(true);
        listaIng.addItemDecoration(new DividerItemDecoration(detalle,DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(detalle);
        listaIng.setLayoutManager(layoutManager);
        vista = view.getContext();
        helper  = new BBDDHelper(vista);

        actualizaLista();
    }

    //----------RECUPERA ARRAY DE INGREDIENTES
    public String[] recuperaIng(){
        ArrayList<String> in = new ArrayList<String>();
        SQLiteDatabase db = this.helper.getReadableDatabase();
        String selection = TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " = ?";
        String[] selectionArgs = { String.valueOf(detalle.plato.getId()) };

        Cursor cursor = db.query(TablaPlatoIngredientePeso.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);

        while(cursor.moveToNext()){
            String ingId = cursor.getString(2);
            in.add(String.valueOf(ingId));
        }
        cursor.close();
        String[] i = in.toArray(new String[0]);
        return i;
    }

    //----------------------------------------

    public void actualizaLista(){
        SQLiteDatabase db = this.helper.getReadableDatabase();
        ings = new ArrayList<Ingrediente>();
        for(String i: recuperaIng()){

            String selection = TablaIngrediente.NOMBRE_COLUMNA_1 + " = ?";
            String[] selectionArgs = { i };

            Cursor cursor = db.query(TablaIngrediente.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);

            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            String formato = cursor.getString(3);
            String proveedor = cursor.getString(4);

            Ingrediente ing = new Ingrediente(id,nombre,formato,proveedor,precio);
            ings.add(ing);

            cursor.close();



        }
        adaptador = new AdaptadorListaDetalleIng(ings, this,helper,detalle);
        listaIng.setAdapter(adaptador);


    }

    @Override
    public void onListItemClick(int clickedItem) {
        if(mToast!= null){
            mToast.cancel();
        }

        mToast = mToast.makeText(detalle, "Elemento pulsado: " + clickedItem, Toast.LENGTH_SHORT);
        mToast.show();
    }
}