package com.zifu.mendibile.tablas;

public class TablaProveedorTlf {
    private TablaProveedorTlf(){}


    public static final String NOMBRE_TABLA = "proveedorTlf";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "idPlato";
    public static final String NOMBRE_COLUMNA_3 = "nombre";
    public static final String NOMBRE_COLUMNA_4 = "telefono";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaProveedorTlf.NOMBRE_TABLA + " (" +
                    TablaProveedorTlf.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaProveedorTlf.NOMBRE_COLUMNA_2 + " INTEGER," +
                    TablaProveedorTlf.NOMBRE_COLUMNA_3 + " TEXT," +
                    TablaProveedorTlf.NOMBRE_COLUMNA_4 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaProveedorTlf.NOMBRE_TABLA;


}
