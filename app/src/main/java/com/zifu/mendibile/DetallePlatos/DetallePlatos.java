package com.zifu.mendibile.DetallePlatos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.ListaPlt.AgregaPlato;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.Modelos.Plato;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;

import static com.zifu.mendibile.MainActivity.helper;

public class DetallePlatos extends AppCompatActivity {
//    PlatosViewPageAdapter platosViewPageAdapter;
//    ViewPager2 viewPager;
    private double costeTotal;
    Plato plato;
    private Toolbar tlb;
    private ImageView fotoPlato;
    private TextView nombrePlato,costeRacion;
    private RecyclerView listaIng;
    private RecyclerView.Adapter adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Ingrediente> ings;
    TextInputLayout tlEditarReceta;
    TextInputEditText txtEditarReceta;
    Button btnEditarReceta;
    TextView tvPlatoCosteTotal,receta;
    DetallePlatos detalle;
    RadioGroup tipoElaboracion;
    EditText numeroElaboracion;
    int estadoReceta = 0;
    LinearLayout lyElaboracion;
    LinearLayout lyCosteElaboracion;
    TextView tvElaboracion;
    TextView tvCosteElaboracion;


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
            i.putExtra("fotoPlato",plato.getFoto());

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


        fotoPlato = (ImageView) findViewById(R.id.ivFotoPlatoDetalle);
        nombrePlato = (TextView) findViewById(R.id.tvDetallePltNombre);
        tipoElaboracion = (RadioGroup) findViewById(R.id.rgDetallePlatoElaboracion);
        numeroElaboracion = (EditText) findViewById(R.id.etDetallePlatoNumeroRacion);
        costeRacion = (TextView) findViewById(R.id.tvDetallePltCosteRacion);
        nombrePlato.setText(plato.getNombre());
        tvPlatoCosteTotal = (TextView) findViewById(R.id.tvDetallePltCoste);
        lyElaboracion = (LinearLayout) findViewById(R.id.lyDetallePlatoElaboracion);
        lyCosteElaboracion = (LinearLayout) findViewById(R.id.lyCosteRacion);
        tvElaboracion = (TextView) findViewById(R.id.tvDetallePlatoElaboracion);
        tvCosteElaboracion = (TextView) findViewById(R.id.tvCosteRacionElaboracion);
        numeroElaboracion.setText(plato.getNumeroElaboracion());

        //--------TIPO DE ELABORACION
        switch (plato.getTipoElaboracion()){
            case "0":
                tipoElaboracion.check(R.id.rbElaboracionRacionUnica);
                lyElaboracion.setVisibility(View.GONE);
                lyCosteElaboracion.setVisibility(View.GONE);
                break;
            case "1":
                tipoElaboracion.check(R.id.rbElaboracionRaciones);
                tvElaboracion.setText("Raciones por elaboración: ");
                lyElaboracion.setVisibility(View.VISIBLE);
                lyCosteElaboracion.setVisibility(View.VISIBLE);
                tvCosteElaboracion.setText("Coste por ración: ");
                int racionNumero = Integer.parseInt(numeroElaboracion.getText().toString());
                costeRacion.setText("" + (double)Math.round( (plato.getCoste()/racionNumero)*100)/100);
                break;
            case "2":
                tipoElaboracion.check(R.id.rbElaboracionUnidades);
                tvElaboracion.setText("Unidades por elaboración: ");
                lyElaboracion.setVisibility(View.VISIBLE);
                lyCosteElaboracion.setVisibility(View.VISIBLE);
                tvCosteElaboracion.setText("Coste por unidad: ");
                int racionNumero2 = Integer.parseInt(numeroElaboracion.getText().toString());
                costeRacion.setText("" + (double)Math.round( (plato.getCoste()/racionNumero2)*100)/100);
                break;
            default:
        }
        //---------------------------

        if(plato.getFoto() != null && !plato.getFoto().equals("null")){
            fotoPlato.setScaleType(ImageView.ScaleType.CENTER_CROP);
            fotoPlato.setImageURI(Uri.parse(plato.getFoto()));
        }
        actualizaCoste();

        listaIng = (RecyclerView) findViewById(R.id.listaDetalleIng);
        listaIng.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listaIng.setLayoutManager(layoutManager);

        actualizaLista();

        //---------BINDEO
        tlEditarReceta = (TextInputLayout) findViewById(R.id.tlEditarReceta);
        txtEditarReceta = (TextInputEditText) findViewById(R.id.txtEditarReceta);
        btnEditarReceta = (Button) findViewById(R.id.btnEditarReceta);
        receta = (TextView) findViewById(R.id.tvDetallePltElaboracion);

