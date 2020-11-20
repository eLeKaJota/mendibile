package com.zifu.mendibile.Modelos;

import java.io.Serializable;

public class ProveedorTlf implements Serializable {
    private int id, idPlato;
    private String nombre,telefono;

    public ProveedorTlf(int id,int idPlato, String nombre, String telefono) {
        this.id = id;
        this.idPlato = idPlato;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int iddPlato) {
        this.idPlato = iddPlato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
