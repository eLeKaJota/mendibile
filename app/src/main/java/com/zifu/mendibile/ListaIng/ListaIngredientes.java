package com.zifu.mendibile.ListaIng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.ListaPlt.AdaptadorListaPlt;
import com.zifu.mendibile.ListaPlt.AgregaPlato;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.Modelos.Plato;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;

import java.util.ArrayList;

public class ListaIngredientes extends AppCompatActivity implements AdaptadorListaIng.ListItemClick {
    private RecyclerView listaIng;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatAgregaIng;
    private Toast mToast;
    ArrayList<Ingrediente> ingredientes, listaFiltrada;
    final BBDDHelper helper = new BBDDHelper(this);
    Toolbar tlb;
    SearchView buscador;
    boolean buscando;


    //-------------TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_ing,menu);
        MenuItem buscar = menu.findItem(R.id.itmBuscarIng);
        buscador = (SearchView) buscar.getActionView();
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String busqueda) {
                if(busqueda.length()>0){
                    buscando = true;
                } else{
                    buscando = false;
                }
                try{
                    listaFiltrada = filtro(ingredientes,busqueda);
                    ((AdaptadorListaIng)listaIng.getAdapter()).filtro(listaFiltrada);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("bucar",busqueda);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(buscar, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                ((AdaptadorListaIng)listaIng.getAdapter()).filtro(ingredientes);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private ArrayList<Ingrediente> filtro(ArrayList<Ingrediente> platos, String busqueda){
        ArrayList<Ingrediente> listaFiltrada = new ArrayList<>();
        try{
            busqueda = busqueda.toLowerCase();
            for (Ingrediente p : platos){
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
        if(item.getItemId() == R.id.itmAgregaIng){
            Intent i = new Intent(getApplicationContext(),AgregaIngrediente.class);
            i.putExtra("modifica",0);
            startActivity(i);
        }
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ingredientes);
        tlb = (Toolbar) findViewById(R.id.tlbListaIng);
        setSupportActionBar(tlb);
        getSupportActionBar().setTitle("Lista de ingredientes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaIng = (RecyclerView) findViewById(R.id.listaIng);

        listaIng.setHasFixedSize(true);

        //listaIng.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(this);
        listaIng.setLayoutManager(layoutManager);


        actualizaLista();


        floatAgregaIng = (FloatingActionButton) findViewById(R.id.floatAgregaIng);
        floatAgregaIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AgregaIngrediente.class);
                i.putExtra("modifica",0);
                startActivity(i);

            }
        });
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
    protected void onResume() {
        super.onResume();
        actualizaLista();
    }

    //----------------CLICK AL INGREDIENTE PARA MODIFICARLO
    @Override
    public void onListItemClick(int clickedItem) {
        Intent i = new Intent(this, AgregaIngrediente.class);
        if(buscando){
            i.putExtra("modifica",listaFiltrada.get(clickedItem).getId());
            i.putExtra("ing",listaFiltrada.get(clickedItem));
        }
        else{
            i.putExtra("modifica",ingredientes.get(clickedItem).getId());
            i.putExtra("ing",ingredientes.get(clickedItem));
        }
        startActivity(i);

        buscador.setQuery("",false);
        buscador.onActionViewCollapsed();
        buscador.clearFocus();
    }


    public void actualizaLista(){

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


        ingredientes = new ArrayList<Ingrediente>();
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            String formato = cursor.getString(3);
            String proveedor = cursor.getString(4);

            Ingrediente ing = new Ingrediente(id,nombre,formato,proveedor,precio);
            ingredientes.add(ing);
        }
        cursor.close();
        adaptador = new AdaptadorListaIng(ingredientes,this,helper, this);
        listaIng.setAdapter(adaptador);


    }
}