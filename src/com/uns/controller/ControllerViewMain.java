package com.uns.controller;

import com.uns.model.Conexion;
import com.uns.model.Usuario;
import com.uns.model.UsuariosDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sun.applet.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ControllerViewMain {
    @FXML
    private ImageView logoImageView;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLogin;

    private Stage stage;

    @FXML
    private void initialize() {
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/uns/res/img/task.png")));
        logoImageView.setImage(logoImage);
    }

    public void setStage(Stage stage){
        this.stage  = stage;
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try  {
            UsuariosDAO usuariosDao = new UsuariosDAO();
            Usuario usuario = usuariosDao.obtenerUsuarioPorUsername(username);

            if (usuario != null && usuario.getPassword().equals(password)) {
               // showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + usuario.getNombre() + "!");

                setMessageLogin("Login Successful - Welcome, " + usuario.getNombre() + "!");
                launchHome();

            } else {
               // showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
                setMessageLogin("Login Failed - Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
           // showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
            setMessageLogin( "Database Error - An error occurred while accessing the database.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void setMessageLogin(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageLogin.setText(message);
            }
        });
    }

    private void launchHome(){
        try {
            Stage stage = new Stage();
            FXMLLoader loaderHome = new FXMLLoader(getClass().getResource("/com/uns/view/view_home.fxml"));
            Parent root = loaderHome.load();
            ControllerViewTareas controllerViewHome = loaderHome.getController();
            Scene sceneMain = new Scene(root);
            sceneMain.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/uns/res/css/home.css")).toExternalForm());
            stage.setScene(sceneMain);
            stage.getIcons().add(new Image("/com/uns/res/img/to-do-list.png"));
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setTitle("Home - Tareas");
            stage.show();
            this.stage.close();
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
}
