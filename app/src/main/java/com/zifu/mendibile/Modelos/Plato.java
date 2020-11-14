package com.zifu.mendibile.Modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Plato implements Serializable {

    private int id;
    private Double coste;
    private String nombre;
    private String foto;
    private String elaboracion;
    private String ingredientes;

    public ArrayList<IngPeso> getIngPeso() {
        return ingPeso;
    }

    public void setIngPeso(ArrayList<IngPeso> ingPeso) {
        this.ingPeso = ingPeso;
    }

    private ArrayList<IngPeso> ingPeso;


    public Plato(int id, String nombre, Double coste, String foto, String elaboracion, ArrayList<IngPeso> ingPeso) {
        this.id = id;
        this.coste = coste;
        this.nombre = nombre;
        this.foto = foto;
        this.elaboracion = elaboracion;
        this.ingPeso = ingPeso;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//-----------CALCULA EL COSTE TOTAL CON LA SUMA DE LOS INGREDIENTES
    public Double getCoste() {

        coste = 0.0;
        for(int i = 0; i< ingPeso.size(); i++){
            coste += ingPeso.get(i).getPrecio();
        }

        return coste;
    }

    public void setCoste(Double coste) {
        this.coste = coste;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getElaboracion() {
        return elaboracion;
    }

    public void setElaboracion(String elaboracion) {
        this.elaboracion = elaboracion;
    }
}

