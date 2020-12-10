package com.zifu.mendibile.Compra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.CompraIngrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaListaCompra;
import com.zifu.mendibile.tablas.TablaListaCompraIng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class AgregaCompraGeneral extends AppCompatActivity {

    RecyclerView listaCompra;
    RecyclerView.Adapter adaptador;
    LayoutManager layoutManager;
    ArrayList<CompraIngrediente> ings;
    SimpleDateFormat fecha;
    TextView tvFecha;
    EditText notas;
    Button btnAgregar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrega_compra_general);

        ings = new ArrayList<>();
        ings.add(new CompraIngrediente());
        tvFecha = (TextView) findViewById(R.id.tvAgregaCompraFecha);
        fecha = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm:ss" );
        fecha.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        tvFecha.setText(fecha.format(new Date()));
        notas = (EditText) findViewById(R.id.txtNotasCompraGeneral);
        btnAgregar = (Button) findViewById(R.id.btnAgregaCompraGeneral);
        listaCompra = (RecyclerView) findViewById(R.id.listaAgregaCompraGeneral);

        layoutManager = new LinearLayoutManager(this);



        class ingsViewHolder extends RecyclerView.ViewHolder{
            EditText cantidad;
            AutoCompleteTextView nombre;
            Spinner formato;
            ImageButton mas;
            ImageView borrar;
            TextView confirmado;
            LinearLayout lyNuevo,lyConfirmado;
            Boolean conf; //PARA CUANDO TOQUE MODIFICAR, USARLO COMO CONTROL.
            public ingsViewHolder(@NonNull View itemView) {
                super(itemView);
                nombre = (AutoCompleteTextView) itemView.findViewById(R.id.etCompraGeneralIng);
                cantidad = (EditText) itemView.findViewById(R.id.etCompraGeneralCantidad);
                formato = (Spinner) itemView.findViewById(R.id.spCompraGeneralFormato);
                mas = (ImageButton) itemView.findViewById(R.id.ibCompraGeneralAñadir);
                borrar = (ImageView) itemView.findViewById(R.id.ivBtnCompraEliminaIng);
                confirmado = (TextView) itemView.findViewById(R.id.tvCompraGeneralIng);
                lyNuevo = (LinearLayout) itemView.findViewById(R.id.layoutCompraIngNuevo);
                lyConfirmado = (LinearLayout) itemView.findViewById(R.id.layoutCompraIngConfirmado);

                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, actualizaIngs());
                nombre.setAdapter(adaptador);



                mas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        lyNuevo.setVisibility(View.GONE);
                        lyConfirmado.setVisibility(View.VISIBLE);
                        confirmado.setText(cantidad.getText() + " " +formato.getSelectedItem().toString() + " - " + nombre.getText());

                        ings.get(getAdapterPosition()).setNombre(nombre.getText().toString());
                        ings.get(getAdapterPosition()).setCantidad(cantidad.getText().toString());
                        ings.get(getAdapterPosition()).setFormmato(formato.getSelectedItem().toString());

                        ings.add(new CompraIngrediente());
                        adaptador.notifyDataSetChanged();
                    }
                });

            }
        }

        adaptador = new RecyclerView.Adapter<ingsViewHolder>() {

            @NonNull
            @Override
            public ingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_compra_general_row, parent, false);
                return new ingsViewHolder(view);
            }



            @Override
            public void onBindViewHolder(@NonNull ingsViewHolder holder, int position) {

                holder.borrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ings.remove(position);
                        //if (position != 0)
                        //listaCompra.getRecycledViewPool().clear();
                            notifyItemRemoved(position);
                           if (position == 0) notifyItemRangeChanged(position, ings.size()) ;
                            //notifyItemChanged(position);


                        //if (position == 0) notifyItemRemoved(position+1);
                       // notifyItemRangeChanged(position,getItemCount());

                        holder.lyNuevo.setVisibility(View.VISIBLE);
                        holder.lyConfirmado.setVisibility(View.GONE);
                        holder.confirmado.setText("");
                        holder.nombre.setText("");
                        holder.cantidad.setText("");
                        holder.formato.setSelection(0);
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
                return ings.size();
            }
        };
        adaptador.setHasStableIds(true);
        listaCompra.setLayoutManager(layoutManager);
        listaCompra.setAdapter(adaptador);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregaListaCompra();
            }
        });

    }




    public void agregaListaCompra(){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(TablaListaCompra.NOMBRE_COLUMNA_2,tvFecha.getText().toString());
        v.put(TablaListaCompra.NOMBRE_COLUMNA_3,notas.getText().toString());
        long nuevaId = db.insert(TablaListaCompra.NOMBRE_TABLA,null,v);
        agregaListaIng(Integer.parseInt(String.valueOf(nuevaId)));
    }

    public void agregaListaIng(int idLista){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();

        for(CompraIngrediente c : ings){
            if( c.getNombre() != null){
                if(!c.getNombre().equals("")){
                    ContentValues v = new ContentValues();
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_2,idLista);
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_3,c.getNombre());
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_4,c.getFormmato());
                    v.put(TablaListaCompraIng.NOMBRE_COLUMNA_5,c.getCantidad());

                    db.insert(TablaListaCompraIng.NOMBRE_TABLA,null,v);
                }
            }
        }

        finish();

    }
    public String[] actualizaIngs(){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        ArrayList<String> ing = new ArrayList<>();

        Cursor c = db.query(TablaIngrediente.NOMBRE_TABLA,null,null,null,null,null,null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String nombre = c.getString(1);
            Double precio = c.getDouble(2);
            String formato = c.getString(3);
            String proveedor = c.getString(4);
            ing.add(nombre);
        }
        c.close();
        return ing.toArray(new String[0]);

    }

}