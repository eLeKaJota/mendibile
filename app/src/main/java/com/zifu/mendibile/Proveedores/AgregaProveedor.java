package com.zifu.mendibile.Proveedores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;
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
import com.zifu.mendibile.Modelos.Proveedor;
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
    Proveedor prov;
    int modifica;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agrega_prov,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if(item.getItemId() == R.id.itmAgregaNuevoProv){
            agregaProveedor();
            finish();
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

        Bundle datos = getIntent().getExtras();
        if(datos != null){
            modifica = datos.getInt("modifica");
            prov = (Proveedor) datos.getSerializable("prov");
        }

        if (modifica != 0){
            tlfs = prov.getTelefonos();
            nombre.setText(prov.getNombre());
            productos.setText(prov.getProducto());
            cif.setText(prov.getCif());
            notas.setText(prov.getNotas());
            agregar.setText("Modificar proveedor");

        }else{
            tlfs = new ArrayList<>();
            tlfs.add(new ProveedorTlf(1,1,"",""));
        }

        tlb.setTitle("Agregar proveedor");
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        listaTlf.setLayoutManager(layoutManager);

        class TlfViewHolder extends RecyclerView.ViewHolder{
            EditText nombre,telefono;
            ImageButton agregar;
            int borrar = 0;

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

                if(modifica!=0){
                    holder.nombre.setText(tlfs.get(position).getNombre());
                    holder.telefono.setText(tlfs.get(position).getTelefono());
                }

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
            //TODO Arreglar el cambio de botones y orden.
                        if(holder.borrar == 0){
                            tlfs.add(0,new ProveedorTlf(1,1,"",""));
                            holder.agregar.setImageResource(android.R.drawable.ic_menu_delete);
                            holder.borrar = 1;
                            notifyItemInserted(tlfs.size());
                        }else{
                            holder.borrar = 0;
                            holder.agregar.setImageResource(android.R.drawable.ic_input_add);
                            tlfs.remove(position);
                            notifyDataSetChanged();
                        }


                    }
                });
            }

            @Override
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }
            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemCount() {
                return tlfs.size();
            }
        };
        adaptador.setHasStableIds(true);
        listaTlf.setAdapter(adaptador);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregaProveedor();
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

        if(modifica!=0){
            String selection = TablaProveedor.NOMBRE_COLUMNA_1 + " LIKE ?";
            String selectionArgs[] = {String.valueOf(modifica)};
            db.update(TablaProveedor.NOMBRE_TABLA,v,selection,selectionArgs);
            db.delete(TablaProveedorTlf.NOMBRE_TABLA,TablaProveedorTlf.NOMBRE_COLUMNA_2 + " LIKE ? ",new String[] {String.valueOf(modifica)});
            agregaTlf(modifica);
        }else {
            long nuevaId = db.insert(TablaProveedor.NOMBRE_TABLA, null, v);
            agregaTlf(Integer.parseInt(String.valueOf(nuevaId)));
        }
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

        Intent i = new Intent(this,ListaProveedores.class);
        i.putExtra("id" , idProv);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}