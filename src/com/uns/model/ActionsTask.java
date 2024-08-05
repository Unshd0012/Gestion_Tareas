package com.uns.model;

import java.util.List;

public interface ActionsTask {

    List<Tarea> getAllTareas();

    Tarea getTareaById(int id);

    void addTarea(Tarea tarea);

    void updateTarea(Tarea tarea);

    void deleteTarea(int id);
}