        //-----------MOSTRAR/GUARDAR EDITAR RECETA
        btnEditarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estadoReceta ==0) {
                    receta.setVisibility(View.GONE);
                    tlEditarReceta.setVisibility(View.VISIBLE);
                    txtEditarReceta.setText(receta.getText());
                    btnEditarReceta.setText("Guardar receta");
                    estadoReceta = 1;
                }else{
                    receta.setVisibility(View.VISIBLE);
                    tlEditarReceta.setVisibility(View.GONE);
                    receta.setText(txtEditarReceta.getText());
                    editaReceta(txtEditarReceta.getText().toString());
                    btnEditarReceta.setText("Editar receta");
                    estadoReceta = 0;
                }
            }
        });

        receta.setText("" + plato.getElaboracion());

        tipoElaboracion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LinearLayout lyElaboracion = (LinearLayout) findViewById(R.id.lyDetallePlatoElaboracion);
                LinearLayout lyCosteElaboracion = (LinearLayout) findViewById(R.id.lyCosteRacion);
                TextView tvElaboracion = (TextView) findViewById(R.id.tvDetallePlatoElaboracion);
                TextView tvCosteElaboracion = (TextView) findViewById(R.id.tvCosteRacionElaboracion);
                String tipoElaboracion = "0";

                switch (checkedId){
                    case R.id.rbElaboracionRacionUnica:
                        lyElaboracion.setVisibility(View.GONE);
                        lyCosteElaboracion.setVisibility(View.GONE);
                        tipoElaboracion = "0";
                        break;
                    case R.id.rbElaboracionRaciones:
                        tvElaboracion.setText("Raciones por elaboración: ");
                        lyElaboracion.setVisibility(View.VISIBLE);
                        lyCosteElaboracion.setVisibility(View.VISIBLE);
                        tvCosteElaboracion.setText("Coste por ración: ");
                        tipoElaboracion = "1";
                        break;
                    case R.id.rbElaboracionUnidades:
                        tvElaboracion.setText("Unidades por elaboración: ");
                        lyElaboracion.setVisibility(View.VISIBLE);
                        lyCosteElaboracion.setVisibility(View.VISIBLE);
                        tvCosteElaboracion.setText("Coste por unidad: ");
                        tipoElaboracion = "2";
                        break;
                    default:
                }

                plato.setTipoElaboracion(tipoElaboracion);

                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues v = new ContentValues();
                v.put(TablaPlato.NOMBRE_COLUMNA_7,tipoElaboracion);
                String selection = TablaPlato.NOMBRE_COLUMNA_1 + " LIKE ?";
                String[] args = {String.valueOf(plato.getId())};
                db.update(TablaPlato.NOMBRE_TABLA,v,selection,args);

            }
        });

        numeroElaboracion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(numeroElaboracion.getText().toString().equals("")){
                    return;
                }
                int racionNumero = Integer.parseInt(numeroElaboracion.getText().toString());
                if (racionNumero < 1){
                    numeroElaboracion.setText("1");
                }
                costeRacion.setText("" + (double)Math.round( (plato.getCoste()/racionNumero)*100)/100);

                plato.setNumeroElaboracion(numeroElaboracion.getText().toString());

                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues v = new ContentValues();
                v.put(TablaPlato.NOMBRE_COLUMNA_8,plato.getNumeroElaboracion());
                String selection = TablaPlato.NOMBRE_COLUMNA_1 + " LIKE ?";
                String[] args = {String.valueOf(plato.getId())};
                db.update(TablaPlato.NOMBRE_TABLA,v,selection,args);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvPlatoCosteTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int racionNumero = Integer.parseInt(numeroElaboracion.getText().toString());

                costeRacion.setText("" + (double)Math.round( (plato.getCoste()/racionNumero)*100)/100);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



//        viewPager = (ViewPager2) findViewById(R.id.pager);
//        platosViewPageAdapter = new PlatosViewPageAdapter(this);
//        viewPager.setOffscreenPageLimit(5);
//
//        viewPager.setAdapter(platosViewPageAdapter);
//
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        new TabLayoutMediator(tabLayout, viewPager,
//                new TabLayoutMediator.TabConfigurationStrategy() {
//                    @Override
//                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                        if (position == 0){
//                            tab.setText("Resumen");
//                            tab.setIcon(R.drawable.factura);
//                        }
//                        if (position == 1){
//                            tab.setText("Ingredientes");
//                            tab.setIcon(R.drawable.plato);
//                        }
//                        if (position == 2){
//                            tab.setText("Receta");
//                            tab.setIcon(R.drawable.receta);
//                        }
//                    }
//                }).attach();
    }

    public void borrarPlato(String columna, String argumentos, String tabla){
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = columna + " LIKE ?";
        String[] selectionArgs = { argumentos };
        int deletedRows = db.delete(tabla, selection, selectionArgs);

    }
    public void actualizaCoste(){
        tvPlatoCosteTotal.setText("" +(double)Math.round( plato.getCoste()*100)/100);
    }
    public void actualizaLista(){
        SQLiteDatabase db = helper.getReadableDatabase();
        ings = new ArrayList<Ingrediente>();
        for(String i: recuperaIng()){

            String selection = TablaIngrediente.NOMBRE_COLUMNA_1 + " = ?";
            String[] selectionArgs = { i };

            Cursor cursor = db.query(TablaIngrediente.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);

            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            String formato = cursor.getString(3);
            String proveedor = cursor.getString(4);

            Ingrediente ing = new Ingrediente(id,nombre,formato,proveedor,precio);
            ings.add(ing);

            cursor.close();

        }
        adaptador = new AdaptadorListaDetalleIng(ings,helper,this);
        listaIng.setAdapter(adaptador);


    }
    //----------RECUPERA ARRAY DE INGREDIENTES
    public String[] recuperaIng(){
        ArrayList<String> in = new ArrayList<String>();
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        String selection = TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " = ?";
        String[] selectionArgs = { String.valueOf(plato.getId()) };

        Cursor cursor = db.query(TablaPlatoIngredientePeso.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);

        while(cursor.moveToNext()){
            String ingId = cursor.getString(2);
            in.add(String.valueOf(ingId));
        }
        cursor.close();
        String[] i = in.toArray(new String[0]);
        return i;
    }

    //----------------------------------------

    public void editaReceta(String receta){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TablaPlato.NOMBRE_COLUMNA_6,receta);

        String selection = TablaPlato.NOMBRE_COLUMNA_1 + " LIKE ?";
        String[] selectionArgs = { String.valueOf(plato.getId()) };

        db.update(TablaPlato.NOMBRE_TABLA,values,selection,selectionArgs);
    }
}