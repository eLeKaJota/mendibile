package com.zifu.mendibile.Ajustes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zifu.mendibile.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class AjustesGeneral extends AppCompatActivity {
    Toolbar tlb;
    EditText cabecera;
    TextView acercaDe,backup,restore,recomendar,politicaPrivacidad;
    String moneda;
    Spinner ajustesMoneda;

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
        setContentView(R.layout.activity_ajustes_general);

        tlb = findViewById(R.id.tlbAjustes);
        setSupportActionBar(tlb);
        getSupportActionBar().setTitle("Ajustes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recomendar = findViewById(R.id.txtRecomendarApp);
        acercaDe = findViewById(R.id.txtAcercaDe);
        backup = findViewById(R.id.txtAjustesBackup);
        restore = findViewById(R.id.txtAjustesRestore);
        cabecera = (EditText) findViewById(R.id.txtCabeceraPersonalizada);
        ajustesMoneda = (Spinner) findViewById(R.id.spnAjustesMoneda);


        SharedPreferences ajustes = this.getSharedPreferences("com.zifu.mendibil", Context.MODE_PRIVATE);
        cabecera.setText(ajustes.getString("cabecera","Pedido:"));
        moneda = ajustes.getString("moneda","euro");

        switch (moneda){
            case "euro":
                ajustesMoneda.setSelection(0);
                break;
            case "dolar":
                ajustesMoneda.setSelection(1);
                break;
            case "libra":
                ajustesMoneda.setSelection(2);
                break;
            case "yen":
                ajustesMoneda.setSelection(3);
                break;
            case "yuan":
                ajustesMoneda.setSelection(4);
                break;
            default:
                ajustesMoneda.setSelection(0);
        }

        ajustesMoneda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ajustes.edit().putString("moneda","euro").apply();
                        break;
                    case 1:
                        ajustes.edit().putString("moneda","dolar").apply();
                        break;
                    case 2:
                        ajustes.edit().putString("moneda","libra").apply();
                        break;
                    case 3:
                        ajustes.edit().putString("moneda","yen").apply();
                        break;
                    case 4:
                        ajustes.edit().putString("moneda","yuan").apply();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        cabecera.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ajustes.edit().putString("cabecera",s.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBackup = new AlertDialog.Builder(AjustesGeneral.this);
                alertBackup.setTitle("Respaldo");
                alertBackup.setMessage("La función para respaldar datos estará disponible pronto.");
                alertBackup.setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBackup.show();
                //backUp();
            }
        });
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBackup = new AlertDialog.Builder(AjustesGeneral.this);
                alertBackup.setTitle("Restaurar");
                alertBackup.setMessage("La función para restaurar datos estará disponible pronto.");
                alertBackup.setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBackup.show();
                //restore();
            }
        });

        recomendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertProximamente = new AlertDialog.Builder(AjustesGeneral.this);
                alertProximamente.setTitle("Recomendar");
                alertProximamente.setMessage("La función recomendar estará disponible pronto.");
                alertProximamente.setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertProximamente.show();
            }
        });

        acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AjustesGeneral.this,AcercaDe.class);
                startActivity(i);
            }
        });



    }

    public void backUp() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/com.zifu.mendibile/databases/Mendibile.db";
                String backupDBPath = "Mendibile.db";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                Log.d("DB path", "" + currentDB.getAbsolutePath());
                Log.d("backupDB path", "" + backupDB.getAbsolutePath());

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getApplicationContext(), "Respaldo guardado en la tarjeta SD", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "No se puede acceder a la tarjeta SD", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void restore() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/com.zifu.mendibile/databases/Mendibile.db";
                String backupDBPath = "Mendibile.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getApplicationContext(), "Base de datos restaurada correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}