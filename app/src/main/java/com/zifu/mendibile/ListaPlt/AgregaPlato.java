package com.zifu.mendibile.ListaPlt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.ListaIng.AgregaIngrediente;
import com.zifu.mendibile.Modelos.IngPeso;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AgregaPlato extends AppCompatActivity implements AdaptadorListaAgrega.ListItemClick {

    RecyclerView listaAgrega, listaAgregado;
    RecyclerView.Adapter adaptadorAgrega, adaptadorAgregado;
    RecyclerView.LayoutManager layoutManagerAgrega , layoutManagerAgregado;
    BBDDHelper helper;
    ArrayList<Ingrediente> ingredientes;
    ArrayList<Ingrediente> ingAgregados;
    private Toolbar tlb;
    EditText txtNombre, txtIngredientes, txtElaboracion;
    Button btnAgregar,btnFoto,btnGaleria;
    //Button btnMostrarIngAgrega;
    int modifica;
    ArrayList<IngPeso> ing;
    ImageView fotoPlato;
    Uri fotoUriTemp;
    Uri fotoUriOld;
    Uri fotoUri;


    //------------------MOSTRAR LISTA DE INGREDIENTES
    public void mostrarAgrega(){
        if ( listaAgrega.getVisibility() == View.GONE) listaAgrega.setVisibility(View.VISIBLE);
        else listaAgrega.setVisibility(View.GONE);
    }

    //--------------METODO PARA USAR LA CAMARA
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int DEVUELVE_NUEVO_INGREDIENTE = 28;
    static final int REQUEST_TAKE_PHOTO = 27;
    static final int FOTO_GALERIA = 26;


   String pathFotoPlato;

    private File crearArchivoImagen() throws IOException{
        String fecha = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String nombreFichero = "PLATO_" + fecha + "_";
        File directorio = getFilesDir();
        File foto = File.createTempFile(nombreFichero,".jpg",directorio);
        pathFotoPlato = foto.getAbsolutePath();
        return foto;
    }
    private void hacerFotoIntent(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if ( i.resolveActivity(getPackageManager()) != null){
            File archivoFoto = null;
            try{
                archivoFoto = crearArchivoImagen();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(archivoFoto != null){
                fotoUriOld = fotoUriTemp;
                fotoUriTemp = FileProvider.getUriForFile(this,"com.zifu.mendibile",archivoFoto);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fotoUriTemp);
                startActivityForResult(i,REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void abrirGaleria(){
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, FOTO_GALERIA);
    }

    private void borrarFoto(Uri uri) throws IOException {
        File foto = new File(uri.getPath());
        foto.setWritable(true);

        deleteFile(foto.getName());
    }

    //-------------------TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agrega_platos,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itmAgregaPlato){
            agregaPlato();
            return true;
        }
        if(item.getItemId() == R.id.itmAgregaPlatoIng){
            Intent i = new Intent(this, AgregaIngrediente.class);
            i.putExtra("modifica",0);
            startActivityForResult(i,DEVUELVE_NUEVO_INGREDIENTE);
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------



    //-----------------SI AÑADES UN NUEVO INGREDIENTE DESDE ESTA ACTIVITY, SE ACTUALIZA EN LA LISTA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DEVUELVE_NUEVO_INGREDIENTE){
            if(resultCode == AppCompatActivity.RESULT_OK){
                int idIng = Integer.parseInt(data.getStringExtra("idIng"));
                if (idIng != 0) ingredientes.add(actualizaIngrediente(idIng));
            }
        }
        if(requestCode == FOTO_GALERIA){
            if(resultCode == RESULT_OK){
                //TODO es posible que queden fotos viejas sin borrar
                fotoUri = data.getData();
                fotoPlato.setScaleType(ImageView.ScaleType.CENTER_CROP);
                fotoPlato.setImageURI(fotoUri);
            }
        }


        if (requestCode == REQUEST_TAKE_PHOTO) {
            if(resultCode == RESULT_OK){
                fotoUri = fotoUriTemp;
                if (fotoUriOld != null) {
                    try {
                        borrarFoto(fotoUriOld);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fotoPlato.setScaleType(ImageView.ScaleType.CENTER_CROP);
                fotoPlato.setImageURI(fotoUri);
            }else{

                try {
                    borrarFoto(fotoUriTemp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrega_plato);
         helper = new BBDDHelper(this);
         tlb = (Toolbar) findViewById(R.id.tlbAgregaPlato);
         setSupportActionBar(tlb);
         getSupportActionBar().setTitle("Añadir nuevo plato");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         Bundle datos = getIntent().getExtras();
         modifica = datos.getInt("modifica");
         ing = (ArrayList<IngPeso>) datos.getSerializable("ing");

        ingredientes = new ArrayList<Ingrediente>();
        ingAgregados = new ArrayList<Ingrediente>();

        //----------------BINDEOS
        txtNombre = (EditText) findViewById(R.id.txtPltNombre);
        //txtElaboracion = (EditText) findViewById(R.id.txtPltElaboracion);
        btnAgregar = (Button) findViewById(R.id.btnPltAgregar);
        btnFoto = (Button) findViewById(R.id.btnFotoPlato);
        listaAgrega = (RecyclerView) findViewById(R.id.listaAgregaIngPlato);
        listaAgregado = (RecyclerView) findViewById(R.id.listaAgregadoIngPlato);
        fotoPlato = (ImageView) findViewById(R.id.ivFotoPlato);
        btnGaleria = (Button) findViewById(R.id.btnGaleria);
        //btnMostrarIngAgrega = (Button)  findViewById(R.id.btnMostrar);
        if (modifica != 0) btnAgregar.setText("Modificar plato");
        if (modifica != 0) txtNombre.setText(datos.getString("nombrePlato"));
        if(modifica != 0) {
            fotoUri = Uri.parse(datos.getString("fotoPlato"));
            fotoUriTemp = Uri.parse(datos.getString("fotoPlato"));
            fotoPlato.setScaleType(ImageView.ScaleType.CENTER_CROP);
            fotoPlato.setImageURI(fotoUri);
        }

        //--------------ACTUALIZA LOS RECYCLERVIEW
        if (modifica == 0) actualizaListaAgrega();
        if (modifica != 0) modificaListaAgrega();


        //--------------CONFIGURA LOS DOS RECYCLERVIEW
        layoutManagerAgrega = new GridLayoutManager(this,4);
        layoutManagerAgregado = new GridLayoutManager(this,1);
        listaAgrega.setHasFixedSize(true);
        listaAgrega.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        listaAgrega.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        listaAgrega.setLayoutManager(layoutManagerAgrega);
        //listaAgregado.setHasFixedSize(true);
        listaAgregado.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        listaAgregado.setLayoutManager(layoutManagerAgregado);

        //---------------ABRE LA GALERÍA
        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });


        //---------------ADAPTADOR AGREGADOS
        class AgregadoViewHolder extends RecyclerView.ViewHolder{
            TextView tvNombre;
            ImageButton btnQuitar;
            public AgregadoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvAgregadoIngNombre);
                btnQuitar = itemView.findViewById(R.id.btnAgregadoIngQuitar);

            }
        }
        adaptadorAgregado = new RecyclerView.Adapter<AgregadoViewHolder>() {
            @NonNull
            @Override
            public AgregadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View agregados = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_agregado_ing_plato, parent, false);

                return new AgregadoViewHolder(agregados);
            }

            @Override
            public void onBindViewHolder(@NonNull AgregadoViewHolder holder, int position) {
                holder.tvNombre.setText(ingAgregados.get(position).getNombre());
                holder.btnQuitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ingredientes.size() >= ingAgregados.get(position).getId()) {
                            ingredientes.add(ingAgregados.get(position).getId() - 1, ingAgregados.get(position));
                        }
                        else{
                            ingredientes.add(ingAgregados.get(position));
                        }

                        ingAgregados.remove(position);
                        notifyDataSetChanged();
                        adaptadorAgrega.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return ingAgregados.size();
            }
        };

        listaAgregado.setAdapter(adaptadorAgregado);
        //------------------------------------------------------------------------------------




        //-------------------------TOMA FOTO O IMAGEN DEL PLATO
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerFotoIntent();
            }
        });







        //------------AGREGA EL PLATO A LA BASE DE DATOS
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                agregaPlato();
            }
        });


    }


    public void agregaPlato(){
        String[] ingArray = new String[ingAgregados.size()];
        long nuevaId;

        for(int i = 0; i < ingAgregados.size(); i++){
            ingArray[i] = String.valueOf(ingAgregados.get(i).getId());
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        //----------------AGREGA EL PLATO
        ContentValues values = new ContentValues();
        values.put(TablaPlato.NOMBRE_COLUMNA_2,txtNombre.getText().toString());
        values.put(TablaPlato.NOMBRE_COLUMNA_5, fotoUri.toString());

        if (modifica == 0) {
            nuevaId = db.insert(TablaPlato.NOMBRE_TABLA,null,values);
        }else{
            String selection = TablaPlato.NOMBRE_COLUMNA_1 + " LIKE ?";
            String[] selectionArgs = { String.valueOf(modifica) };
            nuevaId = db.update(TablaPlato.NOMBRE_TABLA,values,selection,selectionArgs);
            db.delete(TablaPlatoIngredientePeso.NOMBRE_TABLA,TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " LIKE ?",selectionArgs);
        }

        //----------------AGREGA EL INGPESO

        SQLiteDatabase db2 = helper.getWritableDatabase();

        for(String x : ingArray){
            double p = 0.5;
            ContentValues values2 = new ContentValues();
            if (modifica == 0) values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2,String.valueOf(nuevaId));
            if (modifica != 0) values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2,String.valueOf(modifica));
            values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_3,x);
            if (modifica !=0){
                for ( IngPeso v : this.ing){

                    System.out.println("X: " + x + " ID: " + v.getIdIng() + " PESO: " + p);
                    if (x.equals(String.valueOf(v.getIdIng()))) p = v.getPeso();

                }
            }
            values2.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_4,String.valueOf(p));
            long IngNuevaId = db2.insert(TablaPlatoIngredientePeso.NOMBRE_TABLA,null,values2);

        }

        Intent i = new Intent(this , ListaPlatos.class);
        if (modifica == 0) i.putExtra("id",Integer.parseInt(String.valueOf(nuevaId)));
        if (modifica != 0) i.putExtra("id",modifica);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        finish();
    }

    //----------------------SI EL PLATO ES NUEVO
    public void actualizaListaAgrega(){
        SQLiteDatabase db = this.helper.getReadableDatabase();

        Cursor cursor = db.query(
                TablaIngrediente.NOMBRE_TABLA,null,null,null,null,null,null);


        //ingredientes = new ArrayList<Ingrediente>();
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            String formato = cursor.getString(3);
            String proveedor = cursor.getString(4);

            Ingrediente ing = new Ingrediente(id,nombre,formato,proveedor,precio);
            ingredientes.add(ing);
        }
        cursor.close();
        adaptadorAgrega = new AdaptadorListaAgrega(ingredientes,this,helper, this);
        listaAgrega.setAdapter(adaptadorAgrega);
    }


    //-----------------------SI SE AÑADE UN NUEVO INGREDIENTE
    public Ingrediente actualizaIngrediente(int idIng){
        Ingrediente ing;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        String selection = TablaIngrediente.NOMBRE_COLUMNA_1 + " LIKE ?";
        String[] selectionArgs = {String.valueOf(idIng)};

        Cursor cursor = db.query(TablaIngrediente.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String nombre = cursor.getString(1);
        Double precio = cursor.getDouble(2);
        String formato = cursor.getString(3);
        String proveedor = cursor.getString(4);

        ing = new Ingrediente(id,nombre,formato,proveedor,precio);
        cursor.close();
        return ing;
    }


    //----------------------SI YA EXISTE EL PLATO
    public void modificaListaAgrega(){
        SQLiteDatabase db = this.helper.getReadableDatabase();



        Cursor cursor = db.query(
                TablaIngrediente.NOMBRE_TABLA,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        ArrayList<Ingrediente> temp = new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            String formato = cursor.getString(3);
            String proveedor = cursor.getString(4);


            Ingrediente ing = new Ingrediente(id,nombre,formato,proveedor,precio);
            temp.add(ing);




            }
        for ( Ingrediente i : temp){
            int x = 0;
            for(IngPeso p : this.ing){
                if(p.getIdIng() == i.getId()){
                    ingAgregados.add(i);
                    x = 1;
                }
            }
            if(x==0) ingredientes.add(i);



        }
        cursor.close();
        adaptadorAgrega = new AdaptadorListaAgrega(ingredientes,this,helper, this);
        listaAgrega.setAdapter(adaptadorAgrega);
    }


    @Override
    public void onListItemClick(int clickedItem) {
        System.out.println("Click: " +clickedItem);
    }
}