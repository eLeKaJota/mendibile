package com.zifu.mendibile.DetallePlatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.Modelos.IngPeso;
import com.zifu.mendibile.Modelos.Ingrediente;
import com.zifu.mendibile.R;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaDetalleIng extends RecyclerView.Adapter<AdaptadorListaDetalleIng.ingViewHolder> {

    private List<Ingrediente> ing;
    final BBDDHelper helper;
    DetallePlatos detalle;
    private Handler handlerMas;
    private Handler handlerMenos;
    private double costeTotal = 0;
    private ArrayList<Double> sumaCostes = new ArrayList<Double>();
    private boolean gr;
    String moneda,monedaSimbolo;

    @NonNull
    @Override
    public AdaptadorListaDetalleIng.ingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context mContext = parent.getContext();
        int layoutIdListIng = R.layout.lista_detalles_ingrediente_row;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachToParentRapido = false;
        View view = inflater.inflate(layoutIdListIng,parent,attachToParentRapido);

        ingViewHolder viewHolder = new ingViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaDetalleIng.ingViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return ing.size();
    }



    public AdaptadorListaDetalleIng(ArrayList<Ingrediente> ing, BBDDHelper helper, DetallePlatos detalle){
        this.ing = ing;
        this.helper = helper;
        this.detalle = detalle;
        SharedPreferences ajustes = MainActivity.context.getSharedPreferences("com.zifu.mendibil", Context.MODE_PRIVATE);
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
    }

    public class ingViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNombre, tvCoste, tvRacion, tvCosteTotal, tvPeso, txtMoneda;
        public ImageButton btnMas,btnMenos;
        public EditText etPeso;
        public String formato;

        public ingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvAgregadoIngNombre);
            tvCoste = itemView.findViewById(R.id.tvDetalleListaIngCoste);
            tvRacion = itemView.findViewById(R.id.tvAgregadoIngRacion);
            btnMas = itemView.findViewById(R.id.btnDetalleListaIngPesoMas);
            btnMenos = itemView.findViewById(R.id.btnDetalleListaIngPesoMenos);
            etPeso = itemView.findViewById(R.id.etAgregadoIngPeso);
            tvPeso = itemView.findViewById(R.id.tvPesoIng);
            txtMoneda = itemView.findViewById(R.id.txtDetalleIngMoneda);
            tvCosteTotal = detalle.findViewById(R.id.tvDetallePltCoste);
            costeTotal = detalle.plato.getCoste();


        }

        void sumaCoste(){


            tvCosteTotal.setText(String.valueOf((double)Math.round(detalle.plato.getCoste()*100)/100));
        }

        //Muestra correctamente el peso si baja de 1
        void imprimePeso(double peso){
            switch (formato){
                case "Kg.":
                    if(peso < 1){
                        gr = true;
                        tvRacion.setText("gr./Ración");
                        double p = peso*1000;
                        tvPeso.setText(String.valueOf(p));

                    }else{
                        gr = false;
                        tvRacion.setText("Kg./Ración");
                        tvPeso.setText(String.valueOf(peso));
                    }
                    break;
                case "Litro":
                    if(peso < 1){
                        gr = true;
                        tvRacion.setText("ml./Ración");
                        double p = peso*1000;
                        tvPeso.setText(String.valueOf(p));

                    }else{
                        gr = false;
                        tvRacion.setText("Litro/Ración");
                        tvPeso.setText(String.valueOf(peso));
                    }
                    break;
                default:
                    tvPeso.setText(String.valueOf(peso));
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(int listaIndex){
            Ingrediente i = ing.get(listaIndex);
            tvNombre.setText(i.getNombre());
            txtMoneda.setText(monedaSimbolo);
            formato = i.getFormato();
            double costeIng = i.getPrecio();
            tvRacion.setText(i.getFormato() + "/Ración");
            for (IngPeso e : detalle.plato.getIngPeso()){
                if (e.getIdIng() == i.getId()){
                    etPeso.setText(String.valueOf(e.getPeso()));
                }
            }

            double coste = i.getPrecio() * Double.parseDouble(etPeso.getText().toString());
            tvCoste.setText(String.valueOf((double)Math.round(coste*100)/100));

            imprimePeso(Double.parseDouble(etPeso.getText().toString()));


            //sumaCoste(coste);
            etPeso.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (etPeso.getText().toString().equals("")){
                        etPeso.setText("0.0");
                        //return;
                    }
                    double peso = Double.parseDouble(etPeso.getText().toString());
                    if (peso < 0){
                        etPeso.setText("0.0");
                        //tvPeso.setText("0.0");
                    }
//                    Log.e("CharSequence",s.toString());
//                    Log.e("start",String.valueOf(start));
//                    Log.e("before",String.valueOf(before));
//                    Log.e("count",String.valueOf(count));



                    double coste = i.getPrecio();
                    double nuevoCoste = Double.parseDouble(etPeso.getText().toString()) * coste;


                    tvCoste.setText(String.valueOf((double)Math.round(nuevoCoste * 100)/100));


                    //---------------------ACTUALIZA BBDD--------------
                    SQLiteDatabase db = MainActivity.helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TablaPlatoIngredientePeso.NOMBRE_COLUMNA_4,Double.parseDouble(etPeso.getText().toString()));

                    String selection = TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " LIKE ? AND " + TablaPlatoIngredientePeso.NOMBRE_COLUMNA_3 + " LIKE ?";
                    String[] selectionArgs = { String.valueOf(detalle.plato.getId()) , String.valueOf(i.getId()) };

                    db.update(TablaPlatoIngredientePeso.NOMBRE_TABLA,values,selection,selectionArgs);

                    detalle.plato.setIngPeso(ingredientesPeso(detalle.plato.getId()));

                    //-------------------------------------------------

                    sumaCoste();
                    imprimePeso(Double.parseDouble(etPeso.getText().toString()));
//                    if(peso < 1){
//                        gr = true;
//                        tvRacion.setText("gr/Ración");
//                        double p = Double.parseDouble(s.toString())*1000;
//                        tvPeso.setText(String.valueOf(p));
//
//                    }else{
//                        gr = false;
//                        tvRacion.setText(i.getFormato() + " /Ración");
//                        double p = Double.parseDouble(s.toString());
//                        tvPeso.setText(String.valueOf(p));
//                    }
//                    Log.e("gr",String.valueOf(gr));


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            btnMas.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (handlerMas != null) return true;
                            handlerMas = new Handler();
                            handlerMas.postDelayed(accionMas, 400);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            if (handlerMas == null) return true;
                            handlerMas.removeCallbacks(accionMas);
                            handlerMas = null;
                            break;
                    }
                    return false;
                }
                Runnable accionMas = new Runnable() {
                    @Override public void run() {
                        if(formato.equals("Kg.") || formato.equals("Litro") || formato.equals("gr.") || formato.equals("ml.")){
                            double valor = Double.parseDouble(etPeso.getText().toString());
                            valor += 0.10;
                            etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                        }else{
                            double valor = Double.parseDouble(etPeso.getText().toString());
                            valor += 0.5;
                            etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                        }
                        handlerMas.postDelayed(this, 100);
                    }
                };
            });
            btnMenos.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (handlerMenos != null) return true;
                            handlerMenos = new Handler();
                            handlerMenos.postDelayed(accionMenos, 400);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            if (handlerMenos == null) return true;
                            handlerMenos.removeCallbacks(accionMenos);
                            handlerMenos = null;
                            break;

                    }
                    return false;
                }
                Runnable accionMenos = new Runnable() {
                    @Override public void run() {
                        if(formato.equals("Kg.") || formato.equals("Litro") || formato.equals("gr.") || formato.equals("ml.")){
                            double valor = Double.parseDouble(etPeso.getText().toString());
                            valor -= 0.10;
                            etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                        }else{
                            double valor = Double.parseDouble(etPeso.getText().toString());
                            valor -= 0.5;
                            etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                        }
                        handlerMenos.postDelayed(this, 100);
                    }
                };
            });



            btnMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(formato.equals("Kg.") || formato.equals("Litro") || formato.equals("gr.") || formato.equals("ml.")){
                        double valor = Double.parseDouble(etPeso.getText().toString());
                        valor += 0.01;
                        etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                    }else{
                        double valor = Double.parseDouble(etPeso.getText().toString());
                        valor += 0.25;
                        etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                    }
                }
            });
            btnMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(formato.equals("Kg.") || formato.equals("Litro") || formato.equals("gr.") || formato.equals("ml.")){
                        double valor = Double.parseDouble(etPeso.getText().toString());
                        valor -= 0.01;
                        etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                    }else{
                        double valor = Double.parseDouble(etPeso.getText().toString());
                        valor -= 0.25;
                        etPeso.setText(String.valueOf((double)Math.round(valor * 100)/100));
                    }
                }
            });

        }

        public ArrayList<IngPeso> ingredientesPeso(int id){
            SQLiteDatabase db2 = MainActivity.helper.getReadableDatabase();
            ArrayList<IngPeso> ingPesos = new ArrayList<IngPeso>();



            String selection = TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            Cursor cursor2 = db2.query(TablaPlatoIngredientePeso.NOMBRE_TABLA, null, selection, selectionArgs, null, null, null);

            //cursor2.moveToFirst();
            while (cursor2.moveToNext()) {
                int idIng = cursor2.getInt(2);
                double peso = cursor2.getDouble(3);

                ingPesos.add(new IngPeso(idIng,peso));
            }

            cursor2.close();

            return ingPesos;
        }


    }





}
