package com.zifu.mendibile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zifu.mendibile.tablas.TablaListaCompra;
import com.zifu.mendibile.tablas.TablaListaCompraIng;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaTemp;

public class BBDDHelper extends SQLiteOpenHelper {
    public static final int actualizacion12 = 12;
    public static final int baseDatosPlato = 13;
    public static final int versionDB = baseDatosPlato;
    public static String nombreDB = "Mendibile.db";


    public BBDDHelper(Context context){

        super(context, nombreDB, null, versionDB);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


//            db.execSQL(TablaPlato.SQL_CREATE_ENTRIES);
//
//            db.execSQL(TablaIngrediente.SQL_CREATE_ENTRIES);
//
//            db.execSQL(TablaPlatoIngredientePeso.SQL_CREATE_ENTRIES);
//
//            db.execSQL(TablaProveedor.SQL_CREATE_ENTRIES);
//
//            db.execSQL(TablaProveedorTlf.SQL_CREATE_ENTRIES);
//
//            db.execSQL(TablaListaCompra.SQL_CREATE_ENTRIES);
//
//            db.execSQL(TablaListaCompraIng.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            int version = oldVersion;

            switch (version){
                case 11:
                    db.execSQL("ALTER TABLE " + TablaPlato.NOMBRE_TABLA + " ADD COLUMN " + TablaPlato.NOMBRE_COLUMNA_7 + " REAL");
                    db.execSQL("ALTER TABLE " + TablaPlato.NOMBRE_TABLA + " ADD COLUMN " + TablaPlato.NOMBRE_COLUMNA_8 + " REAL");
                    version = actualizacion12;
                case actualizacion12:
                    db.execSQL(TablaTemp.SQL_CREATE_ENTRIES);
                    db.execSQL("INSERT INTO " + TablaTemp.NOMBRE_TABLA + " SELECT * FROM " + TablaPlato.NOMBRE_TABLA);
                    db.execSQL(TablaPlato.SQL_DELETE_ENTRIES);
                    db.execSQL(TablaPlato.SQL_CREATE_ENTRIES);
                    db.execSQL("INSERT INTO " + TablaPlato.NOMBRE_TABLA + " SELECT " + TablaTemp.NOMBRE_COLUMNA_1 + "," +
                                                                                        TablaTemp.NOMBRE_COLUMNA_2 + "," +
                                                                                        TablaTemp.NOMBRE_COLUMNA_5 + "," +
                                                                                        TablaTemp.NOMBRE_COLUMNA_6 + "," +
                                                                                        TablaTemp.NOMBRE_COLUMNA_7 + "," +
                                                                                        TablaTemp.NOMBRE_COLUMNA_8 + " FROM " + TablaTemp.NOMBRE_TABLA);
                    db.execSQL(TablaTemp.SQL_DELETE_ENTRIES);
                    version = baseDatosPlato;
            }

//            db.execSQL(TablaPlato.SQL_DELETE_ENTRIES);
//
//            db.execSQL(TablaIngrediente.SQL_DELETE_ENTRIES);
//
//            db.execSQL(TablaPlatoIngredientePeso.SQL_DELETE_ENTRIES);
//
//            db.execSQL(TablaProveedor.SQL_DELETE_ENTRIES);
//
//            db.execSQL(TablaProveedorTlf.SQL_DELETE_ENTRIES);
//
//            db.execSQL(TablaListaCompra.SQL_DELETE_ENTRIES);
//
//            db.execSQL(TablaListaCompraIng.SQL_DELETE_ENTRIES);


        onCreate(db);
    }
}
