package com.zifu.mendibile.tablas;

public class TablaProveedor {
    private TablaProveedor(){}


    public static final String NOMBRE_TABLA = "proveedor";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "nombre";
    public static final String NOMBRE_COLUMNA_3 = "producto";
    public static final String NOMBRE_COLUMNA_4 = "cif";
    public static final String NOMBRE_COLUMNA_5 = "notas";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaProveedor.NOMBRE_TABLA + " (" +
                    TablaProveedor.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaProveedor.NOMBRE_COLUMNA_2 + " TEXT," +
                    TablaProveedor.NOMBRE_COLUMNA_3 + " TEXT," +
                    TablaProveedor.NOMBRE_COLUMNA_4 + " TEXT," +
                    TablaProveedor.NOMBRE_COLUMNA_5 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaProveedor.NOMBRE_TABLA;


}
