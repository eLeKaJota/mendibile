package com.zifu.mendibile.ListaIng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;
import com.zifu.mendibile.tablas.TablaProveedor;

import java.util.ArrayList;

public class AgregaIngrediente extends AppCompatActivity  {
    EditText txtNombre, txtPrecio;
    AutoCompleteTextView txtProveedor;
    //ListaIngredientes listaIng;
    Spinner spnFormato;
    Button btnAgregar,btnEliminarIng;
    Toolbar tlb;
    BBDDHelper helper;
    int modifica;
    Ingrediente ing;

    //-------------TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agrega_ing,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itmAgregaNuevoIng) agregaIng();
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrega_ingrediente);
        Bundle data = getIntent().getExtras();
        modifica = data.getInt("modifica");
        ing = (Ingrediente) data.getSerializable("ing");
         helper = new BBDDHelper(this);

        txtNombre = (EditText) findViewById(R.id.txtPltNombre);
        txtPrecio = (EditText) findViewById(R.id.txtIngPrecio);
        txtProveedor = (AutoCompleteTextView) findViewById(R.id.txtIngProveedor);
        spnFormato = (Spinner) findViewById(R.id.spnFormato);
        btnEliminarIng = (Button) findViewById(R.id.btnEliminarIng);
        ArrayAdapter<CharSequence> adaptadorSpinner = ArrayAdapter.createFromResource(this,R.array.listaFormatoIng, android.R.layout.simple_spinner_dropdown_item);
        spnFormato.setAdapter(adaptadorSpinner);
        tlb = (Toolbar) findViewById(R.id.tlbAgregaIng);
        setSupportActionBar(tlb);
        getSupportActionBar().setTitle("Añadir ingrediente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> adaptadorProv = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, actualizaProvs());
        txtProveedor.setAdapter(adaptadorProv);



        btnAgregar = (Button) findViewById(R.id.btnPltAgregar);
        if(modifica!=0){
            btnAgregar.setText("Modificar ingrediente");
            btnEliminarIng.setVisibility(View.VISIBLE);
            txtNombre.setText(ing.getNombre());
            txtPrecio.setText(String.valueOf(ing.getPrecio()));
            spnFormato.setSelection(adaptadorSpinner.getPosition(ing.getFormato()));
            txtProveedor.setText(ing.getProveedor());
        }



        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                agregaIng();

            }
        });

        btnEliminarIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertEliminar = new AlertDialog.Builder(AgregaIngrediente.this);
                alertEliminar.setTitle("Eliminar Ingrediente");
                alertEliminar.setMessage("¿Estás seguro de que quiere eliminar este ingrediente?");
                alertEliminar.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrarIngrediente(TablaIngrediente.NOMBRE_COLUMNA_1,String.valueOf(modifica),TablaIngrediente.NOMBRE_TABLA);
                        borrarIngrediente(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_3,String.valueOf(modifica),TablaPlatoIngredientePeso.NOMBRE_TABLA);
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
        });


    }
    public void agregaIng(){
        if(txtNombre.getText().toString().equals("")){
            Toast nombreVacio =
                    Toast.makeText(getApplicationContext(),"El campo de nombre no puede estar vacío", Toast.LENGTH_SHORT);
            nombreVacio.show();
            return;
        }
        if(comprobarIng(txtNombre.getText().toString().toLowerCase()) && modifica == 0){
            Toast repedito = Toast.makeText(this,"Ya existe un ingrediente con el mismo nombre.",Toast.LENGTH_SHORT);
            repedito.show();
            return;
        }
        if(txtPrecio.getText().toString().equals("")){
            txtPrecio.setText("0");
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TablaIngrediente.NOMBRE_COLUMNA_2,txtNombre.getText().toString());
        values.put(TablaIngrediente.NOMBRE_COLUMNA_3,Double.parseDouble(txtPrecio.getText().toString()));
        values.put(TablaIngrediente.NOMBRE_COLUMNA_4,spnFormato.getSelectedItem().toString());
        values.put(TablaIngrediente.NOMBRE_COLUMNA_5,txtProveedor.getText().toString());

        if (modifica ==0){
            long nuevaId = db.insert(TablaIngrediente.NOMBRE_TABLA,null,values);
            Intent i = new Intent();
            i.putExtra("idIng",String.valueOf(nuevaId));
            setResult(Activity.RESULT_OK,i);
        }else{
            String selection = TablaIngrediente.NOMBRE_COLUMNA_1 + " LIKE ?";
            String[] selectionArgs = { String.valueOf(modifica) };
            db.update(TablaIngrediente.NOMBRE_TABLA,values,selection,selectionArgs);
        }

        finish();
    }

    public void borrarIngrediente(String columna, String argumentos, String tabla){

        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = columna + " LIKE ?";
        String[] selectionArgs = { argumentos };
        int deletedRows = db.delete(tabla, selection, selectionArgs);
        finish();
    }

    public String[] actualizaProvs(){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        ArrayList<String> prov = new ArrayList<>();

        Cursor c = db.query(TablaProveedor.NOMBRE_TABLA,null,null,null,null,null,null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String nombre = c.getString(1);
            prov.add(nombre);
        }
        c.close();
        return prov.toArray(new String[0]);

    }

    public boolean comprobarIng(String nuevoIng){
        ArrayList<Ingrediente> i = new ArrayList<>();
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        Cursor c = db.query(TablaIngrediente.NOMBRE_TABLA, null, null, null, null, null, null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String nombre = c.getString(1).toLowerCase();
            i.add(new Ingrediente(id,nombre));
        }
        c.close();

        for(Ingrediente ing : i){
            if(ing.getNombre().equals(nuevoIng)){
                return true;
            }
        }
        return false;
    }



}