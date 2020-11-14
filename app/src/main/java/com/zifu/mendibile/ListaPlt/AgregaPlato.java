package com.zifu.mendibile.ListaPlt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.DetallePlatos.DetallePlatos;
import com.zifu.mendibile.ListaIng.AdaptadorListaIng;
import com.zifu.mendibile.ListaIng.AgregaIngrediente;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.IngPeso;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;

public class AgregaPlato extends AppCompatActivity implements AdaptadorListaAgrega.ListItemClick {

    RecyclerView listaAgrega, listaAgregado;
    RecyclerView.Adapter adaptadorAgrega, adaptadorAgregado;
    RecyclerView.LayoutManager layoutManagerAgrega , layoutManagerAgregado;
    BBDDHelper helper;
    ArrayList<Ingrediente> ingredientes;
    ArrayList<Ingrediente> ingAgregados;
    private Toolbar tlb;
    EditText txtNombre, txtIngredientes, txtElaboracion;
    Button btnAgregar;
    Button btnMostrarIngAgrega;
    int modifica;
    ArrayList<IngPeso> ing;


    //------------------MOSTRAR LISTA DE INGREDIENTES
    public void mostrarAgrega(){
        if ( listaAgrega.getVisibility() == View.GONE) listaAgrega.setVisibility(View.VISIBLE);
        else listaAgrega.setVisibility(View.GONE);
    }


