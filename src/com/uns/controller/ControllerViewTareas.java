package com.uns.controller;

import com.uns.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField creationDateField;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private ComboBox<Integer> dueHourPicker;
    @FXML
    private ComboBox<Integer> dueMinutePicker;
    @FXML
    private DatePicker completionDatePicker;
    @FXML
    private ComboBox<Integer> completionHourPicker;
    @FXML
    private ComboBox<Integer> completionMinutePicker;
    @FXML
    private TextField creatorField;
    @FXML
    private TextField responsibleField;
    @FXML
    private ComboBox<String> completedComboBox;
    @FXML
    private ComboBox<String> priorityComboBox;
    @FXML
    private TextField tagsField;

    private ObservableList<Tarea> tareasList = FXCollections.observableArrayList();

    private TareaDAO tareaDAO;
    private boolean isEditing = false;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private void initialize() {
        tareaDAO = new TareaDAO();
        configurarColumnas();
        loadDataFromDatabase();

        tasksTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mostrarDetallesTarea(newValue);
        });

        // Inicializar los valores de los ComboBox
        completedComboBox.setItems(FXCollections.observableArrayList("true", "false"));
        priorityComboBox.setItems(FXCollections.observableArrayList("ALTA", "MEDIA", "BAJA"));

        // Inicializar valores para las horas y minutos
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i += 5) { // incrementos de 5 minutos
            minutes.add(i);
        }
        dueHourPicker.setItems(hours);
        dueMinutePicker.setItems(minutes);
        completionHourPicker.setItems(hours);
        completionMinutePicker.setItems(minutes);
    }

    private void configurarColumnas() {
        // Configurar columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Configurar las columnas de fecha con formateo
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
                setText(empty ? "" : item != null ? item.format(formatter) : "");
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
                        if(user != null){
                            setText(user.getNombre());
                        } else {
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

    private void mostrarDetallesTarea(Tarea tarea) {
        if (tarea != null) {
            idField.setText(String.valueOf(tarea.getId()));
            titleField.setText(tarea.getTitulo());
            descriptionField.setText(tarea.getDescripcion());
            creationDateField.setText(tarea.getFechaCreacion().format(formatter));
            dueDatePicker.setValue(tarea.getFechaVencimiento().toLocalDate());
            dueHourPicker.setValue(tarea.getFechaVencimiento().getHour());
            dueMinutePicker.setValue(tarea.getFechaVencimiento().getMinute());
            if (tarea.getFechaFinalizacion() != null) {
                completionDatePicker.setValue(tarea.getFechaFinalizacion().toLocalDate());
                completionHourPicker.setValue(tarea.getFechaFinalizacion().getHour());
                completionMinutePicker.setValue(tarea.getFechaFinalizacion().getMinute());
            } else {
                completionDatePicker.setValue(null);
                completionHourPicker.setValue(null);
                completionMinutePicker.setValue(null);
            }
            creatorField.setText(tarea.getUsuarioCreador());
            responsibleField.setText(String.valueOf(tarea.getUsuarioResponsable()));
            completedComboBox.setValue(tarea.completadaProperty().getValue() ? "true" : "false");
            priorityComboBox.setValue(tarea.getPrioridad().name());
            tagsField.setText(String.join(", ", tarea.getEtiquetas()));
        } else {
            idField.setText("");
            titleField.setText("");
            descriptionField.setText("");
            creationDateField.setText("");
            dueDatePicker.setValue(null);
            dueHourPicker.setValue(null);
            dueMinutePicker.setValue(null);
            completionDatePicker.setValue(null);
            completionHourPicker.setValue(null);
            completionMinutePicker.setValue(null);
            creatorField.setText("");
            responsibleField.setText("");
            completedComboBox.setValue("");
            priorityComboBox.setValue("");
            tagsField.setText("");
        }
    }

    @FXML
    private void handleAddTask() {
        // Implementación del manejo para añadir una nueva tarea
        Dialog<Tarea> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");
        dialog.setHeaderText("Enter Task Details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        DatePicker dueDatePicker = new DatePicker();
        ComboBox<Integer> dueHourPicker = new ComboBox<>();
        ComboBox<Integer> dueMinutePicker = new ComboBox<>();
        DatePicker completionDatePicker = new DatePicker();
        ComboBox<Integer> completionHourPicker = new ComboBox<>();
        ComboBox<Integer> completionMinutePicker = new ComboBox<>();
        TextField creatorField = new TextField();
        creatorField.setPromptText("Creator");
        TextField responsibleField = new TextField();
        responsibleField.setPromptText("Responsible");
        ComboBox<String> completedComboBox = new ComboBox<>(FXCollections.observableArrayList("true", "false"));
        ComboBox<String> priorityComboBox = new ComboBox<>(FXCollections.observableArrayList("ALTA", "MEDIA", "BAJA"));
        TextField tagsField = new TextField();
        tagsField.setPromptText("Tags");

        ObservableList<Integer> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i += 5) { // incrementos de 5 minutos
            minutes.add(i);
        }
        dueHourPicker.setItems(hours);
        dueMinutePicker.setItems(minutes);
        completionHourPicker.setItems(hours);
        completionMinutePicker.setItems(minutes);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(dueHourPicker, 2, 2);
        grid.add(dueMinutePicker, 3, 2);
        grid.add(new Label("Completion Date:"), 0, 3);
        grid.add(completionDatePicker, 1, 3);
        grid.add(completionHourPicker, 2, 3);
        grid.add(completionMinutePicker, 3, 3);
        grid.add(new Label("Creator:"), 0, 4);
        grid.add(creatorField, 1, 4);
        grid.add(new Label("Responsible:"), 0, 5);
        grid.add(responsibleField, 1, 5);
        grid.add(new Label("Completed:"), 0, 6);
        grid.add(completedComboBox, 1, 6);
        grid.add(new Label("Priority:"), 0, 7);
        grid.add(priorityComboBox, 1, 7);
        grid.add(new Label("Tags:"), 0, 8);
        grid.add(tagsField, 1, 8);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                LocalDate dueDate = dueDatePicker.getValue();
                LocalTime dueTime = LocalTime.of(dueHourPicker.getValue(), dueMinutePicker.getValue(), 0);
                LocalDateTime dueDateTime = LocalDateTime.of(dueDate, dueTime);

                LocalDate completionDate = completionDatePicker.getValue();
                LocalTime completionTime = completionDate != null ? LocalTime.of(completionHourPicker.getValue(), completionMinutePicker.getValue(), 0) : null;
                LocalDateTime completionDateTime = completionDate != null ? LocalDateTime.of(completionDate, completionTime) : null;

                // Crear un formato para fecha y hora hasta segundos
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                // Obtener la fecha y hora actual como una cadena con el formato deseado
                String nowFormatted = LocalDateTime.now().format(formatter);
                // Convertir la cadena de nuevo a LocalDateTime
                LocalDateTime nowWithSeconds = LocalDateTime.parse(nowFormatted, formatter);

               return new Tarea(0,
                        titleField.getText(),
                        descriptionField.getText(),
                        nowWithSeconds,
                        dueDateTime.plusSeconds(1),
                        completionDateTime.plusSeconds(1),
                        creatorField.getText(),
                        Integer.parseInt(responsibleField.getText()),
                        Boolean.parseBoolean(completedComboBox.getValue()),
                        Tarea.Prioridad.valueOf(priorityComboBox.getValue()),
                        Arrays.asList(tagsField.getText().split(","))
                );
            }
            return null;
        });

        Optional<Tarea> result = dialog.showAndWait();

        result.ifPresent(task -> {
            tareasList.add(task);
            tareaDAO.addTarea(task); // Save to the database
            tasksTableView.getItems().clear();
            tasksTableView.refresh();
            loadDataFromDatabase();
        });
    }


    @FXML
    private void handleEditTask() {
        String id = idField.getText();
        if(id.isEmpty()){
            showWarning("Tarea Vacia",null,"No se ha seleccionado ninguna tarea para editar," +
                    "Por favor selecciona un registro");
            return;
        }

        if (isEditing) {
            // Guardar los cambios
            saveChanges();
            setEditableFields(false);
            isEditing = false;
            editButton.setStyle("");
            editButton.setText("Edit Task");
        } else {
            // Permitir la edición
            setEditableFields(true);
            isEditing = true;
            editButton.setStyle("-fx-background-color: #71b6f1; -fx-text-fill: black;");
            editButton.setText("Save Task");
        }
    }

    // Método para permitir o deshabilitar la edición de campos
    private void setEditableFields(boolean editable) {
        titleField.setEditable(editable);
        descriptionField.setEditable(editable);
        dueDatePicker.setDisable(!editable);
        dueHourPicker.setDisable(!editable);
        dueMinutePicker.setDisable(!editable);
        completionDatePicker.setDisable(!editable);
        completionHourPicker.setDisable(!editable);
        completionMinutePicker.setDisable(!editable);
        responsibleField.setEditable(editable);
        completedComboBox.setDisable(!editable);
        priorityComboBox.setDisable(!editable);
        tagsField.setEditable(editable);
    }

    private void saveChanges() {
        try {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String description = descriptionField.getText();

            LocalDate dueDate = dueDatePicker.getValue();
            LocalTime dueTime = LocalTime.of(dueHourPicker.getValue(), dueMinutePicker.getValue());
            LocalDateTime dueDateTime = LocalDateTime.of(dueDate, dueTime);

            LocalDate completionDate = completionDatePicker.getValue();
            LocalTime completionTime = LocalTime.of(completionHourPicker.getValue(), completionMinutePicker.getValue());
            LocalDateTime completionDateTime = LocalDateTime.of(completionDate, completionTime);

            String responsible = responsibleField.getText();
            boolean completed = Boolean.parseBoolean(completedComboBox.getValue());
            Tarea.Prioridad priority = Tarea.Prioridad.valueOf(priorityComboBox.getValue().toUpperCase());
            String tags = tagsField.getText();
            String[] tagsArray = tags.split(",");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            // Crear un nuevo objeto Tarea con los valores actualizados
            String fechaC = creationDateField.getText();
            System.out.println("fechaC = " + fechaC);
            String newF = fechaC.replace(" ","T");
            LocalDateTime newC = LocalDateTime.parse(newF);
            System.out.println("newC = " + newC);
            Tarea updatedTarea = new Tarea(
                    id,
                    title,
                    description,
                    newC.plusSeconds(1),
                    dueDateTime.plusSeconds(1),
                    completionDateTime.plusSeconds(1),
                    creatorField.getText(),
                    Integer.parseInt(responsible),
                    completed,
                    priority,
                    Arrays.asList(tagsArray)
            );

            // Llamar al método de actualización del DAO o servicio correspondiente
            tareaDAO.updateTarea(updatedTarea);

            tasksTableView.getItems().remove(0,tasksTableView.getItems().size());
            tasksTableView.refresh();
            loadDataFromDatabase();

            System.out.println("Cambios guardados para la tarea ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar los cambios para la tarea ID: " + idField.getText());
        }


    }


    @FXML
    private void handleDeleteTask() {
        String id = idField.getText();

        if (id != null && !id.isEmpty()) {
            // Crear un cuadro de diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("¿Estás seguro de que quieres eliminar esta tarea?");
            alert.setContentText("Tarea ID: " + id + " -> "+titleField.getText());

            // Mostrar el cuadro de diálogo y esperar la respuesta del usuario
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // El usuario confirmó la eliminación
                tareaDAO.deleteTarea(Integer.parseInt(id));
                tasksTableView.getItems().remove(0,tasksTableView.getItems().size());
                tasksTableView.refresh();
                loadDataFromDatabase();
            } else {
                // El usuario canceló la eliminación
                System.out.println("Eliminación cancelada por el usuario.");
            }
        } else {
            System.out.println("Id nulo o vacío");
        }
    }

    public static void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
