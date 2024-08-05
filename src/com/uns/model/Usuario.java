package com.uns.model;

public class Usuario {
    private int id;
    private  String nombre;
    private String Apellido;
    private String correo;
    private String password;
    private String user;


    public Usuario(int id, String nombre, String apellido, String correo, String password, String user) {
        this.id = id;
        this.nombre = nombre;
        Apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.user = user;
    }

    public Usuario() {

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

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", Apellido='" + Apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", password='" + password + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}


