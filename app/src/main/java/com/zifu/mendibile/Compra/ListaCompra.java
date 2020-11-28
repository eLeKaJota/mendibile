package com.zifu.mendibile.Compra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

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

import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.CompraLista;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaListaCompra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ListaCompra extends AppCompatActivity {

    Toolbar tlb;
    RecyclerView listaCompra;
    RecyclerView.Adapter adaptador;
    LayoutManager layoutManager;
    ArrayList<CompraLista> compraListas;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_compra,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if(item.getItemId() == R.id.itmAgregaListaCompra){
            Intent i = new Intent(this,AgregaCompraGeneral.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        compraListas.clear();
        actualizaLista();
        adaptador.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra);

        tlb = (Toolbar) findViewById(R.id.tlbListaCompra);
        tlb.setTitle("Listas de compra");
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        compraListas = new ArrayList<>();
        listaCompra = (RecyclerView) findViewById(R.id.listaListaCompra);
        layoutManager = new LinearLayoutManager(this);
        listaCompra.setLayoutManager(layoutManager);

        class listaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView fecha, notas;

            public listaViewHolder(@NonNull View itemView) {
                super(itemView);
                fecha = (TextView) itemView.findViewById(R.id.tvListaCompraFecha);
                notas = (TextView) itemView.findViewById(R.id.tvListaCompraNotas);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),DetalleCompra.class);
                i.putExtra("listaCompra",compraListas.get(getAdapterPosition()));
                startActivity(i);
            }
        }

        adaptador = new RecyclerView.Adapter<listaViewHolder>() {

            @NonNull
            @Override
            public listaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_compra_row, parent, false);
                return new listaViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull listaViewHolder holder, int position) {
                holder.fecha.setText(compraListas.get(position).getFecha());
                holder.notas.setText(compraListas.get(position).getNotas());
            }

            @Override
            public int getItemCount() {
                return compraListas.size();
            }
        };
        listaCompra.setAdapter(adaptador);
    }



    public void actualizaLista(){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();

        Cursor c = db.query(TablaListaCompra.NOMBRE_TABLA,null,null,null,null,null,null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String fecha = c.getString(1);
            String notas = c.getString(2);
            compraListas.add(new CompraLista(id,fecha,notas));
        }
        c.close();
        Collections.reverse(compraListas);
    }
}