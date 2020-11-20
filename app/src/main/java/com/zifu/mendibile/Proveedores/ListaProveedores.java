package com.zifu.mendibile.Proveedores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zifu.mendibile.ListaIng.AdaptadorListaIng;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.Modelos.Proveedor;
import com.zifu.mendibile.Modelos.ProveedorTlf;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaProveedor;
import com.zifu.mendibile.tablas.TablaProveedorTlf;

import java.util.ArrayList;

public class ListaProveedores extends AppCompatActivity {

    private Toolbar tlb;
    private ArrayList<Proveedor> proveedores;
    private RecyclerView listaProv;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatAgregaProv;
    private int clickItem;


    //----------------TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
    //--------------------------------

    //-----------ON CLICK
    public void onClickProv(int position){
        Intent i = new Intent(this,DetalleProveedores.class);
        i.putExtra("Prov",proveedores.get(position));
        startActivity(i);
    }
    //------------------------


    @Override
    protected void onResume() {
        super.onResume();
        proveedores.clear();

        actualizaLista();
        adaptador.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_proveedores);


        //---------------BINDEOS
        tlb = (Toolbar) findViewById(R.id.tlbListaProv);
        listaProv = (RecyclerView) findViewById(R.id.listaProv);
        floatAgregaProv = (FloatingActionButton) findViewById(R.id.floatAgregaProv);

        //---------------DECLARACIONES
        layoutManager = new LinearLayoutManager(this);
        proveedores = new ArrayList<>();
        //actualizaLista();
        setSupportActionBar(tlb);
        getSupportActionBar().setTitle("Proveedores");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listaProv.setLayoutManager(layoutManager);

        //--------------ADAPTADOR LISTA
        class ProveedorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView tvNombre, tvProducto;
            public ProveedorViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvListaProveedorNombre);
                tvProducto = itemView.findViewById(R.id.tvListaProveedorProducto);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                clickItem = getAdapterPosition();
                onClickProv(clickItem);
            }
        }
        adaptador = new RecyclerView.Adapter<ProveedorViewHolder>() {
            @NonNull
            @Override
            public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View proveedores = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_proveedores_row, parent, false);
                return new ProveedorViewHolder(proveedores);
            }

            @Override
            public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
                holder.tvNombre.setText(proveedores.get(position).getNombre());
                holder.tvProducto.setText(proveedores.get(position).getProducto());
            }

            @Override
            public int getItemCount() {
                return proveedores.size();
            }
        };
        listaProv.setAdapter(adaptador);
        //---------------------------------------------------------------------------

        floatAgregaProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AgregaProveedor.class);
                startActivity(i);
            }
        });





    }
    public void actualizaLista(){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();

        Cursor c = db.query(TablaProveedor.NOMBRE_TABLA,null,null,null,null,null,null);
        while(c.moveToNext()){
            int id = c.getInt(0);
            String nombre = c.getString(1);
            String producto = c.getString(2);
            String cif = c.getString(3);
            String notas = c.getString(4);
            proveedores.add(new Proveedor(id,nombre,producto,cif,actualizaTlf(id),notas,actualizaIng(nombre)));
        }
        c.close();
    }

    public ArrayList<ProveedorTlf> actualizaTlf(int idProv){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        ArrayList<ProveedorTlf> tlfs = new ArrayList<>();
        String selection = TablaProveedorTlf.NOMBRE_COLUMNA_2 + " LIKE ?";
        String selectionArgs[] = {String.valueOf(idProv)};

        Cursor c = db.query(TablaProveedorTlf.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            int idProv2 = c.getInt(1);
            String nombre = c.getString(2);
            String tlf = c.getString(3);
            tlfs.add(new ProveedorTlf(id,idProv2,nombre,tlf));
        }
        c.close();
        return tlfs;
    }

    public ArrayList<Ingrediente> actualizaIng(String prov){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        ArrayList<Ingrediente> ings = new ArrayList<>();
        String selection = TablaIngrediente.NOMBRE_COLUMNA_5 + " LIKE ?";
        String selectionArgs[] = {prov};

        Cursor c = db.query(TablaIngrediente.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);
        while(c.moveToNext()){
            int id = c.getInt(0);
            String nombre = c.getString(1);
            Double precio = c.getDouble(2);
            String formato = c.getString(3);
            String proveedor = c.getString(4);
            ings.add(new Ingrediente(id,nombre,formato,proveedor,precio));
        }
        c.close();
        return ings;
    }
}