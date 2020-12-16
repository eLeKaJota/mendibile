package com.zifu.mendibile.Modelos;

import java.io.Serializable;

public class Ingrediente implements Serializable {
    private int id;
    private String nombre,formato;
    private String proveedor;
    private double precio;

    public Ingrediente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Ingrediente(int id, String nombre, String formato, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.formato = formato;
        this.precio = precio;
    }

    public Ingrediente(int id, String nombre, String formato, String proveedor, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.formato = formato;
        this.proveedor = proveedor;
        this.precio = precio;
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
