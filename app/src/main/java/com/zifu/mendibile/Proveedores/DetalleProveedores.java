package com.zifu.mendibile.Proveedores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.Proveedor;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaProveedor;
import com.zifu.mendibile.tablas.TablaProveedorTlf;

public class DetalleProveedores extends AppCompatActivity {

    Toolbar tlb;
    TextView txtNombre,txtProducto,txtCif,txtNotas;
    RecyclerView listaTlf,listaIng;
    RecyclerView.Adapter adaptadorTlf,adaptadorIng;
    LayoutManager layoutTlf, layoutIng;
    Proveedor prov;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_prov,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if(item.getItemId() == R.id.itmDetalleProvElimina){
            borrarProveedor(TablaProveedor.NOMBRE_COLUMNA_1,String.valueOf(prov.getId()),TablaProveedor.NOMBRE_TABLA);
            borrarProveedor(TablaProveedorTlf.NOMBRE_COLUMNA_2,String.valueOf(prov.getId()),TablaProveedorTlf.NOMBRE_TABLA);
            finish();
        }
        if(item.getItemId() == R.id.itmDetalleProvEdita){

            Intent i = new Intent(this,AgregaProveedor.class);
            i.putExtra("modifica", prov.getId());
            i.putExtra("prov", prov);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_proveedores);
        Bundle datos = getIntent().getExtras();
        prov = (Proveedor) datos.getSerializable("Prov");

        tlb = (Toolbar) findViewById(R.id.tlbDetalleProv);
        txtNombre = (TextView) findViewById(R.id.txtDetalleProvNombre);
        txtProducto = (TextView) findViewById(R.id.txtDetalleProvProductos);
        txtCif = (TextView) findViewById(R.id.txtDetalleProvCif);
        txtNotas = (TextView) findViewById(R.id.txtDetalleProvNotas);
        listaTlf = (RecyclerView) findViewById(R.id.listaDetalleProvTlf);
        listaIng = (RecyclerView) findViewById(R.id.listaDetalleProvIng);

        tlb.setTitle("Proveedor");
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FlowLayoutManager fl = new FlowLayoutManager();
        fl.removeItemPerLineLimit();
        fl.setAutoMeasureEnabled(true);
        fl.setAlignment(Alignment.LEFT);
        layoutIng = fl;
        layoutTlf = new LinearLayoutManager(this);

        listaTlf.setLayoutManager(layoutTlf);
        listaIng.setLayoutManager(layoutIng);
        listaIng.setHasFixedSize(true);

        txtNombre.setText(prov.getNombre());
        txtProducto.setText(prov.getProducto());
        txtCif.setText("cif: " + prov.getCif());
        txtNotas.setText(prov.getNotas());


        //--------------------ADAPTADORES
        class TlfViewHolder extends RecyclerView.ViewHolder{
            TextView nombre,tlf;
            ImageButton llamar;

            public TlfViewHolder(@NonNull View itemView) {

                super(itemView);
                nombre = itemView.findViewById(R.id.txtDetalleProvIng);
                tlf = itemView.findViewById(R.id.txtDetalleProvTlfTlf);
                llamar = itemView.findViewById(R.id.btnAgregaProvOtroTlf);
            }
        }
        class IngViewHolder extends RecyclerView.ViewHolder{
            TextView ing;

            public IngViewHolder(@NonNull View itemView) {
                super(itemView);
                ing = itemView.findViewById(R.id.txtDetalleProvIng);
            }
        }
        adaptadorTlf = new RecyclerView.Adapter<TlfViewHolder>() {

            @NonNull
            @Override
            public TlfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View tlf = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_prov_tlf_row, parent, false);

                return new TlfViewHolder(tlf);
            }

            @Override
            public void onBindViewHolder(@NonNull TlfViewHolder holder, int position) {
                holder.nombre.setText(prov.getTelefonos().get(position).getNombre());
                holder.tlf.setText(prov.getTelefonos().get(position).getTelefono());
                holder.llamar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Llamando a: " + holder.tlf.getText());
                    }
                });
            }

            @Override
            public int getItemCount() {
                return prov.getTelefonos().size();
            }
        };
        adaptadorIng = new RecyclerView.Adapter<IngViewHolder>() {

            @NonNull
            @Override
            public IngViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View ing = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_prov_ing_row, parent, false);
                return new IngViewHolder(ing);
            }

            @Override
            public void onBindViewHolder(@NonNull IngViewHolder holder, int position) {
                holder.ing.setText(prov.getIngredientes().get(position).getNombre());
            }

            @Override
            public int getItemCount() {
                return prov.getIngredientes().size();
            }
        };
        listaIng.setAdapter(adaptadorIng);
        listaTlf.setAdapter(adaptadorTlf);

        //----------------------------------------------------------------------

    }

    public void borrarProveedor(String columna, String argumentos, String tabla){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
        String selection = columna + " LIKE ?";
        String[] selectionArgs = { argumentos };
        int deletedRows = db.delete(tabla, selection, selectionArgs);

    }
}