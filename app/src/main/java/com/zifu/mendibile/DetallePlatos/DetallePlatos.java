package com.zifu.mendibile.DetallePlatos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zifu.mendibile.ListaPlt.AgregaPlato;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.Plato;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

public class DetallePlatos extends AppCompatActivity {
    PlatosViewPageAdapter platosViewPageAdapter;
    ViewPager2 viewPager;
    private double costeTotal;
    Plato plato;
    private Toolbar tlb;


    public double getCosteTotal() {
        return costeTotal;
    }

    public void setCosteTotal(double costeTotal) {
        this.costeTotal = costeTotal;
    }



    public Plato getPlato() {
        return plato;
    }

    public void setPlato(Plato plato) {
        this.plato = plato;
    }

    //------------------TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles_plato,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home) onBackPressed();
        if ( item.getItemId() == R.id.itmDetalleElimina) {
            borrarPlato(TablaPlato.NOMBRE_COLUMNA_1,String.valueOf(plato.getId()),TablaPlato.NOMBRE_TABLA);
            borrarPlato(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2,String.valueOf(plato.getId()),TablaPlatoIngredientePeso.NOMBRE_TABLA);
            finish();
        }
        if ( item.getItemId() == R.id.itmDetalleModifica) {
            Intent i = new Intent(this, AgregaPlato.class);
            i.putExtra("modifica",plato.getId());
            i.putExtra("ing",plato.getIngPeso());
            i.putExtra("nombrePlato",plato.getNombre());

            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    //----------------------------------------------------------


    @Override
    protected void onResume() {
        super.onResume();


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_platos);
        Bundle extras = getIntent().getExtras();
        plato = (Plato) extras.getSerializable("plato");
        tlb = (Toolbar) findViewById(R.id.tlbDetalle);
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalles del plato");


        viewPager = (ViewPager2) findViewById(R.id.pager);
        platosViewPageAdapter = new PlatosViewPageAdapter(this);
        viewPager.setOffscreenPageLimit(5);

        viewPager.setAdapter(platosViewPageAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0){
                            tab.setText("Resumen");
                            tab.setIcon(R.drawable.factura);
                        }
                        if (position == 1){
                            tab.setText("Ingredientes");
                            tab.setIcon(R.drawable.plato);
                        }
                        if (position == 2){
                            tab.setText("Receta");
                            tab.setIcon(R.drawable.receta);
                        }
                    }
                }).attach();
    }

    public void borrarPlato(String columna, String argumentos, String tabla){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
        String selection = columna + " LIKE ?";
        String[] selectionArgs = { argumentos };
        int deletedRows = db.delete(tabla, selection, selectionArgs);

    }
}