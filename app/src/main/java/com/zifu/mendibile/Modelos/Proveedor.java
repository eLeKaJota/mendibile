package com.zifu.mendibile.Modelos;

import com.zifu.mendibile.Modelos.Ingrediente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proveedor implements Serializable {
    private int id;
    private String nombre,producto,cif,notas;
    private ArrayList<ProveedorTlf> telefonos;
    private ArrayList<Ingrediente> ingredientes;

    public Proveedor(int id, String nombre, String producto, ArrayList<ProveedorTlf> telefonos, String notas, ArrayList<Ingrediente> ingredientes) {
        this.id = id;
        this.nombre = nombre;
        this.producto = producto;
        this.telefonos = telefonos;
        this.notas = notas;
        this.ingredientes = ingredientes;
    }

    public Proveedor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public ArrayList<ProveedorTlf> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(ArrayList<ProveedorTlf> telefonos) {
        this.telefonos = telefonos;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
}
