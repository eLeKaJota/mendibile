package com.zifu.mendibile.Modelos;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import com.zifu.mendibile.BBDDHelper;
import com.zifu.mendibile.MainActivity;
import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;

import java.io.Serializable;

public class IngPeso implements Serializable {

    private int idIng;
    private double peso;
    private double precio;

    public IngPeso(int idIng, double peso) {
        this.idIng = idIng;
        this.peso = peso;
        this.precio = calculaPrecio(idIng);
    }

    public double calculaPrecio(int idIng){
        SQLiteDatabase db = MainActivity.helper.getReadableDatabase();

        String selection = TablaIngrediente.NOMBRE_COLUMNA_1 + " = ?";
        String[] selectionArgs = {String.valueOf(idIng)};

        Cursor cursor = db.query(TablaIngrediente.NOMBRE_TABLA, null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        double precioIng = cursor.getDouble(2);

        return peso * precioIng;

    }


    public int getIdIng() {
        return idIng;
    }

    public void setIdIng(int idIng) {
        this.idIng = idIng;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
