package com.guidogonzalez.eventos.model;

import java.util.Date;

public class Evento {

    public String _id;
    public String nombre;
    public String descripcion;
    public Date fechaEvento;
    public String fotos;
    public Integer precio;
    public String idCreador;

    public Evento(String _id, String nombre, String descripcion, Date fechaEvento, String fotos, Integer precio, String idCreador) {
        this._id = _id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.fotos = fotos;
        this.precio = precio;
        this.idCreador = idCreador;
    }

    public Evento(String nombre, String descripcion, Date fechaEvento, String fotos, Integer precio, String idCreador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.fotos = fotos;
        this.precio = precio;
        this.idCreador = idCreador;
    }
}
