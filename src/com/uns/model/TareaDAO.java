package com.uns.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO extends AbstractDAO implements ActionsTask {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Tarea> getAllTareas() {
        List<Tarea> tareas = new ArrayList<>();
        String query = "SELECT * FROM Tarea";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Tarea tarea = extractTareaFromResultSet(rs);
                tareas.add(tarea);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }

    @Override
    public Tarea getTareaById(int id) {
        Tarea tarea = null;
        String query = "SELECT * FROM Tarea WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tarea = extractTareaFromResultSet(rs);
                }
                closeResources(rs);
            }
            closeResources(pstmt,conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarea;
    }

    @Override
    public void addTarea(Tarea tarea) {
        String query = "INSERT INTO Tarea (titulo, descripcion, fechaCreacion, fechaVencimiento, fechaFinalizacion, usuarioCreador, usuarioResponsable, completada, prioridad, etiquetas) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            setTareaParameters(pstmt, tarea);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTarea(Tarea tarea) {
        String query = "UPDATE Tarea SET titulo = ?, descripcion = ?, fechaCreacion = ?, fechaVencimiento = ?, fechaFinalizacion = ?, usuarioCreador = ?, usuarioResponsable = ?, completada = ?, prioridad = ?, etiquetas = ? " +
                "WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            setTareaParameters(pstmt, tarea);
            pstmt.setInt(6,Integer.parseInt(tarea.getUsuarioCreador()));
            pstmt.setInt(11, tarea.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTarea(int id) {
        String query = "DELETE FROM Tarea WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Tarea extractTareaFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titulo = rs.getString("titulo");
        String descripcion = rs.getString("descripcion");

        String fechaCreacionStr = rs.getString("fechaCreacion");
        LocalDateTime fechaCreacion = parseDateTime(fechaCreacionStr);

        String fechaVencimientoStr = rs.getString("fechaVencimiento");
        LocalDateTime fechaVencimiento = parseDateTime(fechaVencimientoStr);

        String fechaFinalizacionStr = rs.getString("fechaFinalizacion");
        LocalDateTime fechaFinalizacion = parseDateTime(fechaFinalizacionStr);

        int usuarioCreadorx = rs.getInt("usuarioCreador");
        String usuarioCreador = usuarioCreadorx + "";
        int usuarioResponsable = rs.getInt("usuarioResponsable");
        boolean completada = rs.getBoolean("completada");
        Tarea.Prioridad prioridad = Tarea.Prioridad.valueOf(rs.getString("prioridad"));
        List<String> etiquetas = parseEtiquetas(rs.getString("etiquetas"));

        return new Tarea(id, titulo, descripcion, fechaCreacion, fechaVencimiento, fechaFinalizacion,
                usuarioCreador, usuarioResponsable, completada, prioridad, etiquetas);
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr != null && !dateTimeStr.isEmpty()) {
            try {
                return LocalDateTime.parse(dateTimeStr, FORMATTER);
            } catch (Exception e) {
                return LocalDateTime.parse(dateTimeStr, FORMATTER2);
            }
        }
        return null;
    }

    private void setTareaParameters(PreparedStatement pstmt, Tarea tarea) throws SQLException {
        pstmt.setString(1, tarea.getTitulo());
        pstmt.setString(2, tarea.getDescripcion());
        pstmt.setObject(3, tarea.getFechaCreacion());
        pstmt.setObject(4, tarea.getFechaVencimiento());
        pstmt.setObject(5, tarea.getFechaFinalizacion());

        List<Usuario> usuarios = new UsuariosDAO().obtenerTodosLosUsuarios();
        int idCreador = usuarios.stream()
                .filter(usuario -> usuario.getNombre().equals(tarea.getUsuarioCreador()))
                .map(Usuario::getId)
                .findFirst()
                .orElse(0);

        pstmt.setInt(6, idCreador);
        pstmt.setInt(7, tarea.getUsuarioResponsable());
        pstmt.setBoolean(8, tarea.isCompletada());
        pstmt.setString(9, tarea.getPrioridad().name());
        pstmt.setString(10, String.join(", ", tarea.getEtiquetas()));
    }

    private List<String> parseEtiquetas(String etiquetasStr) {
        List<String> etiquetas = new ArrayList<>();
        if (etiquetasStr != null && !etiquetasStr.isEmpty()) {
            String[] tagsArray = etiquetasStr.split(", ");
            for (String tag : tagsArray) {
                etiquetas.add(tag);
            }
        }
        return etiquetas;
    }
}
