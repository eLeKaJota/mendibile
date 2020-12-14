package com.zifu.mendibile.tablas;

public class TablaTemp {
    private TablaTemp(){}

    public static final String NOMBRE_TABLA = "tempPlatos";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "nombre";
    public static final String NOMBRE_COLUMNA_3 = "coste";
    public static final String NOMBRE_COLUMNA_4 = "ingredientes";
    public static final String NOMBRE_COLUMNA_5 = "foto";
    public static final String NOMBRE_COLUMNA_6 = "elaboracion";
    public static final String NOMBRE_COLUMNA_7 = "tipoElaboracion";
    public static final String NOMBRE_COLUMNA_8 = "numeroElaboracion";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaTemp.NOMBRE_TABLA + " (" +
                    TablaTemp.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaTemp.NOMBRE_COLUMNA_2 + " TEXT," +
                    TablaTemp.NOMBRE_COLUMNA_3 + " REAL," +
                    TablaTemp.NOMBRE_COLUMNA_4 + " TEXT," +
                    TablaTemp.NOMBRE_COLUMNA_5 + " TEXT," +
                    TablaTemp.NOMBRE_COLUMNA_6 + " TEXT," +
                    TablaTemp.NOMBRE_COLUMNA_7 + " TEXT," +
                    TablaTemp.NOMBRE_COLUMNA_8 + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaTemp.NOMBRE_TABLA;
}
