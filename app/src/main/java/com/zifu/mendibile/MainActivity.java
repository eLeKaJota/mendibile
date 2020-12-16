package com.zifu.mendibile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zifu.mendibile.Ajustes.AjustesGeneral;
import com.zifu.mendibile.Compra.ListaCompra;
import com.zifu.mendibile.ListaIng.ListaIngredientes;
import com.zifu.mendibile.ListaPlt.ListaPlatos;
import com.zifu.mendibile.Proveedores.ListaProveedores;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    public static BBDDHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        helper = new BBDDHelper(MainActivity.context);

        LinearLayout llbtnPlatos = (LinearLayout) findViewById(R.id.llbtnPlatos);
        LinearLayout llbtnIngrediente = (LinearLayout) findViewById(R.id.llbtnIngrediente);
        LinearLayout llbtnProveedores = (LinearLayout) findViewById(R.id.llbtnProveedores);
        LinearLayout llbtnCompras = (LinearLayout) findViewById(R.id.llbtnCompras);
        LinearLayout llbtnAjustes = (LinearLayout) findViewById(R.id.llbtnAjustes);
        ImageButton btnPlatos = (ImageButton)findViewById(R.id.btnPlatos);
        ImageButton btnIngrediente = (ImageButton)findViewById(R.id.btnIngredientes);
        ImageButton btnProveedores = (ImageButton)findViewById(R.id.btnProveedores);
        ImageButton btnCompras = (ImageButton)findViewById(R.id.btnCompras);
        ImageButton btnAjustes = (ImageButton)findViewById(R.id.btnAjustes);

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
        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AjustesGeneral.class);
                startActivity(i);
            }
        });







        llbtnPlatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaPlatos.class);
                startActivity(i);
            }
        });

        llbtnIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaIngredientes.class);
                startActivity(i);
            }
        });
        llbtnProveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaProveedores.class);
                startActivity(i);
            }
        });
        llbtnCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaCompra.class);
                startActivity(i);
            }
        });
        llbtnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AjustesGeneral.class);
                startActivity(i);
            }
        });


    }
}