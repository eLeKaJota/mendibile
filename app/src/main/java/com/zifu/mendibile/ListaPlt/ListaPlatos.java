package com.zifu.mendibile.ListaPlt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.DetallePlatos.DetallePlatos;
import com.zifu.mendibile.ListaPlt.AdaptadorListaPlt;
import com.zifu.mendibile.ListaPlt.AgregaPlato;
import com.zifu.mendibile.Modelos.IngPeso;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.Modelos.Plato;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaPlatos extends AppCompatActivity implements AdaptadorListaPlt.ListItemClick {
    private RecyclerView listaPlato;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatAgregaPlato;
    private Toast mToast;
    ArrayList<Plato> platos,listaFiltrada;
    final BBDDHelper helper = new BBDDHelper(this);
    private Toolbar tlb;
    private int returnId = 0;
    private boolean buscando = false;
    SearchView buscador;

    //-----------------TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_platos,menu);
        MenuItem buscar = menu.findItem(R.id.itmBuscarPlt);
        buscador = (SearchView) buscar.getActionView();
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String busqueda) {
                try{
                    if(busqueda.length()>0) {
                        buscando = true;
                    }else{
                        buscando = false;
                    }
                    ArrayList<Plato> listaFiltrada = filtro(platos,busqueda);
                    ((AdaptadorListaPlt)listaPlato.getAdapter()).filtro(listaFiltrada);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("bucar",busqueda);
                return false;
            }
        });

        buscar.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                ((AdaptadorListaPlt)listaPlato.getAdapter()).filtro(platos);
                return true;
            }
        });

        return true;
    }

    private ArrayList<Plato> filtro(ArrayList<Plato> platos, String busqueda){
        listaFiltrada = new ArrayList<>();
        try{
            busqueda = busqueda.toLowerCase();
            for (Plato p : platos){
                String pNombre = p.getNombre().toLowerCase();
                if(pNombre.contains(busqueda)){
                    listaFiltrada.add(p);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itmListaPlatosAgrega){
            Intent i = new Intent(getApplicationContext(), AgregaPlato.class);
            i.putExtra("modifica",0);
            startActivity(i);

            return true;
        }
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);
        tlb = (Toolbar) findViewById(R.id.tlbListaPlatos);
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lista de platos");


        //----------------RECUPERA DATOS DE AGREGAPLATO
        Bundle datos = getIntent().getExtras();
        if (datos != null){
            returnId = datos.getInt("id");
        }

        //----------------------------------------------


        listaPlato = (RecyclerView) findViewById(R.id.listaPlato);

        listaPlato.setHasFixedSize(true);

        //listaPlato.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(this);
        listaPlato.setLayoutManager(layoutManager);

        try {
            actualizaLista();
        }catch(Exception e){
            System.out.println(e);
        }


        floatAgregaPlato = (FloatingActionButton) findViewById(R.id.floatAgregaPlato);
        floatAgregaPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AgregaPlato.class);
                i.putExtra("modifica",0);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizaLista();

        //--------------------- ENTRA EN EL PLATO CREADO/MODIFICADO
        if(returnId != 0) {
            System.out.println("ID: " + returnId);
            for (Plato p : platos) {
                if (p.getId() == returnId) {
                    returnId = 0;
                    Intent i = new Intent(this, DetallePlatos.class);
                    i.putExtra("plato",p);
                    startActivity(i);
                    //finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Bundle datos = data.getExtras();
            if(mToast!= null){
                mToast.cancel();
            }

            mToast = mToast.makeText(this, datos.getString("correcto"), Toast.LENGTH_SHORT);
            mToast.show();
            actualizaLista();
        }
    }

    @Override
    public void onListItemClick(int clickedItem) {

        Intent i = new Intent(this, DetallePlatos.class);
        if(buscando) i.putExtra("plato",listaFiltrada.get(clickedItem));
        else i.putExtra("plato",platos.get(clickedItem));
        startActivity(i);
        buscador.setQuery("",false);
        buscador.onActionViewCollapsed();
        buscador.clearFocus();
    }

    public void borrarPlato(String columna, String argumentos, String tabla){
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = columna + " LIKE ?";
        String[] selectionArgs = { argumentos };
        int deletedRows = db.delete(tabla, selection, selectionArgs);
        actualizaLista();
    }


    public void actualizaLista(){

        SQLiteDatabase db = this.helper.getReadableDatabase();

        Cursor cursor = db.query(
                TablaPlato.NOMBRE_TABLA,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        platos = new ArrayList<Plato>();
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            String foto = cursor.getString(2);
            String elaboracion = cursor.getString(3);
            String tipoElaboracion = cursor.getString(4);
            String numeroElaboracion = cursor.getString(5);
            int esIngrediente = cursor.getInt(6);



            Plato plt = new Plato(id,nombre,foto,elaboracion,ingredientesPeso(id),tipoElaboracion,numeroElaboracion,esIngrediente);
            platos.add(plt);
        }
        cursor.close();
        adaptador = new AdaptadorListaPlt(platos, this,helper, this);

        listaPlato.setAdapter(adaptador);


    }
    public String[] recuperaIng(int idPlato){
        ArrayList<String> in = new ArrayList<String>();
        SQLiteDatabase db = this.helper.getReadableDatabase();
        String selection = TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " = ?";


        String[] selectionArgs = { String.valueOf(idPlato) };

        Cursor cursor = db.query(TablaPlatoIngredientePeso.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);

        while(cursor.moveToNext()){
            String ingId = cursor.getString(2);
            in.add(String.valueOf(ingId));
        }
        cursor.close();
        String[] i = in.toArray(new String[0]);
        return i;
    }

    public ArrayList<IngPeso> ingredientesPeso(int id){
        SQLiteDatabase db2 = this.helper.getReadableDatabase();
        ArrayList<IngPeso> ingPesos = new ArrayList<IngPeso>();



            String selection = TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            Cursor cursor2 = db2.query(TablaPlatoIngredientePeso.NOMBRE_TABLA, null, selection, selectionArgs, null, null, null);

            //cursor2.moveToFirst();
            while (cursor2.moveToNext()) {
                int idIng = cursor2.getInt(2);
                double peso = cursor2.getDouble(3);

                ingPesos.add(new IngPeso(idIng,peso));
            }

            cursor2.close();

        return ingPesos;
    }




}