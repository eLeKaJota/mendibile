package com.zifu.mendibile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zifu.mendibile.Modelos.CompraIngrediente;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AgregaCompraGeneral extends AppCompatActivity {

    RecyclerView listaCompra;
    RecyclerView.Adapter adaptador;
    LayoutManager layoutManager;
    ArrayList<CompraIngrediente> ings;
    SimpleDateFormat fecha;
    TextView tvFecha;


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
        listaCompra = (RecyclerView) findViewById(R.id.listaAgregaCompraGeneral);

        layoutManager = new LinearLayoutManager(this);



        class ingsViewHolder extends RecyclerView.ViewHolder{
            EditText nombre,cantidad;
            Spinner formato;
            ImageButton mas;
            TextView confirmado;
            LinearLayout lyNuevo,lyConfirmado;
            public ingsViewHolder(@NonNull View itemView) {
                super(itemView);
                nombre = (EditText) itemView.findViewById(R.id.etCompraGeneralIng);
                cantidad = (EditText) itemView.findViewById(R.id.etCompraGeneralCantidad);
                formato = (Spinner) itemView.findViewById(R.id.spCompraGeneralFormato);
                mas = (ImageButton) itemView.findViewById(R.id.ibCompraGeneralAñadir);
                confirmado = (TextView) itemView.findViewById(R.id.tvCompraGeneralIng);
                lyNuevo = (LinearLayout) itemView.findViewById(R.id.layoutCompraIngNuevo);
                lyConfirmado = (LinearLayout) itemView.findViewById(R.id.layoutCompraIngConfirmado);

                mas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        lyNuevo.setVisibility(View.GONE);
                        lyConfirmado.setVisibility(View.VISIBLE);
                        confirmado.setText(cantidad.getText() + " " +formato.getSelectedItem().toString() + " - " + nombre.getText());
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

    }

}