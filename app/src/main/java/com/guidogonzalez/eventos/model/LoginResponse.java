package com.guidogonzalez.eventos.model;

public class LoginResponse {

    public String _id;
    public String email;
    public String contrasena;
    public String token;

    public LoginResponse(String _id, String email, String contrasena, String token) {
        this._id = _id;
        this.email = email;
        this.contrasena = contrasena;
        this.token = token;
    }
}
