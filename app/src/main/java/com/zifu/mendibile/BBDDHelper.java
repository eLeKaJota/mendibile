package com.zifu.mendibile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zifu.mendibile.tablas.TablaIngrediente;
import com.zifu.mendibile.tablas.TablaListaCompra;
import com.zifu.mendibile.tablas.TablaListaCompraIng;
import com.zifu.mendibile.tablas.TablaPlato;
import com.zifu.mendibile.tablas.TablaPlatoIngredientePeso;
import com.zifu.mendibile.tablas.TablaProveedor;
import com.zifu.mendibile.tablas.TablaProveedorTlf;

public class BBDDHelper extends SQLiteOpenHelper {
    public static final int borronYCuentaNueva = 16;
    public static final int columnaProvCompra = 17;
    public static final int versionDB = columnaProvCompra;
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

            db.execSQL(TablaListaCompra.SQL_CREATE_ENTRIES);

            db.execSQL(TablaListaCompraIng.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            int version = oldVersion;

            switch (version){
                case 15:
                    db.execSQL(TablaPlato.SQL_DELETE_ENTRIES);

                    db.execSQL(TablaIngrediente.SQL_DELETE_ENTRIES);

                    db.execSQL(TablaPlatoIngredientePeso.SQL_DELETE_ENTRIES);

                    db.execSQL(TablaProveedor.SQL_DELETE_ENTRIES);

                    db.execSQL(TablaProveedorTlf.SQL_DELETE_ENTRIES);

                    db.execSQL(TablaListaCompra.SQL_DELETE_ENTRIES);

                    db.execSQL(TablaListaCompraIng.SQL_DELETE_ENTRIES);
                    version = borronYCuentaNueva;

                case borronYCuentaNueva:
                    db.execSQL("ALTER TABLE "+ TablaListaCompraIng.NOMBRE_TABLA +" ADD COLUMN "+ TablaListaCompraIng.NOMBRE_COLUMNA_6 +" TEXT");
                    version = columnaProvCompra;

            }




        //onCreate(db);
    }
}
