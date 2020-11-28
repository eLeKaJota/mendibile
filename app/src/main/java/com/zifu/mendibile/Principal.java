package com.zifu.mendibile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zifu.mendibile.Compra.AgregaCompraGeneral;
import com.zifu.mendibile.Compra.ListaCompra;
import com.zifu.mendibile.ListaIng.ListaIngredientes;
import com.zifu.mendibile.ListaPlt.ListaPlatos;
import com.zifu.mendibile.Proveedores.ListaProveedores;

public class Principal extends AppCompatActivity {

    Application application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        ImageButton btnPlatos = (ImageButton)findViewById(R.id.btnPlatos);
        ImageButton btnIngrediente = (ImageButton)findViewById(R.id.btnIngredientes);
        ImageButton btnProveedores = (ImageButton)findViewById(R.id.btnProveedores);
        ImageButton btnCompras = (ImageButton)findViewById(R.id.btnCompras);

        btnPlatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaPlatos.class);
                startActivity(i);
            }
        });

        btnIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaIngredientes.class);
                startActivity(i);
            }
        });
        btnProveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaProveedores.class);
                startActivity(i);
            }
        });
        btnCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaCompra.class);
                startActivity(i);
            }
        });


    }
}