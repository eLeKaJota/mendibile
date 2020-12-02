package com.zifu.mendibile.Compra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.CompraIngrediente;
import com.zifu.mendibile.Modelos.CompraLista;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaListaCompra;
import com.zifu.mendibile.tablas.TablaListaCompraIng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class DetalleCompra extends AppCompatActivity{

    Toolbar tlb;
    TextView fecha,notas;
    EditText cantidad,nuevaNota;
    AutoCompleteTextView nuevoIng;
    Spinner formato;
    ImageButton agregar;
    RecyclerView listaProv;
    RecyclerView.Adapter adaptador;
    LayoutManager layoutManager;
    TextInputLayout ilNota;
    ArrayList<String> provs;
    ArrayList<CompraIngrediente> ings;
    CompraLista lista;
    MenuItem guardar,editar;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle data = getIntent().getExtras();
        if (data != null){
            getMenuInflater().inflate(R.menu.menu_detalle_compra,menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_agrega_compra,menu);
        }
        guardar = (MenuItem) menu.findItem(R.id.itmDetalleCompraGuardar);
        editar = (MenuItem) menu.findItem(R.id.itmDetalleCompraEdita);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if(item.getItemId() == R.id.itmDetalleCompraEdita){
            item.setVisible(false);
            guardar.setVisible(true);
            nuevaNota.setText(notas.getText());
            ilNota.setVisibility(View.VISIBLE);
            notas.setVisibility(View.GONE);
        }
        if(item.getItemId() == R.id.itmDetalleCompraGuardar){
            item.setVisible(false);
            editar.setVisible(true);
            notas.setText(nuevaNota.getText());
            ilNota.setVisibility(View.GONE);
            notas.setVisibility(View.VISIBLE);
        }
        if(item.getItemId() == R.id.itmDetalleCompraElimina){
            SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
            String selection = TablaListaCompra.NOMBRE_COLUMNA_1 + " LIKE ?";
            String selectionIngs = TablaListaCompraIng.NOMBRE_COLUMNA_2 + " LIKE ?";
            String[] selectionArgs = {String.valueOf(lista.getId())};
            db.delete(TablaListaCompra.NOMBRE_TABLA,selection,selectionArgs);
            db.delete(TablaListaCompraIng.NOMBRE_TABLA,selectionIngs,selectionArgs);
            finish();
        }
        if(item.getItemId() == R.id.itmAgregaCompraGuardar){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ings.clear();
        provs.clear();
        listaProv.removeAllViews();
        adaptador.notifyDataSetChanged();

        actualizaIngs();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_compra);







        tlb = (Toolbar) findViewById(R.id.tlbDetalleCompra);
        fecha = (TextView) findViewById(R.id.tvDetalleCompraFecha);
        notas = (TextView) findViewById(R.id.tvDetalleCompraNotas);
        cantidad = (EditText) findViewById(R.id.txtDetalleCompraNuevoCantidad);
        nuevaNota = (EditText) findViewById(R.id.txtDetalleCompraNuevaNota);
        ilNota = (TextInputLayout) findViewById(R.id.ilDetalleCompraNuevaNota);
        nuevoIng = (AutoCompleteTextView) findViewById(R.id.txtDetalleCompraNuevoIng);
        formato = (Spinner) findViewById(R.id.spnDetalleCompraNuevoFormato);
        agregar = (ImageButton) findViewById(R.id.ibDetalleCompraNuevoAgregar);
        provs = new ArrayList<>();
        ings = new ArrayList<>();
        listaProv = (RecyclerView) findViewById(R.id.listaDetalleCompra);
        layoutManager = new LinearLayoutManager(this);
        listaProv.setLayoutManager(layoutManager);

        setSupportActionBar(tlb);
        tlb.setTitle("Lista de la compra");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> adaptadorAutoCompleteIngs = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, actualizaIngsArray());
        nuevoIng.setAdapter(adaptadorAutoCompleteIngs);




        Bundle data = getIntent().getExtras();
        if (data != null){
            lista = (CompraLista) data.getSerializable("listaCompra");
            fecha.setText(lista.getFecha());
            notas.setText(lista.getNotas());
        }else{
            SimpleDateFormat fechaT = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm:ss" );
            fechaT.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            fecha.setText(fechaT.format(new Date()));
            notas.setVisibility(View.GONE);
            ilNota.setVisibility(View.VISIBLE);
            int id = agregaListaCompra();
            lista = new CompraLista(id);
        }

        //actualizaIngs();

        nuevaNota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
                String selection = TablaListaCompra.NOMBRE_COLUMNA_1 + " LIKE ?";
                String[] selectionArgs = {String.valueOf(lista.getId())};
                ContentValues v = new ContentValues();
                v.put(TablaListaCompra.NOMBRE_COLUMNA_3,nuevaNota.getText().toString());
                db.update(TablaListaCompra.NOMBRE_TABLA,v,selection,selectionArgs);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        class ProvViewHolder extends RecyclerView.ViewHolder  {
            TextView prov;
            RecyclerView listaIngs;
            RecyclerView.Adapter adaptadorIngs;
            LayoutManager layoutIngs;
            ArrayList<CompraIngrediente> ingProv;



            public ProvViewHolder(@NonNull View itemView) {
                super(itemView);
                prov = (TextView) itemView.findViewById(R.id.tvDetalleCompraProv);
                listaIngs = (RecyclerView) itemView.findViewById(R.id.listaDetalleCompraProv);
                layoutIngs = new LinearLayoutManager(getApplicationContext());
                ingProv = new ArrayList<>();




                //ADAPTADOR DEL RECYCLERVIEW DE PROVEEDOR
                class ingsViewHolder extends RecyclerView.ViewHolder{
                    TextView ing;
                    ImageView elimina;
                    public ingsViewHolder(@NonNull View itemView) {
                        super(itemView);
                        ing = (TextView) itemView.findViewById(R.id.tvDetalleCompraIngProv);
                        elimina = (ImageView) itemView.findViewById(R.id.ivDetalleCompraIngElimina);

                        elimina.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
                                String selection = TablaListaCompraIng.NOMBRE_COLUMNA_2 + " LIKE ? AND " + TablaListaCompraIng.NOMBRE_COLUMNA_3 + " LIKE ?";
                                String[] args = {String.valueOf(ingProv.get(getAdapterPosition()).getIdLista()),ingProv.get(getAdapterPosition()).getNombre()};
                                db.delete(TablaListaCompraIng.NOMBRE_TABLA,selection,args);

                                ings.clear();
                                provs.clear();
                                listaProv.removeAllViews();
                                adaptador.notifyDataSetChanged();

                                actualizaIngs();

                            }
                        });
                    }

                }
                adaptadorIngs = new RecyclerView.Adapter<ingsViewHolder>() {



                    @NonNull
                    @Override
                    public ingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_compra_ing_row, parent, false);
                        return new ingsViewHolder(view);
                    }

                    @Override
                    public void onBindViewHolder(@NonNull ingsViewHolder holder, int position) {

                        holder.ing.setText("[" + ingProv.get(position).getCantidad() + " " + ingProv.get(position).getFormmato() + "] " + ingProv.get(position).getNombre());
                    }

                    @Override
                    public int getItemCount() {
                        return ingProv.size();
                    }

                };
                listaIngs.setLayoutManager(layoutIngs);
                listaIngs.setAdapter(adaptadorIngs);






            }


        }
        adaptador = new RecyclerView.Adapter<ProvViewHolder>() {

            @NonNull
            @Override
            public ProvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_compra_row, parent, false);
                return new ProvViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ProvViewHolder holder, int position) {
                holder.prov.setText(provs.get(position));
                for (CompraIngrediente c : ings){
                    String iProv = actualizaProvs(c.getNombre());
                    String lProv = provs.get(position);
                    if(iProv.equals(lProv)){
                        holder.ingProv.add(c);
                    }
                }
            }


            @Override
            public int getItemCount() {
                return provs.size();
            }
        };
        listaProv.setAdapter(adaptador);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompraIngrediente c = new CompraIngrediente(lista.getId(),nuevoIng.getText().toString(),formato.getSelectedItem().toString(),cantidad.getText().toString());
                agregaListaIng(c);
                ings.clear();
                provs.clear();
                listaProv.removeAllViews();
                adaptador.notifyDataSetChanged();

                actualizaIngs();

                nuevoIng.setText("");
                cantidad.setText("");
                formato.setSelection(0);
                nuevoIng.requestFocus();
            }
        });

    }

    public void actualizaIngs(){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();

        String selection = TablaListaCompraIng.NOMBRE_COLUMNA_2 + " LIKE ?";
        String[] selectionArgs = {String.valueOf(lista.getId())};

        Cursor c = db.query(TablaListaCompraIng.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);
        while(c.moveToNext()){
            int id = c.getInt(0);
            int idLista = c.getInt(1);
            String ing = c.getString(2);
            String formato = c.getString(3);
            String cantidad = c.getString(4);
            String proveedor = actualizaProvs(ing);

            ings.add(new CompraIngrediente(id,idLista,ing,formato,cantidad));
            actualizaListaProvs(proveedor);
        }
        c.close();
    }
    public String actualizaProvs(String nombreIng){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();

        String selection = TablaIngrediente.NOMBRE_COLUMNA_2 + " LIKE ?";
        String[] selectionArgs = {nombreIng};
        String p = "Sin proveedor";

        Cursor c = db.query(TablaIngrediente.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String nombre = c.getString(1);
            Double precio = c.getDouble(2);
            String formato = c.getString(3);
            String proveedor = c.getString(4);
            p = proveedor;
        }
        c.close();

        return p;

    }
    public void actualizaListaProvs(String proveedor){
        Boolean repetido = false;
            for (String s : provs){
                if(s.equals(proveedor)){
                    repetido = true;
                }
            }
            if (!repetido){
                provs.add(proveedor);
            }

    }

    public void agregaListaIng(CompraIngrediente c){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
            if( c.getNombre() != null){
                if(!c.getNombre().equals("")){
                    ContentValues v = new ContentValues();
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_2,c.getIdLista());
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_3,c.getNombre());
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_4,c.getFormmato());
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_5,c.getCantidad());

                    db.insert(TablaListaCompraIng.NOMBRE_TABLA,null,v);
                }
            }
    }
    public String[] actualizaIngsArray(){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        ArrayList<String> ing = new ArrayList<>();

        Cursor c = db.query(TablaIngrediente.NOMBRE_TABLA,null,null,null,null,null,null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String nombre = c.getString(1);
            Double precio = c.getDouble(2);
            String formato = c.getString(3);
            String proveedor = c.getString(4);
            ing.add(nombre);
        }
        c.close();
        return ing.toArray(new String[0]);

    }
    public int agregaListaCompra(){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(TablaListaCompra.NOMBRE_COLUMNA_2,fecha.getText().toString());
//        v.put(TablaListaCompra.NOMBRE_COLUMNA_3,notas.getText().toString());
        long nuevaId = db.insert(TablaListaCompra.NOMBRE_TABLA,null,v);

        return Integer.parseInt(String.valueOf(nuevaId));
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        if (ings.size() == 0) {
            SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
            String selection = TablaListaCompra.NOMBRE_COLUMNA_1 + " LIKE ?";
            String[] selectionArgs = {String.valueOf(lista.getId())};
            db.delete(TablaListaCompra.NOMBRE_TABLA, selection, selectionArgs);
            finish();
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (ings.size() == 0){
//            SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
//            String selection = TablaListaCompra.NOMBRE_COLUMNA_1 + " LIKE ?";
//            String[] selectionArgs = {String.valueOf(lista.getId())};
//            db.delete(TablaListaCompra.NOMBRE_TABLA,selection,selectionArgs);
//            Intent i = new Intent(this,ListaCompra.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
//            finish();
//        }
//    }
}