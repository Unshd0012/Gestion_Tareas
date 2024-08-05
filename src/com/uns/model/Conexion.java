package com.uns.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    private static Conexion instance;
    private Connection connection;
    private String url = "jdbc:sqlite:db/base.db";

    // Constructor privado para evitar instanciación externa
    private Conexion() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al realizar la conexion: " + ex.getMessage());
        }
    }


    public static Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        } else try {
            if (instance.getConnection().isClosed()) {
                instance = new Conexion();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }
}