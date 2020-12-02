package com.zifu.mendibile.Modelos;

import java.io.Serializable;

public class CompraIngrediente implements Serializable {
    int id, idLista;
    String nombre,formmato;
    String cantidad;

    public CompraIngrediente() {
    }

    public CompraIngrediente(int id, int idLista, String nombre, String formmato, String cantidad) {
        this.id = id;
        this.idLista = idLista;
        this.nombre = nombre;
        this.formmato = formmato;
        this.cantidad = cantidad;
    }

    public CompraIngrediente(int idLista, String nombre, String formmato, String cantidad) {
        this.idLista = idLista;
        this.nombre = nombre;
        this.formmato = formmato;
        this.cantidad = cantidad;
    }

    public CompraIngrediente(String nombre, String formmato, String cantidad) {
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

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
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

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
