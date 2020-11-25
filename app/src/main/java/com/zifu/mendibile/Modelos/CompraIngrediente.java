package com.zifu.mendibile.Modelos;

import java.io.Serializable;

public class CompraIngrediente implements Serializable {
    int id;
    String nombre,formmato;
    Double cantidad;

    public CompraIngrediente() {
    }

    public CompraIngrediente(int id, String nombre, String formmato, Double cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.formmato = formmato;
        this.cantidad = cantidad;
    }

    public CompraIngrediente(String nombre, String formmato, Double cantidad) {
        this.nombre = nombre;
        this.formmato = formmato;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFormmato() {
        return formmato;
    }

    public void setFormmato(String formmato) {
        this.formmato = formmato;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
}
