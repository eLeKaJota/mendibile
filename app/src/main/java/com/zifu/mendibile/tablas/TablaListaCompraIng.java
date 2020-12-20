package com.zifu.mendibile.tablas;

public class TablaListaCompraIng {
    private TablaListaCompraIng(){}


    public static final String NOMBRE_TABLA = "listaCompraIng";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "idLista";
    public static final String NOMBRE_COLUMNA_3 = "ingrediente";
    public static final String NOMBRE_COLUMNA_4 = "formato";
    public static final String NOMBRE_COLUMNA_5 = "cantidad";
    public static final String NOMBRE_COLUMNA_6 = "proveedor";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaListaCompraIng.NOMBRE_TABLA + " (" +
                    TablaListaCompraIng.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaListaCompraIng.NOMBRE_COLUMNA_2 + " INTEGER," +
                    TablaListaCompraIng.NOMBRE_COLUMNA_3 + " TEXT," +
                    TablaListaCompraIng.NOMBRE_COLUMNA_4 + " TEXT," +
                    TablaListaCompraIng.NOMBRE_COLUMNA_5 + " TEXT," +
                    TablaListaCompraIng.NOMBRE_COLUMNA_6 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaListaCompraIng.NOMBRE_TABLA;


}
