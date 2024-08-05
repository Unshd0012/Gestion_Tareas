package com.uns.controller;

import com.uns.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControllerViewTareas {


    @FXML
    private TableView<Tarea> tasksTableView;

    @FXML
    private TableColumn<Tarea, Integer> idColumn;
    @FXML
    private TableColumn<Tarea, String> titleColumn;
    @FXML
    private TableColumn<Tarea, String> descriptionColumn;
    @FXML
    private TableColumn<Tarea, LocalDateTime> creationDateColumn;
    @FXML
    private TableColumn<Tarea, LocalDateTime> dueDateColumn;
    @FXML
    private TableColumn<Tarea, LocalDateTime> completionDateColumn;
    @FXML
    private TableColumn<Tarea, String> creatorColumn;
    @FXML
    private TableColumn<Tarea, Integer> responsibleColumn;
    @FXML
    private TableColumn<Tarea, Boolean> completedColumn;
    @FXML
    private TableColumn<Tarea, Tarea.Prioridad> priorityColumn;
    @FXML
    private TableColumn<Tarea, String> tagsColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private ObservableList<Tarea> tareasList = FXCollections.observableArrayList();

    private TareaDAO tareaDAO;

    @FXML
    private void initialize() {

        tareaDAO = new TareaDAO();
        configurarColumnas();
        loadDataFromDatabase();
    }

    private void configurarColumnas(){
        // Configurar columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Configurar las columnas de fecha con formateo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        creationDateColumn.setCellValueFactory(cellData -> cellData.getValue().fechaCreacionProperty());
        creationDateColumn.setCellFactory(column -> new TableCell<Tarea, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.format(formatter));
            }
        });

        dueDateColumn.setCellValueFactory(cellData -> cellData.getValue().fechaVencimientoProperty());
        dueDateColumn.setCellFactory(column -> new TableCell<Tarea, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.format(formatter));
            }
        });

        completionDateColumn.setCellValueFactory(cellData -> cellData.getValue().fechaFinalizacionProperty());
        completionDateColumn.setCellFactory(column -> new TableCell<Tarea, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.format(formatter));
            }
        });

        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("usuarioCreador"));
        responsibleColumn.setCellValueFactory(new PropertyValueFactory<>("usuarioResponsable"));
        completedColumn.setCellValueFactory(new PropertyValueFactory<>("completada"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        tagsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.join(", ", cellData.getValue().getEtiquetas())));

        // Configurar la celda personalizada para la columna 'completedColumn'
        completedColumn.setCellFactory(column -> new TableCell<Tarea, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item ? "Completada" : "No Completada");
                    setStyle(item ? "-fx-background-color: green; -fx-text-fill: white;" : "-fx-background-color: transparent;");
                }
            }
        });


// Configurar la celda personalizada para la columna 'priorityColumn' con Enum
        priorityColumn.setCellFactory(column -> new TableCell<Tarea, Tarea.Prioridad>() {
            @Override
            protected void updateItem(Tarea.Prioridad item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item.name()); // O item.toString() si deseas otro formato
                    switch (item) {
                        case ALTA:
                            setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            break;
                        case MEDIA:
                            setStyle("-fx-background-color: orange; -fx-text-fill: white;");
                            break;
                        case BAJA:
                            setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                            break;
                        default:
                            setStyle("-fx-background-color: transparent;");
                            setText("Desconocido");
                            break;
                    }
                }
            }
        });

        creatorColumn.setCellFactory(column -> new TableCell<Tarea, String>() {
            @Override
            protected void updateItem(String userId, boolean empty) {
                super.updateItem(userId, empty);
                if (empty || userId == null) {
                    setText("");
                    setStyle("");
                } else {
                    // Realiza la consulta en un hilo separado
                    Task<Usuario> task = new Task<Usuario>() {
                        @Override
                        protected Usuario call() throws SQLException {
                            UsuariosDAO userDAO = new UsuariosDAO(Conexion.getInstance().getConnection());
                            return userDAO.obtenerUsuarioPorId(Integer.parseInt(userId));
                        }
                    };

                    task.setOnSucceeded(event -> {
                        Usuario user = task.getValue();
                        if(user!=null){
                            setText(user.getNombre());
                        }else{
                            setText("Not user");
                        }

                        setStyle("-fx-text-fill: black;");
                    });

                    task.setOnFailed(event -> {
                        setText("Error");
                        setStyle("-fx-text-fill: red;");
                    });

                    new Thread(task).start();
                }
            }
        });

        // Configurar la tabla
        tasksTableView.setItems(tareasList);
        tasksTableView.setEditable(true);

        // Configurar edición en la tabla si es necesario
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // Otros configuraciones de edición según sea necesario
    }
    private void loadDataFromDatabase() {
        List<Tarea> tareas = tareaDAO.getAllTareas();
        tareasList.setAll(tareas);
    }


    @FXML
    private void handleAddTask(){

    }
    @FXML
    private void handleEditTask(){

    }
    @FXML
    private void handleDeleteTask(){

    }



}
