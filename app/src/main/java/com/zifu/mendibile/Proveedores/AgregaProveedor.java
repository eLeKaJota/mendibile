package com.zifu.mendibile.Proveedores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.ProveedorTlf;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaProveedor;
import com.zifu.mendibile.tablas.TablaProveedorTlf;

import java.util.ArrayList;

public class AgregaProveedor extends AppCompatActivity {

    Toolbar tlb;
    RecyclerView listaTlf;
    RecyclerView.Adapter adaptador;
    LayoutManager layoutManager;
    ArrayList<ProveedorTlf> tlfs;
    EditText nombre,productos,cif,notas;
    Button agregar;

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
        setContentView(R.layout.activity_agrega_proveedor);

        tlb = (Toolbar) findViewById(R.id.tlbAgregaProv);
        listaTlf = (RecyclerView) findViewById(R.id.listaProvTlf);
        nombre = (EditText) findViewById(R.id.txtProvNombre);
        productos = (EditText) findViewById(R.id.txtProvProductos);
        cif = (EditText) findViewById(R.id.txtProvCif);
        notas = (EditText) findViewById(R.id.txtProvNotas);
        agregar = (Button) findViewById(R.id.btnProvAgregar);

        tlfs = new ArrayList<>();
        tlfs.add(new ProveedorTlf(1,1,"",""));
        tlb.setTitle("Agregar proveedor");
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        listaTlf.setLayoutManager(layoutManager);

        class TlfViewHolder extends RecyclerView.ViewHolder{
            EditText nombre,telefono;
            ImageButton agregar;

            public TlfViewHolder(@NonNull View itemView) {
                super(itemView);
                nombre = (EditText) itemView.findViewById(R.id.txtAgregaProvTlfNombre);
                telefono = (EditText) itemView.findViewById(R.id.txtAgregaProvTlfTlf);
                agregar = (ImageButton) itemView.findViewById(R.id.btnAgregaProvOtroTlf);
            }
        }
        adaptador = new RecyclerView.Adapter<TlfViewHolder>() {

            @NonNull
            @Override
            public TlfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_agrega_prov_tlf_row, parent, false);
                return new TlfViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull TlfViewHolder holder, int position) {
                holder.nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tlfs.get(position).setNombre(holder.nombre.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                holder.telefono.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tlfs.get(position).setTelefono(holder.telefono.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                holder.agregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tlfs.add(new ProveedorTlf(1,1,"",""));
                        holder.agregar.setVisibility(View.INVISIBLE);
                        notifyItemInserted(tlfs.size());

                    }
                });
            }

            @Override
            public int getItemCount() {
                return tlfs.size();
            }
        };
        listaTlf.setAdapter(adaptador);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregaProveedor();
                finish();
            }
        });
    }

    public void agregaProveedor(){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(TablaProveedor.NOMBRE_COLUMNA_2,nombre.getText().toString());
        v.put(TablaProveedor.NOMBRE_COLUMNA_3,productos.getText().toString());
        v.put(TablaProveedor.NOMBRE_COLUMNA_4,cif.getText().toString());
        v.put(TablaProveedor.NOMBRE_COLUMNA_5,notas.getText().toString());
        long nuevaId = db.insert(TablaProveedor.NOMBRE_TABLA,null,v);
        agregaTlf(Integer.parseInt(String.valueOf(nuevaId)));
    }
    public void agregaTlf(int idProv){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();

        for (ProveedorTlf t : tlfs){
            ContentValues v = new ContentValues();
            v.put(TablaProveedorTlf.NOMBRE_COLUMNA_2,idProv);
            v.put(TablaProveedorTlf.NOMBRE_COLUMNA_3,t.getNombre());
            v.put(TablaProveedorTlf.NOMBRE_COLUMNA_4,t.getTelefono());
            db.insert(TablaProveedorTlf.NOMBRE_TABLA,null,v);
        }

    }
}