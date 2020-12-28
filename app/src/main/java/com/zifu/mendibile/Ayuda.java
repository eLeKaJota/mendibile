package com.zifu.mendibile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Ayuda extends AppCompatActivity {
    Toolbar tlb;
    TextView txtAyudaAgregaIng,txtAyudaAgregaPlt,txtAyudaAgregaElaboracionG,txtAyudaAgregaElaboracionIng,txtAyudaAgregaProv,txtAyudaAgregaCompra,txtAyudaCambiaMoneda,txtAyudaCambiarCabecera;

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
        setContentView(R.layout.activity_ayuda);

        tlb = (Toolbar) findViewById(R.id.tlbAyuda);
        setSupportActionBar(tlb);
        getSupportActionBar().setTitle("Ayuda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtAyudaAgregaIng = (TextView) findViewById(R.id.txtAyudaAgregaIng);
        txtAyudaAgregaPlt = (TextView) findViewById(R.id.txtAyudaAgregaPlt);
        txtAyudaAgregaElaboracionG = (TextView) findViewById(R.id.txtAyudaAgregaElaboracionG);
        txtAyudaAgregaElaboracionIng = (TextView) findViewById(R.id.txtAyudaAgregaElaboracionIng);
        txtAyudaAgregaProv = (TextView) findViewById(R.id.txtAyudaAgregaProv);
        txtAyudaAgregaCompra = (TextView) findViewById(R.id.txtAyudaAgregaCompra);
        txtAyudaCambiaMoneda = (TextView) findViewById(R.id.txtAyudaCambiaMoneda);
        txtAyudaCambiarCabecera = (TextView) findViewById(R.id.txtAyudaCambiarCabecera);

        txtAyudaAgregaIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/agregar-ingrediente.html");
            }
        });
        txtAyudaAgregaPlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/agregar-plato.html");
            }
        });
        txtAyudaAgregaElaboracionG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/elaboracion-grande.html");
            }
        });
        txtAyudaAgregaElaboracionIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/elaboracion-ingrediente.html");
            }
        });
        txtAyudaAgregaProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/agregar-proveedor.html");
            }
        });
        txtAyudaAgregaCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/agregar-compra.html");
            }
        });
        txtAyudaCambiaMoneda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/cambiar-divisa.html");
            }
        });
        txtAyudaCambiarCabecera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com/mendibil/documentacion/cambiar-cabecera-compra.html");
            }
        });
    }

    public void abrirNavegador(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}