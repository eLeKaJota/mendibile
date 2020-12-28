package com.zifu.mendibile.DetallePlatos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zifu.mendibile.Proveedores.AgregaProveedor;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaListaCompra;
import com.zifu.mendibile.tablas.TablaListaCompraIng;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;

import static com.zifu.mendibile.MainActivity.helper;

public class DetallePlatos extends AppCompatActivity {
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
    Switch elaboracionIngrediente;
    String moneda,monedaSimbolo;
    TextView simboloMonedaTotal,simboloMonedaRacion;
    private static final int CODIGO_PERMISOS_LECTURA = 745;


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
            AlertDialog.Builder alertEliminar = new AlertDialog.Builder(this);
            alertEliminar.setTitle("Eliminar Plato");
            alertEliminar.setMessage("¿Estás seguro de que quiere eliminar este plato?");
            alertEliminar.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    borrarPlato(TablaPlato.NOMBRE_COLUMNA_1,String.valueOf(plato.getId()),TablaPlato.NOMBRE_TABLA);
                    borrarPlato(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2,String.valueOf(plato.getId()),TablaPlatoIngredientePeso.NOMBRE_TABLA);
                    finish();
                }
            });
            alertEliminar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            AlertDialog dialog = alertEliminar.create();
            dialog.show();

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

        SharedPreferences ajustes = this.getSharedPreferences("com.zifu.mendibil", Context.MODE_PRIVATE);
        moneda = ajustes.getString("moneda","euro");
        switch (moneda){
            case "euro":
                monedaSimbolo = "€";
                break;
            case "dolar":
                monedaSimbolo = "$";
                break;
            case "libra":
                monedaSimbolo = "£";
                break;
            case "yen":
                monedaSimbolo = "¥";
                break;
            case "yuan":
                monedaSimbolo = "¥";
                break;
            default:
                monedaSimbolo = "€";
        }


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
        elaboracionIngrediente = (Switch) findViewById(R.id.swElaboracionIngrediente);
        numeroElaboracion.setText(plato.getNumeroElaboracion());
        simboloMonedaTotal = (TextView) findViewById(R.id.txtTotalMoneda);
        simboloMonedaRacion = (TextView) findViewById(R.id.txtRacionMoneda);

        simboloMonedaTotal.setText(monedaSimbolo);
        simboloMonedaRacion.setText(monedaSimbolo);



        //--------COMPRUEBA SI LA ELABORACIÖN ES UN INGREDIENTE
        if(comprobarIng(plato.getNombre().toLowerCase())){
            elaboracionIngrediente.setChecked(true);
        }else{
            elaboracionIngrediente.setChecked(false);
        }
        //----------------------------------------------------

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
            int estadoDePermiso = ContextCompat.checkSelfPermission(DetallePlatos.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
                fotoPlato.setScaleType(ImageView.ScaleType.CENTER_CROP);
                fotoPlato.setImageURI(Uri.parse(plato.getFoto()));
            } else {
                ActivityCompat.requestPermissions(DetallePlatos.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODIGO_PERMISOS_LECTURA);
            }

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

                if(comprobarIng(plato.getNombre().toLowerCase())){
                    String precio;
                    switch (tipoElaboracion.getCheckedRadioButtonId()){
                        case R.id.rbElaboracionRacionUnica:
                            precio = tvPlatoCosteTotal.getText().toString();
                            break;
                        case R.id.rbElaboracionRaciones:
                            precio = costeRacion.getText().toString();
                            break;
                        case R.id.rbElaboracionUnidades:
                            precio = costeRacion.getText().toString();
                            break;
                        default:
                            precio = "";
                    }

                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues v = new ContentValues();
                    v.put(TablaIngrediente.NOMBRE_COLUMNA_3,precio);
                    String selection = TablaIngrediente.NOMBRE_COLUMNA_1 + " LIKE ?";
                    String[] args = {String.valueOf(plato.getEsIngrediente())};
                    db.update(TablaIngrediente.NOMBRE_TABLA,v,selection,args);
                }






            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //------------SWITCH PARA HACERLO INGREDIENTE
        elaboracionIngrediente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(comprobarIng(plato.getNombre().toLowerCase())){
                        Toast repedito = Toast.makeText(getApplicationContext(),"Ya existe un ingrediente con el mismo nombre.",Toast.LENGTH_SHORT);
                        repedito.show();
                        isChecked = false;
                        return;
                    }

                    agregaIng();

                    Toast elabIng = Toast.makeText(getApplicationContext(), "Elaboración añadida a la lista de ingredientes",Toast.LENGTH_SHORT );
                    elabIng.show();
                }else{
                    borrarIngrediente(TablaIngrediente.NOMBRE_COLUMNA_1,String.valueOf(plato.getEsIngrediente()),TablaIngrediente.NOMBRE_TABLA);
                    borrarIngrediente(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_3,String.valueOf(plato.getEsIngrediente()),TablaPlatoIngredientePeso.NOMBRE_TABLA);

                    Toast elabIng = Toast.makeText(getApplicationContext(), "Elaboración eliminada de la lista de ingredientes",Toast.LENGTH_SHORT );
                    elabIng.show();
                }
            }
        });
        //------------------------------------------

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_LECTURA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fotoPlato.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    fotoPlato.setImageURI(Uri.parse(plato.getFoto()));
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Cambiar los permisos en configuración");
                    alertDialogBuilder.setMessage("Para poder ver la foto Haz click en 'Configuración -> Permisos -> Almacén -> Permitir'");
                    alertDialogBuilder.setPositiveButton("Configuracón", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    alertDialogBuilder.show();

                }
                break;
        }
    }

    public void agregaIng(){

        //TODO Comprobar si es racion, raciones o unidad
        String precio = "";
        String formato = "";
        switch (tipoElaboracion.getCheckedRadioButtonId()){
            case R.id.rbElaboracionRacionUnica:
                precio = tvPlatoCosteTotal.getText().toString();
                formato = "Ud.";
                break;
            case R.id.rbElaboracionRaciones:
                precio = costeRacion.getText().toString();
                formato = "Ración";
                break;
            case R.id.rbElaboracionUnidades:
                precio = costeRacion.getText().toString();
                formato = "Ud.";
                break;

        }


        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TablaIngrediente.NOMBRE_COLUMNA_2,plato.getNombre());
        values.put(TablaIngrediente.NOMBRE_COLUMNA_3,precio);
        values.put(TablaIngrediente.NOMBRE_COLUMNA_4,formato);
        values.put(TablaIngrediente.NOMBRE_COLUMNA_5,"Elaboración propia");

