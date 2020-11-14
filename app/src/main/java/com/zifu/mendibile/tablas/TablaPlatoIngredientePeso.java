package com.zifu.mendibile.tablas;

public class TablaPlatoIngredientePeso {

    public static final String NOMBRE_TABLA = "PlatoIngredientePeso";
    public static final String NOMBRE_COLUMNA_1 = "id";
    public static final String NOMBRE_COLUMNA_2 = "pltId";
    public static final String NOMBRE_COLUMNA_3 = "ingId";
    public static final String NOMBRE_COLUMNA_4 = "IngPeso";


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablaPlatoIngredientePeso.NOMBRE_TABLA + " (" +
                    TablaPlatoIngredientePeso.NOMBRE_COLUMNA_1 + " INTEGER PRIMARY KEY," +
                    TablaPlatoIngredientePeso.NOMBRE_COLUMNA_2 + " INTEGER," +
                    TablaPlatoIngredientePeso.NOMBRE_COLUMNA_3 + " INTEGER," +
                    TablaPlatoIngredientePeso.NOMBRE_COLUMNA_4 + " REAL)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablaPlatoIngredientePeso.NOMBRE_TABLA;

}
