package com.zifu.mendibile.ListaIng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;

public class AgregaIngrediente extends AppCompatActivity implements DialogInterface {
    EditText txtNombre, txtPrecio, txtProveedor;
    //ListaIngredientes listaIng;
    Spinner spnFormato;
    Button btnAgregar,btnPrueba;
    Toolbar tlb;
    BBDDHelper helper;

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

         helper = new BBDDHelper(this);

        txtNombre = (EditText) findViewById(R.id.txtPltNombre);
        txtPrecio = (EditText) findViewById(R.id.txtIngPrecio);
        txtProveedor = (EditText) findViewById(R.id.txtIngProveedor);
        spnFormato = (Spinner) findViewById(R.id.spnFormato);
        ArrayAdapter<CharSequence> adaptadorSpinner = ArrayAdapter.createFromResource(this,R.array.listaFormatoIng, android.R.layout.simple_spinner_dropdown_item);
        spnFormato.setAdapter(adaptadorSpinner);
        tlb = (Toolbar) findViewById(R.id.tlbAgregaIng);
        setSupportActionBar(tlb);
        getSupportActionBar().setTitle("Añadir ingrediente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnAgregar = (Button) findViewById(R.id.btnPltAgregar);


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregaIng();

            }
        });


    }
    public void agregaIng(){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TablaIngrediente.NOMBRE_COLUMNA_2,txtNombre.getText().toString());
        values.put(TablaIngrediente.NOMBRE_COLUMNA_3,Double.parseDouble(txtPrecio.getText().toString()));
        values.put(TablaIngrediente.NOMBRE_COLUMNA_4,spnFormato.getSelectedItem().toString());
        values.put(TablaIngrediente.NOMBRE_COLUMNA_5,txtProveedor.getText().toString());

        long nuevaId = db.insert(TablaIngrediente.NOMBRE_TABLA,null,values);
        //Toast.makeText(getApplicationContext(),"Registro añadido correctamente: id[" + nuevaId + "]",Toast.LENGTH_LONG).show();
        Intent i = new Intent();
        i.putExtra("idIng",String.valueOf(nuevaId));
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }
}