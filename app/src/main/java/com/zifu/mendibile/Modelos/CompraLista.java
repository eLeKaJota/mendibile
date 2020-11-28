package com.zifu.mendibile.Modelos;

import java.io.Serializable;

public class CompraLista implements Serializable {
    private int id;
    private String fecha,notas;

    public CompraLista(int id, String fecha, String notas) {
        this.id = id;
        this.fecha = fecha;
        this.notas = notas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
