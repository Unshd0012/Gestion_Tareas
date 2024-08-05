package com.uns.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Tarea {

    private final IntegerProperty id;
    private final StringProperty titulo;
    private final StringProperty descripcion;
    private final ObjectProperty<LocalDateTime> fechaCreacion;
    private final ObjectProperty<LocalDateTime> fechaVencimiento;
    private final ObjectProperty<LocalDateTime> fechaFinalizacion;
    private final StringProperty usuarioCreador;
    private final IntegerProperty usuarioResponsable;
    private final BooleanProperty completada;
    private final ObjectProperty<Prioridad> prioridad;
    private final ListProperty<String> etiquetas;

    public Tarea(int id, String titulo, String descripcion, LocalDateTime fechaCreacion,
                 LocalDateTime fechaVencimiento, LocalDateTime fechaFinalizacion, String usuarioCreador,
                 int usuarioResponsable, boolean completada, Prioridad prioridad, List<String> etiquetas) {
        this.id = new SimpleIntegerProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.fechaCreacion = new SimpleObjectProperty<>(fechaCreacion);
        this.fechaVencimiento = new SimpleObjectProperty<>(fechaVencimiento);
        this.fechaFinalizacion = new SimpleObjectProperty<>(fechaFinalizacion);
        this.usuarioCreador = new SimpleStringProperty(usuarioCreador);
        this.usuarioResponsable = new SimpleIntegerProperty(usuarioResponsable);
        this.completada = new SimpleBooleanProperty(completada);
        this.prioridad = new SimpleObjectProperty<>(prioridad);
        this.etiquetas = new SimpleListProperty<>(FXCollections.observableArrayList(etiquetas));
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitulo() {
        return titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion.get();
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion.set(fechaCreacion);
    }

    public ObjectProperty<LocalDateTime> fechaCreacionProperty() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento.get();
    }

    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento.set(fechaVencimiento);
    }

    public ObjectProperty<LocalDateTime> fechaVencimientoProperty() {
        return fechaVencimiento;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion.get();
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion.set(fechaFinalizacion);
    }

    public ObjectProperty<LocalDateTime> fechaFinalizacionProperty() {
        return fechaFinalizacion;
    }

    public String getUsuarioCreador() {
        return usuarioCreador.get();
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador.set(usuarioCreador);
    }

    public StringProperty usuarioCreadorProperty() {
        return usuarioCreador;
    }

    public int getUsuarioResponsable() {
        return usuarioResponsable.get();
    }

    public void setUsuarioResponsable(int usuarioResponsable) {
        this.usuarioResponsable.set(usuarioResponsable);
    }

    public IntegerProperty usuarioResponsableProperty() {
        return usuarioResponsable;
    }

    public boolean isCompletada() {
        return completada.get();
    }

    public void setCompletada(boolean completada) {
        this.completada.set(completada);
    }

    public BooleanProperty completadaProperty() {
        return completada;
    }

    public Prioridad getPrioridad() {
        return prioridad.get();
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad.set(prioridad);
    }

    public ObjectProperty<Prioridad> prioridadProperty() {
        return prioridad;
    }

    public List<String> getEtiquetas() {
        return etiquetas.get();
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas.setAll(etiquetas);
    }

    public ListProperty<String> etiquetasProperty() {
        return etiquetas;
    }

    // Enumeración para Prioridad
    public enum Prioridad {
        BAJA, MEDIA, ALTA
    }
}
