package com.uns.model;

import java.sql.Connection;

public abstract class AbstractDAO {

    protected Connection getConnection() {
        return Conexion.getInstance().getConnection();
    }

    protected void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
