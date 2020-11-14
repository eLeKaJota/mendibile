package com.zifu.mendibile.tablas;

import android.provider.BaseColumns;

import com.google.android.material.tabs.TabLayout;

public class TablaIngrediente {
    private TablaIngrediente(){}


    public static final String NOMBRE_TABLA = "ingrediente";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "nombre";
    public static final String NOMBRE_COLUMNA_3 = "precio";
    public static final String NOMBRE_COLUMNA_4 = "formato";
    public static final String NOMBRE_COLUMNA_5 = "proveedor";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaIngrediente.NOMBRE_TABLA + " (" +
                    TablaIngrediente.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaIngrediente.NOMBRE_COLUMNA_2 + " TEXT," +
                    TablaIngrediente.NOMBRE_COLUMNA_3 + " REAL," +
                    TablaIngrediente.NOMBRE_COLUMNA_4 + " TEXT," +
                    TablaIngrediente.NOMBRE_COLUMNA_5 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaIngrediente.NOMBRE_TABLA;


}
