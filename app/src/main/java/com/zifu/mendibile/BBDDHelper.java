package com.zifu.mendibile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;
import com.zifu.mendibile.tablas.TablaProveedor;
import com.zifu.mendibile.tablas.TablaProveedorTlf;

import java.io.Serializable;

public class BBDDHelper extends SQLiteOpenHelper {
    public static final int versionDB = 6;
    public static String nombreDB = "Mendibile.db";


    public BBDDHelper(Context context){

        super(context, nombreDB, null, versionDB);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


            db.execSQL(TablaPlato.SQL_CREATE_ENTRIES);

            db.execSQL(TablaIngrediente.SQL_CREATE_ENTRIES);

            db.execSQL(TablaPlatoIngredientePeso.SQL_CREATE_ENTRIES);

            db.execSQL(TablaProveedor.SQL_CREATE_ENTRIES);

            db.execSQL(TablaProveedorTlf.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(TablaPlato.SQL_DELETE_ENTRIES);

            db.execSQL(TablaIngrediente.SQL_DELETE_ENTRIES);

            db.execSQL(TablaPlatoIngredientePeso.SQL_DELETE_ENTRIES);

            db.execSQL(TablaProveedor.SQL_DELETE_ENTRIES);

            db.execSQL(TablaProveedorTlf.SQL_DELETE_ENTRIES);


        onCreate(db);
    }
}
