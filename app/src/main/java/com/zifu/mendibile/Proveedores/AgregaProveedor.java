package com.zifu.mendibile.Proveedores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.Plato;
import com.zifu.mendibile.Modelos.Proveedor;
import com.zifu.mendibile.Modelos.ProveedorTlf;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaProveedor;
import com.zifu.mendibile.tablas.TablaProveedorTlf;

import java.util.ArrayList;

public class AgregaProveedor extends AppCompatActivity {

    final int AGREGAR_CONTACTO = 439;

    Toolbar tlb;
    RecyclerView listaTlf;
    RecyclerView.Adapter adaptador;
    LayoutManager layoutManager;
    ArrayList<ProveedorTlf> tlfs;
    EditText nombre,productos,cif,notas, nombretlf,telefonotlf;
    Button agregar;
    ImageButton agregartlf,contactostlf;
    Proveedor prov;
    int modifica;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agrega_prov,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if(item.getItemId() == R.id.itmAgregaNuevoProv){
            agregaProveedor();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case AGREGAR_CONTACTO :
                if (resultCode == Activity.RESULT_OK) {
                    Uri datosContacto = data.getData();
                    ContentResolver cr = getContentResolver();
                    Cursor cur = cr.query(datosContacto, null, null, null, null);
                    if (cur.getCount() > 0) {
                        if(cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String nombre = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            {
                                Cursor telefonos = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                                telefonos.moveToFirst();
                                    String tlf = telefonos.getString(telefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    nombretlf.setText(nombre);
                                    telefonotlf.setText(tlf);
                                telefonos.close();
                            }

                        }
                    }
                    cur.close();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrega_proveedor);

        tlb = (Toolbar) findViewById(R.id.tlbAgregaProv);
        listaTlf = (RecyclerView) findViewById(R.id.listaProvTlf);
        nombre = (EditText) findViewById(R.id.txtProvNombre);
        productos = (EditText) findViewById(R.id.txtProvProductos);
        cif = (EditText) findViewById(R.id.txtProvCif);
        notas = (EditText) findViewById(R.id.txtProvNotas);
        agregar = (Button) findViewById(R.id.btnProvAgregar);
        nombretlf = (EditText) findViewById(R.id.txtProvContactoNombre);
        telefonotlf = (EditText) findViewById(R.id.txtProvContactoTlf);
        agregartlf = (ImageButton) findViewById(R.id.ibProvAgregaTlf);
        contactostlf = (ImageButton) findViewById(R.id.ibProvAgenda);

        Bundle datos = getIntent().getExtras();
        if(datos != null){
            modifica = datos.getInt("modifica");
            prov = (Proveedor) datos.getSerializable("prov");
        }

        if (modifica != 0){
            tlfs = prov.getTelefonos();
            nombre.setText(prov.getNombre());
            productos.setText(prov.getProducto());
            cif.setText(prov.getCif());
            notas.setText(prov.getNotas());
            agregar.setText("Modificar proveedor");

        }else{
            tlfs = new ArrayList<>();
//            tlfs.add(new ProveedorTlf(1,1,"",""));
        }

        tlb.setTitle("Agregar proveedor");
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        listaTlf.setLayoutManager(layoutManager);




        class TlfViewHolder extends RecyclerView.ViewHolder{
            TextView contacto;
            ImageView borrarTlf;

            public TlfViewHolder(@NonNull View itemView) {
                super(itemView);
                contacto = (TextView) itemView.findViewById(R.id.tvDetalleCompraIngProv);
                borrarTlf = (ImageView) itemView.findViewById(R.id.ivDetalleCompraIngElimina);

                borrarTlf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tlfs.remove(getAdapterPosition());
                        adaptador.notifyDataSetChanged();
                    }
                });
            }
        }
        adaptador = new RecyclerView.Adapter<TlfViewHolder>() {

            @NonNull
            @Override
            public TlfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_compra_ing_row, parent, false);
                return new TlfViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull TlfViewHolder holder, int position) {

                holder.contacto.setText(tlfs.get(position).getNombre() + " - " + tlfs.get(position).getTelefono());

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
                return tlfs.size();
            }
        };
        adaptador.setHasStableIds(true);
        listaTlf.setAdapter(adaptador);

        agregartlf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(telefonotlf.getText().toString().equals("")){
                    Toast tlfVacio =
                            Toast.makeText(getApplicationContext(),"El campo de número de teléfono no puede estar vacío", Toast.LENGTH_SHORT);
                    tlfVacio.show();
                    return;
                }
                tlfs.add(new ProveedorTlf(nombretlf.getText().toString(),telefonotlf.getText().toString()));
                adaptador.notifyDataSetChanged();
                nombretlf.setText("");
                telefonotlf.setText("");
                nombretlf.requestFocus();
            }
        });

        contactostlf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent,AGREGAR_CONTACTO);
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                agregaProveedor();
            }
        });
    }

    public void agregaProveedor(){
        if(nombre.getText().toString().equals("")){
            Toast nombreVacio =
                    Toast.makeText(getApplicationContext(),"El campo de nombre no puede estar vacío", Toast.LENGTH_SHORT);
            nombreVacio.show();
            return;
        }
        if(comprobarProv(nombre.getText().toString().toLowerCase())){
            Toast repetido = Toast.makeText(this, "Ya existe un proveedor con el mismo nombre.", Toast.LENGTH_SHORT);
            repetido.show();
            return;
        }
        if(telefonotlf.getText() != null){
            if(!telefonotlf.getText().toString().equals("")){
                tlfs.add(new ProveedorTlf(nombretlf.getText().toString(),telefonotlf.getText().toString()));
            }
        }
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(TablaProveedor.NOMBRE_COLUMNA_2,nombre.getText().toString());
        v.put(TablaProveedor.NOMBRE_COLUMNA_3,productos.getText().toString());
        v.put(TablaProveedor.NOMBRE_COLUMNA_4,cif.getText().toString());
        v.put(TablaProveedor.NOMBRE_COLUMNA_5,notas.getText().toString());

        if(modifica!=0){
            String selection = TablaProveedor.NOMBRE_COLUMNA_1 + " LIKE ?";
            String selectionArgs[] = {String.valueOf(modifica)};
            db.update(TablaProveedor.NOMBRE_TABLA,v,selection,selectionArgs);
            db.delete(TablaProveedorTlf.NOMBRE_TABLA,TablaProveedorTlf.NOMBRE_COLUMNA_2 + " LIKE ? ",new String[] {String.valueOf(modifica)});
            agregaTlf(modifica);
        }else {
            long nuevaId = db.insert(TablaProveedor.NOMBRE_TABLA, null, v);
            agregaTlf(Integer.parseInt(String.valueOf(nuevaId)));
        }
    }
    public void agregaTlf(int idProv){
        SQLiteDatabase db = MainActivity.helper.getWritableDatabase();

        for (ProveedorTlf t : tlfs){
            ContentValues v = new ContentValues();
            v.put(TablaProveedorTlf.NOMBRE_COLUMNA_2,idProv);
            v.put(TablaProveedorTlf.NOMBRE_COLUMNA_3,t.getNombre());
            v.put(TablaProveedorTlf.NOMBRE_COLUMNA_4,t.getTelefono());
            db.insert(TablaProveedorTlf.NOMBRE_TABLA,null,v);
        }

        Intent i = new Intent(this,ListaProveedores.class);
        i.putExtra("id" , idProv);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public boolean comprobarProv(String nuevo){
        ArrayList<Proveedor> p = new ArrayList<>();
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();
        Cursor c = db.query(TablaProveedor.NOMBRE_TABLA, null, null, null, null, null, null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String nombre = c.getString(1).toLowerCase();
            p.add(new Proveedor(id,nombre));
        }
        c.close();

        for(Proveedor prov : p){
            if(prov.getNombre().equals(nuevo)){
                return true;
            }
        }
        return false;
    }
}