//        if (modifica ==0){
            long nuevaId = db.insert(TablaIngrediente.NOMBRE_TABLA,null,values);

            ContentValues vPlato = new ContentValues();
            int idI = Integer.parseInt(String.valueOf(nuevaId));
            vPlato.put(TablaPlato.NOMBRE_COLUMNA_9,String.valueOf(idI));
            String[] args = {String.valueOf(plato.getId())};

            db.update(TablaPlato.NOMBRE_TABLA,vPlato,TablaPlato.NOMBRE_COLUMNA_1 + " LIKE ?",args);
            plato.setEsIngrediente(idI);
//        }else{
//            String selection = TablaIngrediente.NOMBRE_COLUMNA_1 + " LIKE ?";
//            String[] selectionArgs = { String.valueOf(modifica) };
//            db.update(TablaIngrediente.NOMBRE_TABLA,values,selection,selectionArgs);
//        }

    }

    public void borrarIngrediente(String columna, String argumentos, String tabla){

        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = columna + " LIKE ?";
        String[] selectionArgs = { argumentos };
        int deletedRows = db.delete(tabla, selection, selectionArgs);
    }

    public boolean comprobarIng(String nuevoIng){
        ArrayList<Ingrediente> i = new ArrayList<>();
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        Cursor c = db.query(TablaIngrediente.NOMBRE_TABLA, null, null, null, null, null, null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String nombre = c.getString(1).toLowerCase();
            i.add(new Ingrediente(id,nombre));
        }
        c.close();

        for(Ingrediente ing : i){
            if(ing.getNombre().equals(nuevoIng)){
                return true;
            }
        }
        return false;
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