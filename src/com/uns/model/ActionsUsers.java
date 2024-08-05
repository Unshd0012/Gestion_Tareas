package com.uns.model;

import java.util.List;

public interface ActionsUsers {
    boolean agregarUsuario(Usuario usuario);
    Usuario obtenerUsuarioPorId(int id);
    List<Usuario> obtenerTodosLosUsuarios();
    boolean actualizarUsuario(Usuario usuario);
    boolean eliminarUsuario(int id);
    Usuario obtenerUsuarioPorUsername(String username);
}
