package com.zifu.mendibile.tablas;

public class TablaPlato {
    private TablaPlato(){}

    public static final String NOMBRE_TABLA = "platos";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "nombre";
    public static final String NOMBRE_COLUMNA_3 = "coste";
    public static final String NOMBRE_COLUMNA_4 = "ingredientes";
    public static final String NOMBRE_COLUMNA_5 = "foto";
    public static final String NOMBRE_COLUMNA_6 = "elaboracion";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaPlato.NOMBRE_TABLA + " (" +
                    TablaPlato.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaPlato.NOMBRE_COLUMNA_2 + " TEXT," +
                    TablaPlato.NOMBRE_COLUMNA_3 + " REAL," +
                    TablaPlato.NOMBRE_COLUMNA_4 + " TEXT," +
                    TablaPlato.NOMBRE_COLUMNA_5 + " TEXT," +
                    TablaPlato.NOMBRE_COLUMNA_6 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaPlato.NOMBRE_TABLA;
}
