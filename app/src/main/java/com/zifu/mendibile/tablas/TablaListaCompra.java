package com.zifu.mendibile.tablas;

public class TablaListaCompra {
    private TablaListaCompra(){}


    public static final String NOMBRE_TABLA = "listaCompra";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "fecha";
    public static final String NOMBRE_COLUMNA_3 = "notas";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaListaCompra.NOMBRE_TABLA + " (" +
                    TablaListaCompra.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaListaCompra.NOMBRE_COLUMNA_2 + " TEXT," +
                    TablaListaCompra.NOMBRE_COLUMNA_3 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaListaCompra.NOMBRE_TABLA;


}
