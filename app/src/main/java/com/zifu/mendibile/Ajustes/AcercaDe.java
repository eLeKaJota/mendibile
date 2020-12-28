package com.zifu.mendibile.Ajustes;

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

import com.zifu.mendibile.R;

public class AcercaDe extends AppCompatActivity {
    Toolbar tlb;
    TextView politicaPrivacidad, web, mail;

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
        setContentView(R.layout.activity_acerca_de);

        tlb = (Toolbar) findViewById(R.id.tlbAcercaDe);
        setSupportActionBar(tlb);
        getSupportActionBar().setTitle("Acerca de");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        politicaPrivacidad = (TextView) findViewById(R.id.txtPoliticaPrivacidad);
        web = (TextView) findViewById(R.id.txtEnlaceWeb);
        mail = (TextView) findViewById(R.id.txtEnlaceMail);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("mailto:alifuhkv@gmail.com?Subject=Mendibile:%20Gestión%20de%20cocina");
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://alifuhkv.com");
            }
        });
        politicaPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNavegador("https://sites.google.com/view/alifuhkvprivacidad/inicio");
            }
        });

    }

    public void abrirNavegador(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}