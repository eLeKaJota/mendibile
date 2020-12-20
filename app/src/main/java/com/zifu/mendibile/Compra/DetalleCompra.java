package com.zifu.mendibile.Compra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.CompraIngrediente;
import com.zifu.mendibile.Modelos.CompraLista;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaListaCompra;
import com.zifu.mendibile.tablas.TablaListaCompraIng;
import com.zifu.mendibile.tablas.TablaProveedor;

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
    RecyclerView listaIngs;
    RecyclerView.Adapter adaptadorIngs;
    LayoutManager layoutManager;
    TextInputLayout ilNota;
    ArrayList<String> provs;
    ArrayList<CompraIngrediente> ings;
    CompraLista lista;
    MenuItem guardar,editar;
    String sinProv;




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
            AlertDialog.Builder alertEliminar = new AlertDialog.Builder(this);
            alertEliminar.setTitle("Eliminar lista");
            alertEliminar.setMessage("¿Estás seguro de que quiere eliminar esta lista de la compra?");
            alertEliminar.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
                    String selection = TablaListaCompra.NOMBRE_COLUMNA_1 + " LIKE ?";
                    String selectionIngs = TablaListaCompraIng.NOMBRE_COLUMNA_2 + " LIKE ?";
                    String[] selectionArgs = {String.valueOf(lista.getId())};
                    db.delete(TablaListaCompra.NOMBRE_TABLA,selection,selectionArgs);
                    db.delete(TablaListaCompraIng.NOMBRE_TABLA,selectionIngs,selectionArgs);
                    finish();
                }
            });
            alertEliminar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            AlertDialog dialog = alertEliminar.create();
            dialog.show();



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


        SharedPreferences ajustes = this.getSharedPreferences("com.zifu.mendibil", Context.MODE_PRIVATE);
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
        getSupportActionBar().setTitle("Lista de la compra");
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
            fechaT.setTimeZone(TimeZone.getDefault());
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
            LayoutManager layoutIngs;
            ArrayList<CompraIngrediente> ingProv;
            String listaCompra = ajustes.getString("cabecera","Pedido:") + "\n";
            ImageButton copiar,compartir,compartirTodo;



            public ProvViewHolder(@NonNull View itemView) {
                super(itemView);
                prov = (TextView) itemView.findViewById(R.id.tvDetalleCompraProv);
                listaIngs = (RecyclerView) itemView.findViewById(R.id.listaDetalleCompraProv);
                layoutIngs = new LinearLayoutManager(getApplicationContext());
                ingProv = new ArrayList<>();
                copiar = (ImageButton) itemView.findViewById(R.id.ibDetalleCompraCopiar);
                compartir = (ImageButton) itemView.findViewById(R.id.ibDetalleCompraCompartir);
                compartirTodo = (ImageButton) itemView.findViewById(R.id.ibDetalleCompraCompartirTodo);




                //ADAPTADOR DEL RECYCLERVIEW DE PROVEEDOR
                class ingsViewHolder extends RecyclerView.ViewHolder  {
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

                        copiar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast copiado = Toast.makeText(getApplicationContext(),"Lista de compra copiada",Toast.LENGTH_SHORT);
                                copiado.show();

                                ClipboardManager portapapeles = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData datos = ClipData.newPlainText("ListaCompra", listaCompra);
                                if (portapapeles == null || datos == null) return;
                                portapapeles.setPrimaryClip(datos);
                            }
                        });

                        compartir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent compartirLista = new Intent();
                                compartirLista.setAction(Intent.ACTION_SEND);
                                compartirLista.putExtra(Intent.EXTRA_TEXT, listaCompra);
                                compartirLista.setType("text/rtf");
                                compartirLista.setPackage("com.whatsapp");

                                Intent shareIntent = Intent.createChooser(compartirLista, null);
                                startActivity(shareIntent);
                            }
                        });
                        compartirTodo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent compartirLista = new Intent();
                                compartirLista.setAction(Intent.ACTION_SEND);
                                compartirLista.putExtra(Intent.EXTRA_TEXT, listaCompra);
                                compartirLista.setType("text/rtf");

                                Intent shareIntent = Intent.createChooser(compartirLista, null);
                                startActivity(shareIntent);
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
                        if (!ingProv.get(position).getCantidad().equals("")){
                            holder.ing.setText("[" + ingProv.get(position).getCantidad() + " " + ingProv.get(position).getFormmato() + "] " + ingProv.get(position).getNombre());
                            listaCompra = listaCompra + "[" + ingProv.get(position).getCantidad() + " " + ingProv.get(position).getFormmato() + "] " + ingProv.get(position).getNombre();
                        }else{
                            holder.ing.setText("[ * ] " + ingProv.get(position).getNombre());
                            listaCompra = listaCompra + "[ * ] " + ingProv.get(position).getNombre();
                        }
                        if(position < getItemCount() -1){
                            listaCompra = listaCompra  + "\n";
                        }


                    }

                    @Override
                    public int getItemCount() {
                        return ingProv.size();
                    }
                    @Override
                    public int getItemViewType(int position) {
                        return super.getItemViewType(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return super.getItemId(position);
                    }

                };
                listaIngs.setLayoutManager(layoutIngs);
                adaptadorIngs.setHasStableIds(true);
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

                    //String iProv = actualizaProvs(c.getNombre());
                    String iProv = c.getProveedor();
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

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }
        };
        adaptador.setHasStableIds(true);
        listaProv.setAdapter(adaptador);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( nuevoIng.getText().toString().equals("")){
                    Toast ingRepetido = Toast.makeText(getApplicationContext(),"El campo nuevo ingrediente no puede estar vacío",Toast.LENGTH_SHORT);
                    ingRepetido.show();
                    return;
                }
                for (CompraIngrediente ci : ings){
                    if ( ci.getNombre().equals(nuevoIng.getText().toString())){
                        Toast ingRepetido = Toast.makeText(getApplicationContext(),"Ya tienes un ingrediente con el mismo nombre",Toast.LENGTH_SHORT);
                        ingRepetido.show();
                        return;
                    }
                }

                if(actualizaProvs(nuevoIng.getText().toString()) == "Sin proveedor"){
                    //--------PROVS
                    AutoCompleteTextView txtProv = new AutoCompleteTextView(getApplicationContext());
                    txtProv.setThreshold(1);
                    txtProv.setHint("Nombre del proveedor");
                    ArrayAdapter<String> adaptadorAutoCompleteProv = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, actualizaProvsArray());
                    txtProv.setAdapter(adaptadorAutoCompleteProv);
                    //----------------
                    AlertDialog.Builder alertProv = new AlertDialog.Builder(DetalleCompra.this);
                    alertProv.setTitle("Proveedor");
                    alertProv.setMessage("Este ingrediente no tiene asignado ningún proveedor.\n¿Quieres asignar uno manualmente?:");
                    alertProv.setView(txtProv);
                    alertProv.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String prov = "Sin proveedor";
                            if (!txtProv.getText().toString().equals("")) prov = txtProv.getText().toString();
                            CompraIngrediente c = new CompraIngrediente(lista.getId(),nuevoIng.getText().toString(),formato.getSelectedItem().toString(),cantidad.getText().toString(),prov);

                            agregaListaIng(c);
                            ings.clear();
                            provs.clear();
                            listaProv.removeAllViews();

                            actualizaIngs();

                            nuevoIng.setText("");
                            cantidad.setText("");
                            formato.setSelection(0);
                            nuevoIng.requestFocus();
                        }
                    });
                    alertProv.setNegativeButton("Sin proveedor", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            CompraIngrediente c = new CompraIngrediente(lista.getId(),nuevoIng.getText().toString(),formato.getSelectedItem().toString(),cantidad.getText().toString(),"Sin proveedor");

                            agregaListaIng(c);
                            ings.clear();
                            provs.clear();
                            listaProv.removeAllViews();

                            actualizaIngs();

                            nuevoIng.setText("");
                            cantidad.setText("");
                            formato.setSelection(0);
                            nuevoIng.requestFocus();
                        }
                    });
                    alertProv.show();
                }else{
                    CompraIngrediente c = new CompraIngrediente(lista.getId(),nuevoIng.getText().toString(),formato.getSelectedItem().toString(),cantidad.getText().toString(),actualizaProvs(nuevoIng.getText().toString()));

                    agregaListaIng(c);
                    ings.clear();
                    provs.clear();
                    listaProv.removeAllViews();

                    actualizaIngs();

                    nuevoIng.setText("");
                    cantidad.setText("");
                    formato.setSelection(0);
                    nuevoIng.requestFocus();
                }
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
            String proveedor;
            if(c.getString(5) != null) proveedor = c.getString(5);
            else proveedor = actualizaProvs(ing);
            ings.add(new CompraIngrediente(id,idLista,ing,formato,cantidad,proveedor));
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
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_6,c.getProveedor());

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
    public String[] actualizaProvsArray(){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        ArrayList<String> p = new ArrayList<>();

        Cursor c = db.query(TablaProveedor.NOMBRE_TABLA,null,null,null,null,null,null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String nombre = c.getString(1);
            p.add(nombre);
        }
        c.close();
        return p.toArray(new String[0]);

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