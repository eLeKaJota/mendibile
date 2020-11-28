package com.zifu.mendibile.Compra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.CompraIngrediente;
import com.zifu.mendibile.Modelos.CompraLista;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaListaCompraIng;

import java.util.ArrayList;

public class DetalleCompra extends AppCompatActivity {

    Toolbar tlb;
    TextView fecha,notas;
    RecyclerView listaProv;
    RecyclerView.Adapter adaptador;
    LayoutManager layoutManager;
    ArrayList<String> provs;
    ArrayList<CompraIngrediente> ings;
    CompraLista lista;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_compra);

        Bundle data = getIntent().getExtras();
        lista = (CompraLista) data.getSerializable("listaCompra");

        tlb = (Toolbar) findViewById(R.id.tlbDetalleCompra);
        fecha = (TextView) findViewById(R.id.tvDetalleCompraFecha);
        notas = (TextView) findViewById(R.id.tvDetalleCompraNotas);
        provs = new ArrayList<>();
        ings = new ArrayList<>();
        listaProv = (RecyclerView) findViewById(R.id.listaDetalleCompra);
        layoutManager = new LinearLayoutManager(this);
        listaProv.setLayoutManager(layoutManager);

        setSupportActionBar(tlb);
        tlb.setTitle("Lista de la compra");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fecha.setText(lista.getFecha());
        notas.setText(lista.getNotas());

        actualizaIngs();

        class ProvViewHolder extends RecyclerView.ViewHolder{
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
                    public ingsViewHolder(@NonNull View itemView) {
                        super(itemView);
                        ing = (TextView) itemView.findViewById(R.id.tvDetalleCompraIngProv);

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
            System.out.println(provs);

    }

}