    //-------------------TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agrega_platos,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itmAgregaPlato){
            agregaPlato();
            return true;
        }
        if(item.getItemId() == R.id.itmAgregaPlatoIng){
            Intent i = new Intent(this, AgregaIngrediente.class);
            startActivity(i);
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------


    @Override
    protected void onResume() {
        super.onResume();
        //TODO actualizar correctamente sin repetir elementos

        if(modifica == 0){
//            if(!ingredientes.isEmpty()){
//                ingredientes.clear();
//            }
//            if(!ingAgregados.isEmpty()){
//                ingAgregados.clear();
//            }
            actualizaListaAgrega();
        }
        else {
//            if(!ingredientes.isEmpty()){
//                ingredientes.clear();
//            }
//            if(!ingAgregados.isEmpty()){
//                ingAgregados.clear();
//            }
            modificaListaAgrega();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrega_plato);
         helper = new BBDDHelper(this);
         tlb = (Toolbar) findViewById(R.id.tlbAgregaPlato);
         setSupportActionBar(tlb);
         getSupportActionBar().setTitle("Añadir nuevo plato");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         Bundle datos = getIntent().getExtras();
         modifica = datos.getInt("modifica");
         ing = (ArrayList<IngPeso>) datos.getSerializable("ing");
        ingredientes = new ArrayList<Ingrediente>();
        ingAgregados = new ArrayList<Ingrediente>();

        //----------------BINDEOS
        txtNombre = (EditText) findViewById(R.id.txtPltNombre);
        txtElaboracion = (EditText) findViewById(R.id.txtPltElaboracion);
        btnAgregar = (Button) findViewById(R.id.btnPltAgregar);
        listaAgrega = (RecyclerView) findViewById(R.id.listaAgregaIngPlato);
        listaAgregado = (RecyclerView) findViewById(R.id.listaAgregadoIngPlato);
        btnMostrarIngAgrega = (Button)  findViewById(R.id.btnMostrar);
        if (modifica != 0) btnAgregar.setText("Modificar plato");
        if (modifica != 0) txtNombre.setText(datos.getString("nombrePlato"));


        //--------------CONFIGURA LOS DOS RECYCLERVIEW
        layoutManagerAgrega = new GridLayoutManager(this,4);
        layoutManagerAgregado = new LinearLayoutManager(this);
        listaAgrega.setHasFixedSize(true);
        listaAgrega.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        listaAgrega.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        listaAgrega.setLayoutManager(layoutManagerAgrega);
        listaAgregado.setHasFixedSize(true);
        listaAgregado.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        listaAgregado.setLayoutManager(layoutManagerAgregado);

        //---------------MOSTRAR U OCULTAR LISTA DE INGREDIENTES
        btnMostrarIngAgrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAgrega();
            }
        });


        //---------------ADAPTADOR AGREGADOS
        class AgregadoViewHolder extends RecyclerView.ViewHolder{
            TextView tvNombre;
            ImageButton btnQuitar;
            public AgregadoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvAgregadoIngNombre);
                btnQuitar = itemView.findViewById(R.id.btnAgregadoIngQuitar);

            }
        }
        adaptadorAgregado = new RecyclerView.Adapter<AgregadoViewHolder>() {
            @NonNull
            @Override
            public AgregadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View agregados = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_agregado_ing_plato, parent, false);

                return new AgregadoViewHolder(agregados);
            }

            @Override
            public void onBindViewHolder(@NonNull AgregadoViewHolder holder, int position) {
                holder.tvNombre.setText(ingAgregados.get(position).getNombre());
                holder.btnQuitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ingredientes.size() >= ingAgregados.get(position).getId()) {
                            ingredientes.add(ingAgregados.get(position).getId() - 1, ingAgregados.get(position));
                        }
                        else{
                            ingredientes.add(ingAgregados.get(position));
                        }

                        ingAgregados.remove(position);
                        notifyDataSetChanged();
                        adaptadorAgrega.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return ingAgregados.size();
            }
        };

        listaAgregado.setAdapter(adaptadorAgregado);
        //------------------------------------------------------------------------------------









        //------------AGREGA EL PLATO A LA BASE DE DATOS
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                agregaPlato();
            }
        });


    }
    public void agregaPlato(){
        String[] ingArray = new String[ingAgregados.size()];
        long nuevaId;

        for(int i = 0; i < ingAgregados.size(); i++){
            ingArray[i] = String.valueOf(ingAgregados.get(i).getId());
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        //----------------AGREGA EL PLATO
        ContentValues values = new ContentValues();
        values.put(TablaPlato.NOMBRE_COLUMNA_2,txtNombre.getText().toString());
        //values.put(TablaPlato.NOMBRE_COLUMNA_4, TextUtils.join(",",ingArray));
        //values.put(TablaPlato.NOMBRE_COLUMNA_6,txtElaboracion.getText().toString());

        if (modifica == 0) {
            nuevaId = db.insert(TablaPlato.NOMBRE_TABLA,null,values);
        }else{
            String selection = TablaPlato.NOMBRE_COLUMNA_1 + " LIKE ?";
            String[] selectionArgs = { String.valueOf(modifica) };
            nuevaId = db.update(TablaPlato.NOMBRE_TABLA,values,selection,selectionArgs);
            db.delete(TablaPlatoIngredientePeso.NOMBRE_TABLA,TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " LIKE ?",selectionArgs);
        }
        System.out.println("ID PLATO: " + nuevaId);

        //----------------AGREGA EL INGPESO

        SQLiteDatabase db2 = helper.getWritableDatabase();

        for(String x : ingArray){
            double p = 0.5;
            ContentValues values2 = new ContentValues();
            if (modifica == 0) values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2,String.valueOf(nuevaId));
            if (modifica != 0) values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2,String.valueOf(modifica));
            values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_3,x);
            if (modifica !=0){
                for ( IngPeso v : this.ing){

                    System.out.println("X: " + x + " ID: " + v.getIdIng() + " PESO: " + p);
                    if (x.equals(String.valueOf(v.getIdIng()))) p = v.getPeso();

                }
            }
            values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_4,String.valueOf(p));
            long IngNuevaId = db2.insert(TablaPlatoIngredientePeso.NOMBRE_TABLA,null,values2);

        }

        Intent i = new Intent(this , ListaPlatos.class);
        if (modifica == 0) i.putExtra("id",Integer.parseInt(String.valueOf(nuevaId)));
        if (modifica != 0) i.putExtra("id",modifica);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        finish();
    }

    public void actualizaListaAgrega(){
        SQLiteDatabase db = this.helper.getReadableDatabase();

        Cursor cursor = db.query(
                TablaIngrediente.NOMBRE_TABLA,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        //ingredientes = new ArrayList<Ingrediente>();
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            String formato = cursor.getString(3);
            String proveedor = cursor.getString(4);

            Ingrediente ing = new Ingrediente(id,nombre,formato,proveedor,precio);
            System.out.println("OOOOH");
            ingredientes.add(ing);
        }
        cursor.close();
        adaptadorAgrega = new AdaptadorListaAgrega(ingredientes,this,helper, this);
        listaAgrega.setAdapter(adaptadorAgrega);
    }

    public void modificaListaAgrega(){
        SQLiteDatabase db = this.helper.getReadableDatabase();



        Cursor cursor = db.query(
                TablaIngrediente.NOMBRE_TABLA,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        //ingredientes = new ArrayList<Ingrediente>();
        ArrayList<Ingrediente> temp = new ArrayList<>();
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            String formato = cursor.getString(3);
            String proveedor = cursor.getString(4);


            Ingrediente ing = new Ingrediente(id,nombre,formato,proveedor,precio);
            temp.add(ing);




            }
        for ( Ingrediente i : temp){
            int x = 0;
            for(IngPeso p : this.ing){
                if(p.getIdIng() == i.getId()){
                    ingAgregados.add(i);
                    System.out.println("Agregados: " + i.getNombre());
                    x = 1;
                }
            }
            System.out.println("Agregar: " + i.getNombre());
            if(x==0) ingredientes.add(i);



        }
        cursor.close();
        adaptadorAgrega = new AdaptadorListaAgrega(ingredientes,this,helper, this);
        listaAgrega.setAdapter(adaptadorAgrega);
    }


    @Override
    public void onListItemClick(int clickedItem) {
        System.out.println("Click: " +clickedItem);
    }
}