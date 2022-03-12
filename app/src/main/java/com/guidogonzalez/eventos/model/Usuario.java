package com.guidogonzalez.eventos.model;

import java.util.Date;

public class Usuario {

    public String _id;
    public String nombre;
    public String apellidos;
    public String email;
    public Date fechaNacimiento;
    public String foto;
    public String contrasena;

    public Usuario() { }
    public Usuario(String _id, String nombre, String apellidos, String email, Date fechaNacimiento, String foto, String contrasena) {
        this._id = _id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
        this.contrasena = contrasena;
    }
}
