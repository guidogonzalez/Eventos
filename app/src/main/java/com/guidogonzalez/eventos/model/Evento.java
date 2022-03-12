package com.guidogonzalez.eventos.model;

import java.io.Serializable;
import java.util.Date;

public class Evento implements Serializable {

    public String _id;
    public String nombre;
    public String descripcion;
    public Date fechaEvento;
    public String fotos;
    public Integer precio;
    public String idCreador;
    public String fotoCreador;
    public String nombreCreador;

    public Evento(String _id, String nombre, String descripcion, Date fechaEvento, String fotos, Integer precio, String idCreador, String fotoCreador, String nombreCreador) {
        this._id = _id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.fotos = fotos;
        this.precio = precio;
        this.idCreador = idCreador;
        this.fotoCreador = fotoCreador;
        this.nombreCreador = nombreCreador;
    }

    public Evento(String nombre, String descripcion, Date fechaEvento, String fotos, Integer precio, String idCreador, String fotoCreador, String nombreCreador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.fotos = fotos;
        this.precio = precio;
        this.idCreador = idCreador;
        this.fotoCreador = fotoCreador;
        this.nombreCreador = nombreCreador;
    }
}
